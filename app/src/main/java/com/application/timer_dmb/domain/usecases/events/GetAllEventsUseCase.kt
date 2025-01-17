package com.application.timer_dmb.domain.usecases.events

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.dto.toEventEntity
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.receive.EventEntity
import com.application.timer_dmb.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
){
    operator fun invoke() : Flow<Resource<List<EventEntity>>> = flow {
        emit(Resource.Loading())
        val response = eventsRepository.getAllEvents()
        if (response is ApiResponse.Success) emit(Resource.Success(data = response.data!!.map { it.toEventEntity() }))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }

}