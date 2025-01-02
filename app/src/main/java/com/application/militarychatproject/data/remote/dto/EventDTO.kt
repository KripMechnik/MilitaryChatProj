package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.EventEntity
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class EventDTO(
    val id: String,
    val timeMillis: String,
    val title: String
)

fun EventDTO.toEventEntity(): EventEntity = EventEntity(
    id = this.id,
    timeMillis = this.timeMillis,
    title = this.title
)

