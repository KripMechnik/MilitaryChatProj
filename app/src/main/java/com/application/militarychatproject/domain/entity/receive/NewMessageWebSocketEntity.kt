package com.application.militarychatproject.domain.entity.receive

import com.application.militarychatproject.data.remote.dto.MessageDTO
import kotlinx.serialization.Serializable

@Serializable
data class NewMessageWebSocketEntity(
    val type: String = "chat",
    val name: String = "message_sent",
    val data: MessageEntity
)
