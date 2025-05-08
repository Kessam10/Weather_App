package com.example.data.repository

import android.content.Context
import android.location.Geocoder
import com.example.data.R
import com.example.domain.repository.LocationRepository
import java.util.*

class LocationRepositoryImpl(private val context: Context) : LocationRepository {
    override fun getStateAndCountry(lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val state = address.adminArea ?: context.getString(R.string.unknown_state)
                val country = address.countryName ?: context.getString(R.string.unknown_country)
                "$state, $country"
            } else {
                context.getString(R.string.no_location_found)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            context.getString(R.string.error_getting_location)
        }
    }
}
