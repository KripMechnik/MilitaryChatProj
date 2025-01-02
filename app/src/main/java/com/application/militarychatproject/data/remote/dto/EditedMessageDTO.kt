package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.EditedMessageEntity
import kotlinx.serialization.Serializable

@Serializable
data class EditedMessageDTO(
    val chatId: Int,
    val messageId: Int,
    val text: String
)

fun EditedMessageDTO.toEditedMessageEntity() = EditedMessageEntity(
    chatId = this.chatId,
    messageId = this.messageId,
    text = this.text
)
