package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.EditedMessageEntity
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
