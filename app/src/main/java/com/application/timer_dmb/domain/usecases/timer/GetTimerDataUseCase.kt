package com.application.timer_dmb.domain.usecases.timer

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.dto.toTimerEntity
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.receive.TimerEntity
import com.application.timer_dmb.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTimerDataUseCase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    operator fun invoke(): Flow<Resource<TimerEntity>> = flow {
        emit(Resource.Loading())
        val response = timerRepository.getTimerData()
        if (response is ApiResponse.Success)
            emit(Resource.Success(response.data!!.toTimerEntity()))
        else
            emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}