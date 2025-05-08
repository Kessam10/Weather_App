package com.example.data.dataSource

import com.example.data.BuildConfig
import com.example.data.model.WeatherResponseModel
import com.example.data.parse.MyGsonParser
import java.net.HttpURLConnection
import java.net.URL

class WeatherDataSource {
    fun fetchWeather(lat: Double, lon: Double): WeatherResponseModel {
        val apiKey = BuildConfig.API_KEY
        val baseUrl = BuildConfig.BASE_URL
        val url = "$baseUrl$lat,$lon?unitGroup=metric&key=$apiKey&contentType=json"

        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return connection.inputStream.bufferedReader().use {
            val response = it.readText()
            parseWeatherJson(response)
        }
    }

    private fun parseWeatherJson(json: String): WeatherResponseModel {
        val gsonParser = MyGsonParser()
        return gsonParser.parseWeatherResponse(json) ?: throw IllegalArgumentException("Parsing failed")
    }
}
