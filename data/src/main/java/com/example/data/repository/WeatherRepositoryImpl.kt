package com.example.data.repository

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.example.data.cashe.WeatherCacheHelper
import com.example.data.mappers.DailyWeatherEntityMapper
import com.example.data.mappers.toRawString
import com.example.data.utils.NetworkUtils
import com.example.domain.ApiResult
import com.example.domain.entity.DailyWeatherEntity
import com.example.domain.error.ErrorType
import com.example.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val remote: WeatherOnlineDataSourceImpl,
    private val context: Context
) : WeatherRepository {

    private val cacheHelper = WeatherCacheHelper(context)

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun getWeather(lat: Double, lon: Double): ApiResult<List<DailyWeatherEntity>> {
        return if (NetworkUtils.isConnected(context)) {
            val result = remote.fetchWeather(lat, lon)
            if (result is ApiResult.Success) {
                val json = serialize(result.data)
                cacheHelper.saveWeatherData(json)
            }
            result
        } else {
            val json = cacheHelper.getLastWeatherData()
            if (json != null) {
                val cachedData = deserialize(json)
                ApiResult.Success(cachedData)
            } else {
                ApiResult.Error(ErrorType.NO_INTERNET)
            }
        }
    }

private fun serialize(data: List<DailyWeatherEntity>): String {
    return data.joinToString("||") { it.toRawString() }
}

    private fun deserialize(json: String): List<DailyWeatherEntity> {
        return json.split("||").mapNotNull {
            if (it.isNotBlank()) {
                try {
                    DailyWeatherEntityMapper.fromRawString(it)
                } catch (e: Exception) {
                    null
                }
            } else null
        }
    }

}
