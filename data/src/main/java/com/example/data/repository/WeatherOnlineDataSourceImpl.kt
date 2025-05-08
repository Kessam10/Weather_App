package com.example.data.repository

import com.example.domain.ApiResult
import com.example.domain.entity.DailyWeatherEntity
import com.example.domain.repository.WeatherOnlineDataSource
import com.example.data.dataSource.WeatherDataSource
import com.example.data.mappers.toEntity
import com.example.domain.error.ErrorType

class WeatherOnlineDataSourceImpl(
    private val weatherDataSource: WeatherDataSource
) : WeatherOnlineDataSource {

    override  fun fetchWeather(lat: Double, lon: Double): ApiResult<List<DailyWeatherEntity>> {
        return try {
            val model = weatherDataSource.fetchWeather(lat, lon)
            val entity = model.toEntity()
            ApiResult.Success(entity.forecast ?: emptyList())
        } catch (e: Exception) {
            ApiResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }
}
