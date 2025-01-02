package com.application.militarychatproject.domain.entity.receive

import com.application.militarychatproject.data.remote.dto.ReadMessageDTO
import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageWebSocketEntity(
    val type: String = "chat",
    val name: String = "all_messages_read",
    val data: ReadMessageEntity
)
