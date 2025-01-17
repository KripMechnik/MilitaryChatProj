package com.application.timer_dmb.data.remote

import com.application.timer_dmb.data.remote.dto.EventDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.data.remote.network.AuthRequest
import com.application.timer_dmb.domain.entity.send.NewEventEntity
import io.ktor.http.HttpMethod
import io.ktor.util.StringValues
import javax.inject.Inject

class EventsRequests @Inject constructor(
    private val authRequest: AuthRequest
) {
    private val basePath = "/event/"

    //token -> yes
    suspend fun createEventRequest(
        newEvent: NewEventEntity
    ): ApiResponse<EventDTO> {
        return authRequest(
            path = basePath,
            method = HttpMethod.Post,
            body = newEvent
        )
    }

    //token -> yes
    suspend fun deleteEventRequest(
        id: String
    ) : ApiResponse<Unit> {
        return authRequest(
            path = basePath,
            query = id,
            method = HttpMethod.Delete
        )
    }

    //token -> yes
    suspend fun getAllEventsRequest() : ApiResponse<List<EventDTO>> {
        return authRequest(
            path = basePath,
            method = HttpMethod.Get
        )
    }

    //token -> yes
    suspend fun getEventRequest(
        id: String
    ) : ApiResponse<EventDTO> {
        return authRequest(
            path = basePath,
            params = StringValues.build {
                append("eventId", id)
            },
            method = HttpMethod.Get
        )
    }

    //token -> yes
    suspend fun updateEventRequest(
        id: String,
        newEvent: NewEventEntity
    ) : ApiResponse<EventDTO> {
        return authRequest(
            path = basePath,
            query = id,
            method = HttpMethod.Put,
            body = newEvent
        )
    }
}