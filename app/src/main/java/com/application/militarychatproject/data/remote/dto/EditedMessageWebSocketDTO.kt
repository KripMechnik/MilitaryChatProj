package com.application.militarychatproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EditedMessageWebSocketDTO (
    val type: String = "chat",
    val name: String = "message_edited",
    val data: EditedMessageDTO
)