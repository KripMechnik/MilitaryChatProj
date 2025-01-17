package com.application.timer_dmb.data.repository

import com.application.timer_dmb.data.remote.TimerRequests
import com.application.timer_dmb.data.remote.dto.TimerDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.UpdatedTimerEntity
import com.application.timer_dmb.domain.repository.TimerRepository
import javax.inject.Inject

class TimerRepoImpl @Inject constructor (
    private val timerRequests: TimerRequests
) : TimerRepository {
    override suspend fun getTimerData(): ApiResponse<TimerDTO> {
        return timerRequests.getTimerDataRequest()
    }

    override suspend fun synchronizeTimer(timerId: String): ApiResponse<TimerDTO> {
        return timerRequests.synchronizeTimerRequest(timerId)
    }

    override suspend fun updateTimer(updatedTimer: UpdatedTimerEntity): ApiResponse<Unit> {
        return timerRequests.updateTimerRequest(updatedTimer)
    }
}