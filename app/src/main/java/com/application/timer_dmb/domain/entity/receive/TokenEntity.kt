package com.application.timer_dmb.domain.entity.receive

data class TokenEntity(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: Long,
    val refreshTokenExpiresAt: Long
)