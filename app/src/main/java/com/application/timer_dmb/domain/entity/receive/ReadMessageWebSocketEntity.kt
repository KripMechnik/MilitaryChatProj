package com.application.timer_dmb.domain.entity.receive

import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageWebSocketEntity(
    val type: String = "chat",
    val name: String = "all_messages_read",
    val data: ReadMessageEntity
)
