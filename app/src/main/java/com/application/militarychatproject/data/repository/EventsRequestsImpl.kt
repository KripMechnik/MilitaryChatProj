package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.EventsRequests
import com.application.militarychatproject.data.remote.dto.EventDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.NewEventEntity
import com.application.militarychatproject.domain.repository.EventsRepository
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import javax.inject.Inject

class EventsRequestsImpl @Inject constructor (
    private val requests: EventsRequests
) : EventsRepository {
    override suspend fun createEvent(newEvent: NewEventEntity): ApiResponse<EventDTO> {
        return requests.createEventRequest(newEvent)
    }

    override suspend fun deleteEvent(id: String): ApiResponse<Any> {
        return requests.deleteEventRequest(id)
    }

    override suspend fun getAllEvents(): ApiResponse<List<EventDTO>> {
        return requests.getAllEventsRequest()
    }

    override suspend fun getEvent(id: String): ApiResponse<EventDTO> {
        return requests.getEventRequest(id)
    }

    override suspend fun updateEvent(id: String): ApiResponse<EventDTO> {
        return requests.updateEventRequest(id)
    }
}