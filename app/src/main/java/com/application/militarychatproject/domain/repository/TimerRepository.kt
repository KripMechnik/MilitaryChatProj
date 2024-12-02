package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.data.remote.dto.TimerDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.UpdatedTimerEntity

interface TimerRepository {

    suspend fun getTimerData(): ApiResponse<TimerDTO>

    suspend fun synchronizeTimer(timerId: String): ApiResponse<TimerDTO>

    suspend fun updateTimer(updatedTimer: UpdatedTimerEntity): ApiResponse<TimerDTO>

}