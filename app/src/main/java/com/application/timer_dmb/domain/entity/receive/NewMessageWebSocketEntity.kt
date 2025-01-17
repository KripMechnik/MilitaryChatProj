package com.application.timer_dmb.domain.entity.receive

import kotlinx.serialization.Serializable

@Serializable
data class NewMessageWebSocketEntity(
    val type: String = "chat",
    val name: String = "message_sent",
    val data: MessageEntity
)
