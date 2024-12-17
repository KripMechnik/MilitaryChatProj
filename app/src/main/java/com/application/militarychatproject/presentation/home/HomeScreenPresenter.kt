package com.application.militarychatproject.presentation.home

import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface HomeScreenPresenter {
    val state: StateFlow<HomeState>

    fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime)
}