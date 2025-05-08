package com.example.data.parse

import com.example.data.model.CurrentConditionsModel
import com.example.data.model.DayModel
import com.example.data.model.HourlyWeatherModel
import com.example.data.model.WeatherResponseModel
import org.json.JSONObject

class MyGsonParser {
    fun parseWeatherResponse(json: String): WeatherResponseModel? {
        return try {
            val jsonObject = JSONObject(json)
            val address = jsonObject.optString("address")
            val timezone = jsonObject.optString("timezone")
            val description = jsonObject.optString("description")
            val currentConditions = jsonObject.optJSONObject("currentConditions")?.let {
                CurrentConditionsModel(
                    datetime = it.optString("datetime"),
                    icon = it.optString("icon"),
                    temp = it.optDouble("temp"),
                    conditions = it.optString("conditions")
                )
            }
            val forecastList = mutableListOf<DayModel>()
            val daysArray = jsonObject.optJSONArray("days")
            daysArray?.let { days ->
                for (i in 0 until days.length()) {
                    val dayObject = days.getJSONObject(i)
                    val dayModel = DayModel(
                        datetime = dayObject.optString("datetime"),
                        icon = dayObject.optString("icon"),
                        tempmin = dayObject.optDouble("tempmin", 0.0),
                        tempmax = dayObject.optDouble("tempmax", 0.0),
                        temp = dayObject.optDouble("temp", 0.0),
                        conditions = dayObject.optString("conditions"),
                        description = dayObject.optString("description"),
                        hours = dayObject.optJSONArray("hours")?.let { hoursArray ->
                            val hourList = mutableListOf<HourlyWeatherModel>()
                            for (j in 0 until hoursArray.length()) {
                                val hourObj = hoursArray.getJSONObject(j)
                                val hour = HourlyWeatherModel(
                                    datetime = hourObj.optString("datetime"),
                                    datetimeEpoch = hourObj.optLong("datetimeEpoch"),
                                    temp = hourObj.optDouble("temp"),
                                    feelslike = hourObj.optDouble("feelslike"),
                                    humidity = hourObj.optDouble("humidity"),
                                    dew = hourObj.optDouble("dew"),
                                    icon = dayObject.optString("icon"),
                                    )
                                hourList.add(hour)
                            }
                            hourList
                        }
                    )
                    forecastList.add(dayModel)
                }
            }
            WeatherResponseModel(
                address = address,
                timezone = timezone,
                description = description,
                currentConditions = currentConditions,
                forecast = forecastList
            )

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
