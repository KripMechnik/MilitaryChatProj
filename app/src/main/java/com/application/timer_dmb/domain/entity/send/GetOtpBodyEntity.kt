package com.application.timer_dmb.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class GetOtpBodyEntity(
    val email: String
)
