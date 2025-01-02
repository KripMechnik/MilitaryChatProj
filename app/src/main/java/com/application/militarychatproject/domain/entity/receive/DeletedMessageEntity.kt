package com.application.militarychatproject.domain.entity.receive

import kotlinx.serialization.Serializable

@Serializable
data class DeletedMessageEntity(
    val chatId: Int,
    val messageId: Int
)
