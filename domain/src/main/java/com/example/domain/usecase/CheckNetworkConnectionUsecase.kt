package com.example.domain.usecase

import com.example.domain.repository.NetworkRepository

class CheckNetworkConnectionUseCase(private val networkRepository: NetworkRepository) {
    operator fun invoke(): Boolean {
        return networkRepository.isConnected()
    }
}
