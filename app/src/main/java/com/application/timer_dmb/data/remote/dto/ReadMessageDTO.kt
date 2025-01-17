package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.ReadMessageEntity
import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageDTO(
    val chatId: Int
)

fun ReadMessageDTO.toReadMessageEntity() = ReadMessageEntity(
    chatId = this.chatId
)