package com.application.militarychatproject.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class NewEventEntity(
    val title: String,
    val timeMillis: Long
)
