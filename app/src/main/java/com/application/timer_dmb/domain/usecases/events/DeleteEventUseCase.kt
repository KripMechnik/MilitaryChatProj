package com.application.timer_dmb.domain.usecases.events

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {
    operator fun invoke(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = eventsRepository.deleteEvent(id)
        if (response is ApiResponse.Success)
            emit(Resource.Success(data = Unit))
        else
            emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}