package com.application.timer_dmb.presentation.registration.add_soldier

import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.ADD_SOLDIER_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.HOME_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class AddSoldierScreenPresenterImpl(
    private val navController: NavController,
    private val viewModel: AddSoldierScreenViewModel
) : AddSoldierScreenPresenter {

    override val addedEvents: StateFlow<Boolean>
        get() = viewModel.addedEvents

    override fun navigateToHome() {
        navController.navigate(HOME_SCREEN_ROUTE){
            popUpTo(ADD_SOLDIER_SCREEN_ROUTE){
                inclusive = true
            }
        }
    }

    override fun saveData(dateStart: String, dateEnd: String, name: String) {
        viewModel.saveData(dateStart, dateEnd, name)
    }
}