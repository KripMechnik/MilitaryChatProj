package com.application.militarychatproject.domain.entity.receive

import com.application.militarychatproject.data.remote.dto.EditedMessageDTO
import kotlinx.serialization.Serializable

@Serializable
data class EditedMessageWebSocketEntity(
    val type: String = "chat",
    val name: String = "message_edited",
    val data: EditedMessageEntity
)
