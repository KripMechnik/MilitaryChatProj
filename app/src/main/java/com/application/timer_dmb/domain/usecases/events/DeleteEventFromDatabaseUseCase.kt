package com.application.timer_dmb.domain.usecases.events

import com.application.timer_dmb.data.calendar_database.Event
import com.application.timer_dmb.domain.repository.EventDaoRepository
import javax.inject.Inject

class DeleteEventFromDatabaseUseCase @Inject constructor(
    private val dao: EventDaoRepository
) {
    suspend operator fun invoke(date: String, title: String, id: Int){
        val event = Event(date, title, id)
        dao.deleteEvent(event)
    }
}