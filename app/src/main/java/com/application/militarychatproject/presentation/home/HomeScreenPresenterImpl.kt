package com.application.militarychatproject.presentation.home

import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

class HomeScreenPresenterImpl(
    private val viewModel: HomeScreenViewModel,
    private val navController: NavController
) : HomeScreenPresenter {
    override val state: StateFlow<HomeState>
        get() = viewModel.state

    override fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime) {
        viewModel.countDate(dateEnd, dateStart)
    }
}