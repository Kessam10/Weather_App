package com.example.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.usecase.CheckNetworkConnectionUseCase
import com.example.domain.usecase.GetLocationNameUseCase
import com.example.domain.usecase.GetWeatherUseCase

class WeatherViewModelFactory(
    private val getLocationNameUseCase: GetLocationNameUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val checkNetworkConnectionUseCase: CheckNetworkConnectionUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(getLocationNameUseCase, getWeatherUseCase,checkNetworkConnectionUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
