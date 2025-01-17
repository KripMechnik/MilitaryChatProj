package com.application.timer_dmb.domain.entity.receive

import kotlinx.serialization.Serializable

@Serializable
data class EditedMessageWebSocketEntity(
    val type: String = "chat",
    val name: String = "message_edited",
    val data: EditedMessageEntity
)
