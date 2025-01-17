package com.application.timer_dmb.presentation.home.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableIntState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.HOME_SCREEN_ROUTE
import com.application.timer_dmb.presentation.home.HomeScreenPresenterImpl
import com.application.timer_dmb.presentation.home.HomeScreenViewModel


@ExperimentalMaterial3Api
fun NavGraphBuilder.home(navController: NavController, selectedItemIndex: MutableIntState, innerPadding: PaddingValues) {

    composable(HOME_SCREEN_ROUTE) {

        val viewModel = hiltViewModel<HomeScreenViewModel>()
        val presenter = HomeScreenPresenterImpl(viewModel, navController, selectedItemIndex)

        HomeScreen(presenter, innerPadding)
    }
}

