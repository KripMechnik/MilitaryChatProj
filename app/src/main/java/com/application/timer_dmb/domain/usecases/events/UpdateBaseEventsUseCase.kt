package com.application.timer_dmb.domain.usecases.events

import com.application.timer_dmb.domain.repository.EventDaoRepository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UpdateBaseEventsUseCase @Inject constructor(
    private val dao: EventDaoRepository
) {

    suspend operator fun invoke(dateStart: String, dateEnd: String){
        val calculatableEvents = dao.getCalculatableEvents()

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val dateEndAsMillis = LocalDateTime.parse("$dateEnd 00:00:00", formatter).toInstant(
            ZoneOffset.UTC).toEpochMilli()
        val dateStartAsMillis = LocalDateTime.parse("$dateStart 00:00:00", formatter).toInstant(
            ZoneOffset.UTC).toEpochMilli()

        val half = dateStartAsMillis + (dateEndAsMillis / 2 - dateStartAsMillis / 2)

        val before100Days = getDateBefore(100, dateEndAsMillis)
        val before3Days = getDateBefore(3, dateEndAsMillis)

        val dmb = getDateAfter(1, dateEndAsMillis)

        val times = listOf(
            half,
            before100Days,
            before3Days,
            dmb
        )

        calculatableEvents.forEachIndexed{index, event->
            event.timeMillis = times[index].toString()
        }

        calculatableEvents.forEach {
            dao.updateEvent(it)
        }
    }

    private fun getDateAfter(days: Int, dateInMillis: Long): Long{
        val millisInADay = 86_400_000L
        return dateInMillis + (days * millisInADay)
    }

    private fun getDateBefore(days: Int, dateInMillis: Long): Long{
        val millisInADay = 86_400_000L
        return dateInMillis - (days * millisInADay)
    }
}