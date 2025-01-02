package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.ReadMessageEntity
import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageDTO(
    val chatId: Int
)

fun ReadMessageDTO.toReadMessageEntity() = ReadMessageEntity(
    chatId = this.chatId
)