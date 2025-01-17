package com.application.timer_dmb.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class SignedInUserEntity(
    val login: String,
    val password: String
)
