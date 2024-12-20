package com.application.militarychatproject.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class GetOtpBodyEntity(
    val email: String
)
