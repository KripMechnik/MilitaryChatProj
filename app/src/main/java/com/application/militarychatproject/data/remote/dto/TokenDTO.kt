package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.TokenEntity
import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(
    val accessToken: String,
    val accessTokenExpiresAt: Long,
    val refreshToken: String,
    val refreshTokenExpiresAt: Long
)

fun TokenDTO.toTokenEntity() = TokenEntity(
    accessToken = this.accessToken,
    accessTokenExpiresAt = this.accessTokenExpiresAt,
    refreshToken = this.refreshToken,
    refreshTokenExpiresAt = this.refreshTokenExpiresAt
)