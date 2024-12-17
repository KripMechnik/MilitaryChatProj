package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.RefreshTokenRequest
import com.application.militarychatproject.data.remote.dto.TokenDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.repository.RefreshTokenRepository
import javax.inject.Inject

class RefreshTokenRepoImpl @Inject constructor(
    private val request: RefreshTokenRequest
) : RefreshTokenRepository {
    override suspend fun refreshToken(refreshToken: String): ApiResponse<TokenDTO> {
        return request.refreshTokenRequest(refreshToken)
    }
}