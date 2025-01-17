package com.application.timer_dmb.presentation.registration.add_soldier.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.ADD_SOLDIER_SCREEN_ROUTE
import com.application.timer_dmb.presentation.registration.add_soldier.AddSoldierScreenPresenterImpl
import com.application.timer_dmb.presentation.registration.add_soldier.AddSoldierScreenViewModel

fun NavGraphBuilder.addSoldier(navController: NavController) {
    composable(ADD_SOLDIER_SCREEN_ROUTE) {

        val viewModel = hiltViewModel<AddSoldierScreenViewModel>()


        val presenter = AddSoldierScreenPresenterImpl(
            viewModel = viewModel,
            navController = navController
        )

        AddSoldierScreen(presenter)
    }
}