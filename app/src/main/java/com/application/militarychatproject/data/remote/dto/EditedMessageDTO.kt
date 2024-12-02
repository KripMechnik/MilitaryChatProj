package com.application.militarychatproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EditedMessageDTO(
    val chatId: Int,
    val messageId: Int,
    val text: String
)
