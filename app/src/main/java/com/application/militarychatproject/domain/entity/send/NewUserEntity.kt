package com.application.militarychatproject.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class NewUserEntity(
    val login: String,
    val password: String,
    val name: String,
    val nickname: String
)
