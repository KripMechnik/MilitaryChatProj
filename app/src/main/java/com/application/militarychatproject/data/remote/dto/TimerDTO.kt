package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.TimerEntity
import kotlinx.serialization.Serializable

@Serializable
data class TimerDTO(
    val endTime: String,
    val id: String,
    val startTime: String
)

fun TimerDTO.toTimerEntity() = TimerEntity(
    endTime = this.endTime,
    id = this.id,
    startTime = this.startTime
)