package com.example.data.mappers

import com.example.data.model.WeatherResponseModel
import com.example.domain.entity.CurrentWeatherEntity
import com.example.domain.entity.DailyWeatherEntity
import com.example.domain.entity.HourlyWeather
import com.example.domain.entity.WeatherResponse

fun WeatherResponseModel.toEntity(): WeatherResponse {
    return WeatherResponse(
        address = address ?: "",
        timezone = timezone ?: "",
        currentConditions = currentConditions?.let {
            CurrentWeatherEntity(
                temp = (it.temp as? Double) ?: 0.0,
                icon = it.icon ?: "",
                datetime = it.datetime ?: "",
                conditions = it.conditions ?: ""
            )
        } ?: CurrentWeatherEntity(icon = "", datetime = "", temp = 0.0, conditions = ""),
        forecast = forecast?.map {
            DailyWeatherEntity(
                datetime = it.datetime ?: "",
                icon = it.icon ?: "",
                tempmin = (it.tempmin as? Double) ?: 0.0,
                tempmax = (it.tempmax as? Double) ?: 0.0,
                conditions = it.conditions ?: "",
                temp = (it.temp as? Double) ?: 0.0,
                description = it.description?:"",
                hours = it.hours?.let { list ->
                    list.map { hour ->
                        HourlyWeather(
                            datetime = hour.datetime,
                            datetimeEpoch = hour.datetimeEpoch,
                            temp = hour.temp,
                            feelslike = hour.feelslike,
                            humidity = hour.humidity,
                            dew = hour.dew,
                            icon = hour.icon
                        )
                    }
                }

            )
        } ?: emptyList()
    )
}

fun DailyWeatherEntity.toRawString(): String {
    val hourlyString = hours?.joinToString(";") {
        listOf(
            it.datetime,
            it.datetimeEpoch,
            it.temp,
            it.feelslike,
            it.humidity,
            it.dew
        ).joinToString(":")
    }

    return listOf(
        datetime,
        tempmin,
        tempmax,
        conditions,
        icon,
        temp,
        description,
        hourlyString
    ).joinToString("|")
}

object DailyWeatherEntityMapper {
    fun fromRawString(raw: String): DailyWeatherEntity {
        val parts = raw.split("|")
        val hoursRaw = if (parts.size >= 8) parts[7] else ""
        val hours = hoursRaw.split(";").mapNotNull { hour ->
            val h = hour.split(":")
            if (h.size == 6) {
                try {
                    HourlyWeather(
                        datetime = h[0],
                        datetimeEpoch = h[1].toLong(),
                        temp = h[2].toDouble(),
                        feelslike = h[3].toDouble(),
                        humidity = h[4].toDouble(),
                        dew = h[5].toDouble()
                    )
                } catch (e: Exception) {
                    null
                }
            } else null
        }

        return DailyWeatherEntity(
            datetime = parts[0],
            tempmin = parts[1].toDouble(),
            tempmax = parts[2].toDouble(),
            conditions = parts[3],
            icon = parts[4],
            temp = parts[5].toDouble(),
            description = parts.getOrNull(6) ?: "",
            hours = hours
        )
    }
}
