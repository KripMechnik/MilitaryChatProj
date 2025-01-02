package com.application.militarychatproject.presentation.home

import android.media.metrics.Event
import com.application.militarychatproject.domain.entity.receive.EventEntity
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface HomeScreenPresenter {
    val state: StateFlow<HomeState>

    val nearestEvent: StateFlow<EventEntity?>

    val eventAchieve: StateFlow<Int>

    fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime)
}