package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.DeletedMessageEntity
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
