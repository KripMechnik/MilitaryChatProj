package com.application.timer_dmb.presentation.calendar

import androidx.navigation.NavController
import com.application.timer_dmb.domain.entity.receive.EventEntity
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import kotlinx.coroutines.flow.StateFlow

class CalendarScreenPresenterImpl(
    private val viewModel: CalendarScreenViewModel,
    private val navController: NavController
) : CalendarScreenPresenter{
    override val allEventsState: StateFlow<AllEventsState?>
        get() = viewModel.allEventsState
    override val calendarState: CalendarState<DynamicSelectionState>
        get() = viewModel.calendarState
    override val allEvents: StateFlow<List<EventEntity>>
        get() = viewModel.allEvents

    override fun navigateUp() {
        navController.popBackStack()
    }

    override fun insertEvent(title: String, date: String) {
        viewModel.insertEvent(date = date, title = title)
    }

    override fun deleteEvent(title: String, date: String, id: String) {
        viewModel.deleteEvent(date, title, id)
    }

    override fun updateEvent(oldDate: String, newDate: String, newTitle: String, id: String) {
        viewModel.updateEvent(oldDate, newDate, newTitle, id)
    }
}