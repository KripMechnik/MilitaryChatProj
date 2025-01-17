package com.application.timer_dmb.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordEntity(
    val password: String,
    val email: String
)
