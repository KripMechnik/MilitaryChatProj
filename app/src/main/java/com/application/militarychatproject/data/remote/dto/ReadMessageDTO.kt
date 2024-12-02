package com.application.militarychatproject.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageDTO(
    val chatId: Int
)
