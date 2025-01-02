package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.DeletedMessageWebSocketEntity
import kotlinx.serialization.Serializable
import kotlin.concurrent.thread

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