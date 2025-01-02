package com.application.militarychatproject.presentation.home

import androidx.navigation.NavController
import com.application.militarychatproject.domain.entity.receive.EventEntity
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

class HomeScreenPresenterImpl(
    private val viewModel: HomeScreenViewModel,
    private val navController: NavController
) : HomeScreenPresenter {
    override val state: StateFlow<HomeState>
        get() = viewModel.state
    override val nearestEvent: StateFlow<EventEntity?>
        get() = viewModel.nearestEvent
    override val eventAchieve: StateFlow<Int>
        get() = viewModel.eventAchieve

    override fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime) {
        viewModel.countDate(dateEnd, dateStart)
    }
}