package com.application.timer_dmb.presentation.menu.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.MENU_SCREEN_ROUTE
import com.application.timer_dmb.presentation.menu.MenuScreenPresenterImpl
import com.application.timer_dmb.presentation.menu.MenuScreenViewModel

fun NavGraphBuilder.menu(navController: NavController){

    composable(MENU_SCREEN_ROUTE) {

        val viewModel = hiltViewModel<MenuScreenViewModel>()

        val presenter = MenuScreenPresenterImpl(viewModel, navController)

        MenuScreen(presenter)
    }
}