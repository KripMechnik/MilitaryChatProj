package com.application.timer_dmb.domain.repository

import com.application.timer_dmb.data.remote.dto.TokenDTO
import com.application.timer_dmb.data.remote.network.ApiResponse

interface RefreshTokenRepository {
    suspend fun refreshToken(refreshToken: String) : ApiResponse<TokenDTO>
}