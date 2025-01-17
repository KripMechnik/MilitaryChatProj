package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.EventEntity
import kotlinx.serialization.Serializable

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

