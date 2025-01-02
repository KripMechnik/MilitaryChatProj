package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.NewMessageWebSocketEntity
import kotlinx.serialization.Serializable

@Serializable
data class NewMessageWebSocketDTO (
    val type: String = "chat",
    val name: String = "message_sent",
    val data: MessageDTO
)

fun NewMessageWebSocketDTO.toNewMessageWebSocketEntity() = NewMessageWebSocketEntity(
    type = this.type,
    name = this.name,
    data = this.data.toMessageEntity()
)

