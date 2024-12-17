package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.TimerRequests
import com.application.militarychatproject.data.remote.dto.TimerDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.UpdatedTimerEntity
import com.application.militarychatproject.domain.repository.TimerRepository
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

    override suspend fun updateTimer(updatedTimer: UpdatedTimerEntity): ApiResponse<TimerDTO> {
        return timerRequests.updateTimerRequest(updatedTimer)
    }
}