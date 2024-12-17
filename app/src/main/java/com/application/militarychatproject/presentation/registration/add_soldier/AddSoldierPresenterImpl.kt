package com.application.militarychatproject.presentation.registration.add_soldier

import androidx.navigation.NavController
import com.application.militarychatproject.common.Constants.ADD_SOLDIER_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.HOME_SCREEN_ROUTE

class AddSoldierPresenterImpl(
    private val navController: NavController,
    private val viewModel: AddSoldierViewModel
) : AddSoldierPresenter {

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