package com.example.domain.usecase

import com.example.domain.ApiResult
import com.example.domain.entity.DailyWeatherEntity
import com.example.domain.entity.WeatherResponse
import com.example.domain.repository.WeatherRepository

class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    operator fun invoke(lat: Double, lon: Double): ApiResult<List<DailyWeatherEntity>> {
        return repository.getWeather(lat, lon)
    }
}
