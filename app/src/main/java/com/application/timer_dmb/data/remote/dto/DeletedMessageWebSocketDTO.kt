package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.DeletedMessageWebSocketEntity
import kotlinx.serialization.Serializable

@Serializable
data class DeletedMessageWebSocketDTO(
    val type: String = "chat",
    val name: String = "message_deleted",
    val data: DeletedMessageDTO
)

fun DeletedMessageWebSocketDTO.toDeletedMessageWebSocketEntity() = DeletedMessageWebSocketEntity(
    type = this.type,
    name = this.name,
    data = this.data.toDeletedMessageEntity()
)