package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.DeletedMessageEntity
import kotlinx.serialization.Serializable

@Serializable
data class DeletedMessageDTO(
    val chatId: Int,
    val messageId: Int
)

fun DeletedMessageDTO.toDeletedMessageEntity() = DeletedMessageEntity(
    chatId = this.chatId,
    messageId = this.messageId
)
