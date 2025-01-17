package com.application.timer_dmb.domain.repository

import com.application.timer_dmb.data.remote.dto.EventDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.NewEventEntity

interface EventsRepository {

    suspend fun createEvent(newEvent: NewEventEntity): ApiResponse<EventDTO>

    suspend fun deleteEvent(id: String): ApiResponse<Unit>

    suspend fun getAllEvents(): ApiResponse<List<EventDTO>>

    suspend fun getEvent(id: String): ApiResponse<EventDTO>

    suspend fun updateEvent(id: String, newEventEntity: NewEventEntity): ApiResponse<EventDTO>
}