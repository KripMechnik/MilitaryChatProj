package com.application.timer_dmb.domain.repository

import com.application.timer_dmb.data.calendar_database.Event

interface EventDaoRepository {

    suspend fun insertEvent(event: Event)


    suspend fun updateEvent(event: Event)


    suspend fun deleteEvent(event: Event)


    suspend fun getAllEvents(): List<Event>


    suspend fun getCalculatableEvents(): List<Event>

    suspend fun deleteAllEvents()
}