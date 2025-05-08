package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import com.example.weatherapp.fragment.ForecastFragment
import com.example.weatherapp.fragment.TodayWeatherFragment
import com.example.weatherapp.viewModel.WeatherViewModel
import com.example.weatherapp.viewModel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel

    // Cache the fragments and track which one is active
    private val todayWeatherFragment = TodayWeatherFragment()
    private val forecastFragment = ForecastFragment()
    private var activeFragment: Fragment = todayWeatherFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.background_dark)

        // ViewModel setup
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

        // Add fragments only once
        supportFragmentManager.beginTransaction()
            .add(binding.weatherFragmentContainer.id, forecastFragment, "forecast")
            .hide(forecastFragment)
            .commit()

        supportFragmentManager.beginTransaction()
            .add(binding.weatherFragmentContainer.id, todayWeatherFragment, "today")
            .commit()

        binding.bottomNavView.setOnItemSelectedListener {
            val targetFragment = when (it.itemId) {
                R.id.today_weather -> todayWeatherFragment
                R.id.forecast_weather -> forecastFragment
                else -> todayWeatherFragment
            }

            if (activeFragment != targetFragment) {
                supportFragmentManager.beginTransaction()
                    .hide(activeFragment)
                    .show(targetFragment)
                    .commit()
                activeFragment = targetFragment
            }

            true
        }

        // Set initial tab only on first launch
        if (savedInstanceState == null) {
            binding.bottomNavView.selectedItemId = R.id.today_weather
        }
    }
}
