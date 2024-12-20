package com.application.militarychatproject.presentation.registration.add_soldier.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.ADD_SOLDIER_SCREEN_ROUTE
import com.application.militarychatproject.presentation.registration.add_soldier.AddSoldierScreenPresenterImpl
import com.application.militarychatproject.presentation.registration.add_soldier.AddSoldierScreenViewModel

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