package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.TimerEntity
import kotlinx.serialization.Serializable

@Serializable
data class TimerDTO(
    val endTimeMillis: Long,
    val startTimeMillis: Long
)

fun TimerDTO.toTimerEntity() = TimerEntity(
    endTime = this.endTimeMillis,
    startTime = this.startTimeMillis
)