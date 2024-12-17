package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.data.remote.dto.TokenDTO
import com.application.militarychatproject.data.remote.network.ApiResponse

interface RefreshTokenRepository {
    suspend fun refreshToken(refreshToken: String) : ApiResponse<TokenDTO>
}