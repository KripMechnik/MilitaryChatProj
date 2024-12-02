package com.application.militarychatproject.domain.entity.receive

data class TokenEntity(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: Long,
    val refreshTokenExpiresAt: Long
)