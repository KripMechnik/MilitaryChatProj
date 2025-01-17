package com.application.timer_dmb.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpBodyEntity(
    val email: String,
    val otp: Int
)
