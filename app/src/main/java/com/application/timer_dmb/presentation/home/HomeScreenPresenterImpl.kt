package com.application.timer_dmb.presentation.home

import androidx.compose.runtime.MutableIntState
import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.CALENDAR_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.HOME_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.SHARE_PICTURE_SCREEN_ROUTE
import com.application.timer_dmb.domain.entity.receive.EventEntity
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

class HomeScreenPresenterImpl(
    private val viewModel: HomeScreenViewModel,
    private val navController: NavController,
    private val selectedItemIndex: MutableIntState
) : HomeScreenPresenter {
    override val state: StateFlow<HomeState>
        get() = viewModel.state
    override val nearestEvent: StateFlow<EventEntity?>
        get() = viewModel.nearestEvent
    override val eventAchieve: StateFlow<Int>
        get() = viewModel.eventAchieve
    override val background: StateFlow<BackgroundState?>
        get() = viewModel.background

    override fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime) {
        viewModel.countDate(dateEnd, dateStart)
    }

    override fun setAllData() {
        viewModel.setAllData()
    }

    override fun getImageFromCache() {
        viewModel.getImageFromCache()
    }

    override fun navigateToCalendar() {
        navController.navigate(CALENDAR_SCREEN_ROUTE){
            popUpTo(HOME_SCREEN_ROUTE){
                selectedItemIndex.intValue = 2
                inclusive = true
            }
        }
    }

    override fun isAuthorized(): Boolean {
        return viewModel.isAuthorized()
    }

    override fun navigateToSharePicture() {
        navController.navigate(SHARE_PICTURE_SCREEN_ROUTE)
    }
}