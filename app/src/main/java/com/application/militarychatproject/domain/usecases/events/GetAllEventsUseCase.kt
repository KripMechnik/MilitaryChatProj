package com.application.militarychatproject.domain.usecases.events

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.dto.toEventEntity
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.receive.EventEntity
import com.application.militarychatproject.domain.repository.EventsRepository
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