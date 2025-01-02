package com.application.militarychatproject.presentation.calendar

import com.application.militarychatproject.domain.entity.receive.EventEntity
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import kotlinx.coroutines.flow.StateFlow

interface CalendarScreenPresenter {
    val allEventsState: StateFlow<AllEventsState?>

    val calendarState: CalendarState<DynamicSelectionState>

    val allEvents: StateFlow<List<EventEntity>>

    fun navigateUp()

    fun insertEvent(title: String, date: String)

    fun deleteEvent(title: String, date: String, id: Int)

    fun updateEvent(oldDate: String, newDate: String, newTitle: String, id: Int)
}