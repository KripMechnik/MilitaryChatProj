package com.application.timer_dmb.domain.usecases.events

import com.application.timer_dmb.data.calendar_database.Event
import com.application.timer_dmb.domain.repository.EventDaoRepository
import javax.inject.Inject

class UpdateEventFromDatabaseUseCase @Inject constructor(
    private val dao: EventDaoRepository
) {
    suspend operator fun invoke(newDate: String, newTitle: String, id: Int){
        val event = Event(newDate, newTitle, id)
        dao.updateEvent(event)
    }
}