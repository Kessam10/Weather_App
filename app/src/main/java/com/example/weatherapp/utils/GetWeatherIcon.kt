package com.example.weatherapp.utils

import com.example.weatherapp.R

fun getWeatherIcon(icon: String): Int {
    return when (icon) {
        "clear-day" -> R.drawable.clear_sky
        "clear-night" -> R.drawable.clear_night
        "partly-cloudy-day" -> R.drawable.partly_cloudy_day
        "partly-cloudy-night" -> R.drawable.partly_cloudy_night
        "cloudy" -> R.drawable.cloudy
        "rain" -> R.drawable.rainy
        else -> R.drawable.clear_sky
    }
}
