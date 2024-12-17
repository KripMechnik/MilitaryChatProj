package com.application.militarychatproject.data.remote

import com.application.militarychatproject.data.remote.dto.EventDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.data.remote.network.AuthRequest
import com.application.militarychatproject.data.remote.network.BaseRequest
import com.application.militarychatproject.domain.entity.send.NewEventEntity
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
            params = StringValues.build {
                append("eventId", id)
            },
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
        id: String
    ) : ApiResponse<EventDTO> {
        return authRequest(
            path = basePath,
            params = StringValues.build {
                append("eventId", id)
            },
            method = HttpMethod.Put
        )
    }
}