package com.application.militarychatproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageWebSocketDTO (
    val type: String = "chat",
    val name: String = "all_messages_read",
    val data: ReadMessageDTO
)
