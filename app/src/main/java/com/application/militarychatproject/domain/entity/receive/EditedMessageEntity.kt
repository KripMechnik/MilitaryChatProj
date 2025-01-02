package com.application.militarychatproject.domain.entity.receive

import kotlinx.serialization.Serializable

@Serializable
data class EditedMessageEntity(
    val chatId: Int,
    val messageId: Int,
    val text: String
)
