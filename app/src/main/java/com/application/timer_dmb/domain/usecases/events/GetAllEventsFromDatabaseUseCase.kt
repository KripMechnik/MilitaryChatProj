package com.application.timer_dmb.domain.usecases.events

import com.application.timer_dmb.data.calendar_database.toEventEntity
import com.application.timer_dmb.domain.entity.receive.EventEntity
import com.application.timer_dmb.domain.repository.EventDaoRepository
import javax.inject.Inject

class GetAllEventsFromDatabaseUseCase @Inject constructor(
    private val dao: EventDaoRepository
) {
    suspend operator fun invoke() : List<EventEntity>{
        return dao.getAllEvents().map { it.toEventEntity() }
    }
}