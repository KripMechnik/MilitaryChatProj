package com.application.militarychatproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewMessageWebSocketDTO (
    val type: String = "chat",
    val name: String = "message_sent",
    val data: MessageDTO
)