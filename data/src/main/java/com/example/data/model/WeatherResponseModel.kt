package com.example.data.model

data class WeatherResponseModel(
    val address: String? = null,
    val timezone: String? = null,
    val currentConditions: CurrentConditionsModel? = null,
    val forecast: List<DayModel>? = null,
    val description: String? = null,
)

data class CurrentConditionsModel(
    val datetime: String? = null,
    val temp: Double? = null,
    val icon: String? = null,
    val conditions: String? = null
)

data class DayModel(
    val description: String? = null,
    val datetime: String? = null,
    val icon: String? = null,
    val temp: Double? = null,
    val tempmin: Double? = null,
    val tempmax: Double? = null,
    val conditions: String? = null,
    val hours: List<HourlyWeatherModel>? = null
)

data class HourlyWeatherModel(
    val datetime: String,
    val datetimeEpoch: Long,
    val temp: Double,
    val feelslike: Double,
    val humidity: Double,
    val dew: Double,
    val icon: String? = null,

    )


