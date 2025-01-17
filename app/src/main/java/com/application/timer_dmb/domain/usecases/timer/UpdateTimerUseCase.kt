package com.application.timer_dmb.domain.usecases.timer

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.UpdatedTimerEntity
import com.application.timer_dmb.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class UpdateTimerUseCase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(updatedTimerEntity: UpdatedTimerEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = timerRepository.updateTimer(updatedTimerEntity)
        if (response is ApiResponse.Success)
            emit(Resource.Success(data = Unit))
        else
            emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}