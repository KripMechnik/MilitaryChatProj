package com.application.timer_dmb.domain.entity.receive

import kotlinx.serialization.Serializable

@Serializable
data class EditedMessageEntity(
    val chatId: Int,
    val messageId: Int,
    val text: String
)
