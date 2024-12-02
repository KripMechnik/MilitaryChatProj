package com.application.militarychatproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeletedMessageWebSocketDTO(
    val type: String = "chat",
    val name: String = "message_deleted",
    val data: DeletedMessageDTO
)
