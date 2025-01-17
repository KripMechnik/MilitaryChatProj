package com.application.timer_dmb.presentation.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.receive.EventEntity
import com.application.timer_dmb.domain.entity.send.NewEventEntity
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.timer_dmb.domain.usecases.events.CreateEventUseCase
import com.application.timer_dmb.domain.usecases.events.DeleteAllEventsFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.DeleteEventFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.DeleteEventUseCase
import com.application.timer_dmb.domain.usecases.events.GetAllEventsFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.GetAllEventsUseCase
import com.application.timer_dmb.domain.usecases.events.InsertEventIntoDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.UpdateEventFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.UpdateEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
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
    private val getAllEventsFromDatabaseUseCase: GetAllEventsFromDatabaseUseCase,
    private val deleteEventFromDatabaseUseCase: DeleteEventFromDatabaseUseCase,
    private val insertEventIntoDatabaseUseCase: InsertEventIntoDatabaseUseCase,
    private val updateEventFromDatabaseUseCase: UpdateEventFromDatabaseUseCase,
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val getAllEventsUseCase: GetAllEventsUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val deleteAllEventsFromDatabaseUseCase: DeleteAllEventsFromDatabaseUseCase,
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

    private val _insertEventState = MutableStateFlow<InsertEventState?>(null)
    val insertEventState = _insertEventState.asStateFlow()

    private val _updateEventState = MutableStateFlow<UpdateEventState?>(null)
    val updateEventState = _updateEventState.asStateFlow()

    private val _deleteEventState = MutableStateFlow<DeleteEventState?>(null)
    val deleteEventState = _deleteEventState.asStateFlow()


    init {
        getEvents()
    }

    private fun getEvents(){
        if (!isAuthorizedUseCase()){
            viewModelScope.launch {
                getBaseEvents()
            }
        } else {
            viewModelScope.launch {
                deleteAllEventsFromDatabaseUseCase()
            }

            getAllEvents()
        }
    }

    fun deleteEvent(date: String, title: String, id: String){
        if (!isAuthorizedUseCase()) {


            viewModelScope.launch {
                val delete = launch {
                    deleteEventFromDatabaseUseCase(date, title, id.toInt())
                }
                delete.join()
                val get = launch {
                    getBaseEvents()
                }
                get.join()

                calendarState.selectionState.onDateSelected(convertMillisToLocalDate(date.toLong()))
            }
        } else {
            deleteEventUseCase(id).onEach {result ->
                when(result){
                    is Resource.Error -> {
                        _deleteEventState.value = DeleteEventState.Error(
                            message = result.message ?: "Unknown error",
                            code = result.code
                        )
                        Log.e("delete_e", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> _deleteEventState.value = DeleteEventState.Loading()
                    is Resource.Success -> {
                        calendarState.selectionState.onDateSelected(convertMillisToLocalDate(date.toLong()))
                        _deleteEventState.value = DeleteEventState.Success()
                        getAllEvents()
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun updateEvent(oldDate: String, newDate: String, newTitle: String, id: String){
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val dateAsMillis = LocalDateTime.parse("$newDate 00:00:00", formatter).toInstant(
            ZoneOffset.UTC
        ).toEpochMilli()
        if (!isAuthorizedUseCase()) {
            viewModelScope.launch {

                val update = launch {
                    updateEventFromDatabaseUseCase(dateAsMillis.toString(), newTitle, id.toInt())
                }
                update.join()
                val get = launch {
                    getBaseEvents()
                }
                get.join()

                if (oldDate.toLong() != dateAsMillis) {
                    calendarState.selectionState.onDateSelected(convertMillisToLocalDate(oldDate.toLong()))
                }

            }
        } else {
            val newEvent = NewEventEntity(newTitle, dateAsMillis)
            updateEventUseCase(id.toString(), newEvent).onEach {result ->
                when(result){
                    is Resource.Error -> {
                        _updateEventState.value = UpdateEventState.Error(
                            message = result.message ?: "Unknown error",
                            code = result.code
                        )
                        Log.e("update_e", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> _updateEventState.value = UpdateEventState.Loading()
                    is Resource.Success -> {
                        if (oldDate.toLong() != dateAsMillis) {
                            calendarState.selectionState.onDateSelected(convertMillisToLocalDate(oldDate.toLong()))
                        }
                        _updateEventState.value = UpdateEventState.Success()
                        getAllEvents()
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun insertEvent(date: String, title: String){
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val dateAsMillis = LocalDateTime.parse("$date 00:00:00", formatter).toInstant(
            ZoneOffset.UTC
        ).toEpochMilli()
        if (!isAuthorizedUseCase()) {


            viewModelScope.launch {
                val save = launch {


                    insertEventIntoDatabaseUseCase(dateAsMillis.toString(), title)
                }
                save.join()
                val get = launch {
                    getBaseEvents()
                }
                get.join()
            }
        } else {
            val newEvent = NewEventEntity(title, dateAsMillis)
            createEventUseCase(newEvent).onEach {result ->
                when(result){
                    is Resource.Error -> {
                        _insertEventState.value = InsertEventState.Error(
                            message = result.message ?: "Unknown error",
                            code = result.code
                        )
                        Log.e("insert_e", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> _insertEventState.value = InsertEventState.Loading()
                    is Resource.Success -> {
                        _insertEventState.value = InsertEventState.Success()
                        getAllEvents()
                    }
                }

            }.launchIn(viewModelScope)
        }


    }

    private suspend fun getBaseEvents(){
        val list = getAllEventsFromDatabaseUseCase()

        markEvents(list)

        _allEvents.update {
            list
        }

    }

    private fun markEvents(list: List<EventEntity>){
        list.forEach {
            if (!calendarState.selectionState.isDateSelected(convertMillisToLocalDate(it.timeMillis.toLong()))){
                calendarState.selectionState.onDateSelected(convertMillisToLocalDate(it.timeMillis.toLong()))
            }
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
                    markEvents(result.data)
                    _allEvents.update {
                        result.data.sortedBy { it.timeMillis }
                    }
                    Log.e("calendar", result.data.toString())
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun convertMillisToLocalDate(millis: Long): LocalDate {
        val instant = Instant.ofEpochMilli(millis)
        return instant.atZone(ZoneId.systemDefault()).toLocalDate()
    }

}

sealed class AllEventsState(val data: List<EventEntity>? = null, val message: String? = null, val code: Int? = null){
    class Success(data: List<EventEntity>) : AllEventsState(data = data)
    class Error(message: String, code: Int?) : AllEventsState(message = message, code = code)
    class Loading : AllEventsState()
}

sealed class InsertEventState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success : InsertEventState()
    class Error(message: String, code: Int?) : InsertEventState(message = message, code = code)
    class Loading : InsertEventState()
}

sealed class UpdateEventState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success : UpdateEventState()
    class Error(message: String, code: Int?) : UpdateEventState(message = message, code = code)
    class Loading : UpdateEventState()
}

sealed class DeleteEventState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success : DeleteEventState()
    class Error(message: String, code: Int?) : DeleteEventState(message = message, code = code)
    class Loading : DeleteEventState()
}