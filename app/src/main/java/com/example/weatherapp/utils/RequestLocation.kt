package com.example.weatherapp.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import com.example.weatherapp.R

fun Context.requestSingleLocationUpdate(
    onLocationResult: (lat: Double, lon: Double) -> Unit
) {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            onLocationResult(location.latitude, location.longitude)
            locationManager.removeUpdates(this)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    try {
        when {
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener
                )
            }

            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0L, 0f, locationListener
                )
            }
            else -> {
                Toast.makeText(this,
                    getString(R.string.location_providers_are_disabled), Toast.LENGTH_LONG).show()
            }
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}
