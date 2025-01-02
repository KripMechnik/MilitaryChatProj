package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.EditedMessageWebSocketEntity
import kotlinx.serialization.Serializable

@Serializable
data class EditedMessageWebSocketDTO (
    val type: String = "chat",
    val name: String = "message_edited",
    val data: EditedMessageDTO
)

fun EditedMessageWebSocketDTO.toEditedMessageWebSocketEntity() = EditedMessageWebSocketEntity (
    type = this.type,
    name = this.name,
    data = this.data.toEditedMessageEntity()
)