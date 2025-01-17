package com.application.timer_dmb.data.repository

import com.application.timer_dmb.data.remote.EventsRequests
import com.application.timer_dmb.data.remote.dto.EventDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.NewEventEntity
import com.application.timer_dmb.domain.repository.EventsRepository
import javax.inject.Inject

class EventsRepoImpl @Inject constructor (
    private val requests: EventsRequests
) : EventsRepository {
    override suspend fun createEvent(newEvent: NewEventEntity): ApiResponse<EventDTO> {
        return requests.createEventRequest(newEvent)
    }

    override suspend fun deleteEvent(id: String): ApiResponse<Unit> {
        return requests.deleteEventRequest(id)
    }

    override suspend fun getAllEvents(): ApiResponse<List<EventDTO>> {
        return requests.getAllEventsRequest()
    }

    override suspend fun getEvent(id: String): ApiResponse<EventDTO> {
        return requests.getEventRequest(id)
    }

    override suspend fun updateEvent(id: String, newEventEntity: NewEventEntity): ApiResponse<EventDTO> {
        return requests.updateEventRequest(id, newEventEntity)
    }
}