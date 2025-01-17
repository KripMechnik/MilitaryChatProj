package com.application.timer_dmb.presentation.choose_type

import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.HOME_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.MENU_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class TypeScreenPresenterImpl(
    private val viewModel: TypeScreenViewModel,
    private val navController: NavController
) : TypeScreenPresenter{

    override val setTypeState: StateFlow<SetTypeState?>
        get() = viewModel.setTypeState

    override fun setType(type: String) {
        viewModel.setType(type)
    }

    override fun navigateToHome() {
        navController.navigate(HOME_SCREEN_ROUTE){
            popUpTo(MENU_SCREEN_ROUTE)
        }
    }


}