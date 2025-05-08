package com.example.weatherapp.state

import com.example.domain.entity.DailyWeatherEntity
import com.example.domain.error.ErrorType

sealed class WeatherUiState {
        data object Loading : WeatherUiState()
        data class Success(val data: List<DailyWeatherEntity>) : WeatherUiState()
        data class Error(val errMessage: ErrorType) : WeatherUiState()
}