package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.ReadMessageWebSocketEntity
import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageWebSocketDTO (
    val type: String = "chat",
    val name: String = "all_messages_read",
    val data: ReadMessageDTO
)

fun ReadMessageWebSocketDTO.toReadMessageWebSocketEntity() = ReadMessageWebSocketEntity(
    type = this.type,
    name = this.name,
    data = this.data.toReadMessageEntity()
)