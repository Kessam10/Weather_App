package com.example.domain.repository

interface LocationRepository {
     fun getStateAndCountry(lat: Double, lon: Double): String
}