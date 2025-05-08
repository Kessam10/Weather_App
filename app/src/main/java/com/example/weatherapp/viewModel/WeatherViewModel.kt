package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.ApiResult
import com.example.domain.usecase.CheckNetworkConnectionUseCase
import com.example.domain.usecase.GetLocationNameUseCase
import com.example.domain.usecase.GetWeatherUseCase
import com.example.weatherapp.state.WeatherUiState
import java.util.concurrent.Executors

class WeatherViewModel(
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val checkNetworkConnectionUseCase: CheckNetworkConnectionUseCase
) : ViewModel() {

    val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val _networkState = MutableLiveData<Boolean>()
    val networkState: LiveData<Boolean> = _networkState
    private val _uiState = MutableLiveData<WeatherUiState>()
    val uiState: LiveData<WeatherUiState> get() = _uiState
    private val executor = Executors.newSingleThreadExecutor()
    private var lastFetchTime: Long = 0L
    private val fetchCooldown = 5 * 60 * 1000
    var lastLat: Double? = null
    var lastLon: Double? = null

    fun shouldFetchWeather(): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastFetchTime) > fetchCooldown || lastLat == null || lastLon == null
    }


    fun loadWeather(lat: Double, lon: Double) {
        lastFetchTime = System.currentTimeMillis()
        lastLat = lat
        lastLon = lon
        _uiState.postValue(WeatherUiState.Loading)
        executor.execute {
            when (val result = getWeatherUseCase(lat, lon)) {
                is ApiResult.Loading -> _uiState.postValue(WeatherUiState.Loading)
                is ApiResult.Success -> _uiState.postValue(WeatherUiState.Success(result.data))
                is ApiResult.Error -> _uiState.postValue(WeatherUiState.Error(result.errorMessage))
                else -> {}
            }
        }
    }

    fun checkNetworkConnection() {
        _networkState.value = checkNetworkConnectionUseCase()
    }
    fun getStateAndCountry(lat: Double, lon: Double, callback: (String) -> Unit) {
        executor.execute {
            val location = getLocationNameUseCase(lat, lon)
            callback(location)
        }
    }
}
