package com.example.domain

import com.example.domain.error.ErrorType

sealed interface ApiResult<T>{
    class Loading<T>:ApiResult<T>
    data class Error<T>(val errorMessage:ErrorType):ApiResult<T>
    data class Success<T>(val data: T):ApiResult<T>
}