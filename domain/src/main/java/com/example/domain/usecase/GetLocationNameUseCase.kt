package com.example.domain.usecase

import com.example.domain.repository.LocationRepository

class GetLocationNameUseCase(private val repository: LocationRepository) {
     operator fun invoke(lat: Double, lon: Double): String {
        return repository.getStateAndCountry(lat, lon)
    }
}