package com.application.militarychatproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeletedMessageDTO(
    val chatId: Int,
    val messageId: Int
)
