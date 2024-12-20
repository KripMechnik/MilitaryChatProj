package com.application.militarychatproject.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpBodyEntity(
    val login: String,
    val otp: String
)
