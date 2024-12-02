package com.application.militarychatproject.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class UpdatedTimerEntity(
    val startTimeMillis: Long,
    val longTimeMillis: Long
)
