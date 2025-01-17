package com.application.timer_dmb.domain.usecases.events

import com.application.timer_dmb.domain.repository.EventDaoRepository
import javax.inject.Inject

class DeleteAllEventsFromDatabaseUseCase @Inject constructor(
    private val dao: EventDaoRepository
) {
    suspend operator fun invoke(){
        dao.deleteAllEvents()
    }
}