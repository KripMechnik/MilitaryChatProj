package com.application.timer_dmb.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpResetBodyEntity(
    val otp: Int,
    val email: String
)
