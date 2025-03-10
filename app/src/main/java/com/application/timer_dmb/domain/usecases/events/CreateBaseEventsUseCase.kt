package com.application.timer_dmb.domain.usecases.events

import com.application.timer_dmb.data.calendar_database.Event
import com.application.timer_dmb.domain.repository.EventDaoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CreateBaseEventsUseCase @Inject constructor(
    private val dao: EventDaoRepository
) {
    suspend operator fun invoke(dateStart: String, dateEnd: String){
        withContext(Dispatchers.Default){
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            val dateEndAsMillis = LocalDateTime.parse("$dateEnd 00:00:00", formatter).toInstant(
                ZoneOffset.UTC).toEpochMilli()
            val dateStartAsMillis = LocalDateTime.parse("$dateStart 00:00:00", formatter).toInstant(
                ZoneOffset.UTC).toEpochMilli()


            val nextWinter = getDate(Month.DECEMBER, 1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val nextSpring = getDate(Month.MARCH, 1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val nextAutumn = getDate(Month.SEPTEMBER, 1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val nextSummer = getDate(Month.JUNE, 1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val half = dateStartAsMillis + (dateEndAsMillis / 2 - dateStartAsMillis / 2)
            val february23 = getDate(Month.FEBRUARY, 23).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val before100Days = getDateBefore(100, dateEndAsMillis)
            val before3Days = getDateBefore(3, dateEndAsMillis)

            val day9May = getDate(Month.MAY, 9).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val newYear = getDate(Month.DECEMBER, 31).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val christmas = getDate(Month.JANUARY, 7).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val dmb = getDateAfter(1, dateEndAsMillis)

            val events = listOf(
                Event(nextWinter.toString(), "Начало зимы"),
                Event(nextSpring.toString(), "Начало весны"),
                Event(nextAutumn.toString(), "Начало осени"),
                Event(nextSummer.toString(), "Начало лета"),
                Event(half.toString(), "Экватор"),
                Event(before100Days.toString(), "100 дней до дембеля"),
                Event(before3Days.toString(), "3 дня до дембеля"),
                Event(february23.toString(), "День защитника Отечества"),
                Event(day9May.toString(), "День Победы"),
                Event(newYear.toString(), "Новый Год"),
                Event(christmas.toString(), "Рождество Христово"),
                Event(dmb.toString(), "Дембель")
            )
            events.forEach {
                dao.insertEvent(it)
            }
        }

    }

    private fun getDate(month: Month, day: Int): LocalDate {
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year

        val date = LocalDate.of(currentYear, month, day)

        return if (currentDate.isBefore(date)) {
            date
        } else {
            LocalDate.of(currentYear + 1, month, day)
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