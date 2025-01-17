package com.application.timer_dmb.data.repository

import com.application.timer_dmb.data.remote.RefreshTokenRequest
import com.application.timer_dmb.data.remote.dto.TokenDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.repository.RefreshTokenRepository
import javax.inject.Inject

class RefreshTokenRepoImpl @Inject constructor(
    private val request: RefreshTokenRequest
) : RefreshTokenRepository {
    override suspend fun refreshToken(refreshToken: String): ApiResponse<TokenDTO> {
        return request.refreshTokenRequest(refreshToken)
    }
}