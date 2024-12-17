package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.data.remote.dto.EventDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.NewEventEntity

interface EventsRepository {

    suspend fun createEvent(newEvent: NewEventEntity): ApiResponse<EventDTO>

    suspend fun deleteEvent(id: String): ApiResponse<Unit>

    suspend fun getAllEvents(): ApiResponse<List<EventDTO>>

    suspend fun getEvent(id: String): ApiResponse<EventDTO>

    suspend fun updateEvent(id: String): ApiResponse<EventDTO>
}