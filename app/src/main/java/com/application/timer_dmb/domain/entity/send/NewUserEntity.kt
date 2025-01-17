package com.application.timer_dmb.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class NewUserEntity(
    val login: String,
    val password: String,
    val nickname: String
)
