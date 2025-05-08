package com.example.domain.repository

import com.example.domain.ApiResult
import com.example.domain.entity.DailyWeatherEntity


interface WeatherRepository {
     fun getWeather(lat: Double, lon: Double): ApiResult<List<DailyWeatherEntity>>
}

interface WeatherOnlineDataSource {
     fun fetchWeather(lat: Double, lon: Double): ApiResult<List<DailyWeatherEntity>>
}