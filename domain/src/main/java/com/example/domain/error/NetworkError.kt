package com.example.domain.error

import com.example.domain.repository.Error

data class NetworkError(
    val errorType: ErrorType? = null
): Error
