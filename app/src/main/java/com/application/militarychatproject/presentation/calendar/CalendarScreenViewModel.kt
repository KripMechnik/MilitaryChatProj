package com.application.militarychatproject.presentation.calendar

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.calendar_database.Event
import com.application.militarychatproject.data.calendar_database.EventDao
import com.application.militarychatproject.data.calendar_database.toEventEntity
import com.application.militarychatproject.domain.entity.receive.EventEntity
import com.application.militarychatproject.domain.usecases.events.GetAllEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CalendarScreenViewModel @Inject constructor(
    private val dao: EventDao,
    private val getAllEventsUseCase: GetAllEventsUseCase
) : ViewModel() {

    val calendarState = CalendarState(
        MonthState(
            initialMonth = YearMonth.now(),
            minMonth = YearMonth.now().minusMonths(10_000L),
            maxMonth = YearMonth.now().plusMonths(10_000L),
        ),
        DynamicSelectionState({ true }, emptyList(), SelectionMode.Multiple)
    )

    private var _allEvents = MutableStateFlow<List<EventEntity>>(emptyList())
    var allEvents = _allEvents.asStateFlow()

    private val _allEventsState = MutableStateFlow<AllEventsState?>(null)
    val allEventsState = _allEventsState.asStateFlow()

    init {
        //getAllEvents()
        viewModelScope.launch {
            getBaseEvents()
        }
    }

    fun deleteEvent(date: String, title: String, id: Int){
        viewModelScope.launch {
            val delete = launch {

                val event = Event(date, title, id)
                dao.deleteEvent(event)
            }
            delete.join()
            val get = launch {
                getBaseEvents()
            }
            get.join()

            calendarState.selectionState.onDateSelected(convertMillisToLocalDate(date.toLong()))
        }
    }

    fun updateEvent(oldDate: String, newDate: String, newTitle: String, id: Int){
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            val dateAsMillis = LocalDateTime.parse("$newDate 00:00:00", formatter).toInstant(
                ZoneOffset.UTC).toEpochMilli()
            val update = launch {
                val event = Event(dateAsMillis.toString(), newTitle, id)
                dao.updateEvent(event)
            }
            update.join()
            val get = launch {
                getBaseEvents()
            }
            get.join()

            if (oldDate.toLong() != dateAsMillis){
                calendarState.selectionState.onDateSelected(convertMillisToLocalDate(oldDate.toLong()))
            }

        }
    }

    fun insertEvent(date: String, title: String){
        viewModelScope.launch {
            val save = launch {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                val dateAsMillis = LocalDateTime.parse("$date 00:00:00", formatter).toInstant(
                    ZoneOffset.UTC).toEpochMilli()
                val event = Event(dateAsMillis.toString(), title)
                dao.insertEvent(event)
            }
            save.join()
            val get = launch {
                getBaseEvents()
            }
            get.join()
        }


    }

    private suspend fun getBaseEvents(){
        val list = dao.getAllEvents().map { it.toEventEntity() }

        list.forEach {
            if (!calendarState.selectionState.isDateSelected(convertMillisToLocalDate(it.timeMillis.toLong()))){
                calendarState.selectionState.onDateSelected(convertMillisToLocalDate(it.timeMillis.toLong()))
            }
        }

        _allEvents.update {
            list
        }

    }

    private fun getAllEvents(){
        getAllEventsUseCase().onEach { result->
            when (result) {
                is Resource.Error -> {
                    _allEventsState.value = AllEventsState.Error(message = result.message ?: "Unknown error", code = result.code)
                    Log.e("calendar", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _allEventsState.value = AllEventsState.Loading()
                is Resource.Success -> {
                    _allEventsState.value = AllEventsState.Success(data = result.data!!)
                    Log.e("calendar", result.data.toString())
                }
            }
        }
    }

    private fun convertMillisToLocalDate(millis: Long): LocalDate {
        val instant = Instant.ofEpochMilli(millis)
        return instant.atZone(ZoneId.systemDefault()).toLocalDate()
    }

}

sealed class AllEventsState(val data: List<EventEntity>? = null, val message: String? = null, code: Int? = null){
    class Success(data: List<EventEntity>) : AllEventsState(data = data)
    class Error(message: String, code: Int?) : AllEventsState(message = message, code = code)
    class Loading : AllEventsState()
}