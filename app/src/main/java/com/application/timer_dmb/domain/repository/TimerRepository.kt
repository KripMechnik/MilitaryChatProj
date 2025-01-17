package com.application.timer_dmb.domain.repository

import com.application.timer_dmb.data.remote.dto.TimerDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.UpdatedTimerEntity

interface TimerRepository {

    suspend fun getTimerData(): ApiResponse<TimerDTO>

    suspend fun synchronizeTimer(timerId: String): ApiResponse<TimerDTO>

    suspend fun updateTimer(updatedTimer: UpdatedTimerEntity): ApiResponse<Unit>

}