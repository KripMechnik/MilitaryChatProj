package com.application.timer_dmb.data.remote

import com.application.timer_dmb.data.remote.dto.TokenDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.data.remote.network.BaseRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.util.StringValues
import javax.inject.Inject

class RefreshTokenRequest @Inject constructor(
    private val baseRequest: BaseRequest
) {

    private val basePath = "/auth/"

    suspend fun refreshTokenRequest(refreshToken: String) : ApiResponse<TokenDTO> {
        return baseRequest(
            method = HttpMethod.Get,
            path = basePath + "refresh-token",
            headers = StringValues.build {
                append(HttpHeaders.Authorization, refreshToken)
            }
        )
    }
}