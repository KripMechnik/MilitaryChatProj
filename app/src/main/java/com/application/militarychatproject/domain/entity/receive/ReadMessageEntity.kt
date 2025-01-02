package com.application.militarychatproject.domain.entity.receive

import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageEntity(
    val chatId: Int
)
