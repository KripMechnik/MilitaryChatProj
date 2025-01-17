package com.application.timer_dmb.data.remote.network

sealed class ApiResponse<T>(val data: T? = null, val errorCode: Int? = null, val errorMessage: String? = null) {
    class Success<T>(data: T? = null) : ApiResponse<T>(data)
    class Error<T>(errorCode: Int, errorMessage: String, data: T? = null) : ApiResponse<T>(data, errorCode, errorMessage)
}