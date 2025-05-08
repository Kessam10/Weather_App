package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.data.dataSource.WeatherDataSource
import com.example.data.repository.LocationRepositoryImpl
import com.example.data.repository.NetworkRepositoryImpl
import com.example.data.repository.WeatherOnlineDataSourceImpl
import com.example.data.repository.WeatherRepositoryImpl
import com.example.domain.usecase.CheckNetworkConnectionUseCase
import com.example.domain.usecase.GetLocationNameUseCase
import com.example.domain.usecase.GetWeatherUseCase
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewModel.WeatherViewModel
import com.example.weatherapp.viewModel.WeatherViewModelFactory
import com.example.weatherapp.fragment.ForecastFragment
import com.example.weatherapp.fragment.TodayWeatherFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val weatherDataSource = WeatherDataSource()
        val weatherOnlineSource = WeatherOnlineDataSourceImpl(weatherDataSource)
        val weatherRepository = WeatherRepositoryImpl(weatherOnlineSource, this)
        val getWeatherUseCase = GetWeatherUseCase(weatherRepository)
        val locationRepository = LocationRepositoryImpl(this)
        val getLocationNameUseCase = GetLocationNameUseCase(locationRepository)
        val checkNetworkConnectionUseCase = CheckNetworkConnectionUseCase(NetworkRepositoryImpl(this))

        viewModel = ViewModelProvider(
            this,
            WeatherViewModelFactory(getLocationNameUseCase, getWeatherUseCase, checkNetworkConnectionUseCase)
        )[WeatherViewModel::class.java]


        binding.bottomNavView.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.today_weather -> TodayWeatherFragment()
                R.id.forecast_weather -> ForecastFragment()
                else -> TodayWeatherFragment()
            }
            showFragment(fragment)
            true
        }
        if (savedInstanceState == null) {
            binding.bottomNavView.selectedItemId = R.id.today_weather
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.weatherFragmentContainer.id, fragment)
            .commit()
    }
}
