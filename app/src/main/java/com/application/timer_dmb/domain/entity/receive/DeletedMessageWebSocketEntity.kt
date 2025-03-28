package com.application.timer_dmb.domain.entity.receive

import kotlinx.serialization.Serializable

@Serializable
data class DeletedMessageWebSocketEntity(
    val type: String = "chat",
    val name: String = "message_deleted",
    val data: DeletedMessageEntity
)
