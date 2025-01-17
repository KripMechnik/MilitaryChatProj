package com.application.timer_dmb.domain.usecases.events

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.NewEventEntity
import com.application.timer_dmb.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateEventUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {
    operator fun invoke(id: String, newEventEntity: NewEventEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = eventsRepository.updateEvent(id, newEventEntity)
        if (response is ApiResponse.Success)
            emit(Resource.Success(data = Unit))
        else
            emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}