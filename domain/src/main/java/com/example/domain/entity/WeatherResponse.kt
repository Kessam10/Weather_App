package com.example.domain.entity

data class WeatherResponse(
	val address: String? = null,
	val currentConditions: CurrentWeatherEntity? = null,
	val timezone: String? = null,
	val description: String? = null,
	val forecast: List<DailyWeatherEntity>? = null,
)

data class CurrentWeatherEntity(
	val icon: String? = null,
	val datetime: String? = null,
	val temp: Any? = null,
	val conditions: String? = null
)

data class DailyWeatherEntity(
	val icon: String? = null,
	val description: String? = null,
	val datetime: String? = null,
	val tempmin: Any? = null,
	val temp: Any? = null,
	val tempmax: Any? = null,
	val conditions: String? = null,
	val hours:List<HourlyWeather>?=null
)

data class HourlyWeather(
	val datetime: String,
	val datetimeEpoch: Long,
	val temp: Double,
	val feelslike: Double,
	val humidity: Double,
	val dew: Double,
	val icon: String? = null,

	)





