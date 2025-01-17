package com.application.timer_dmb.presentation.home

import com.application.timer_dmb.domain.entity.receive.EventEntity
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface HomeScreenPresenter {
    val state: StateFlow<HomeState>

    val nearestEvent: StateFlow<EventEntity?>

    val eventAchieve: StateFlow<Int>

    val background: StateFlow<BackgroundState?>

    fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime)

    fun setAllData()

    fun getImageFromCache()

    fun navigateToCalendar()

    fun isAuthorized(): Boolean

    fun navigateToSharePicture()
}