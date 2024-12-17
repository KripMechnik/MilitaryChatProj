package com.application.militarychatproject.presentation.home.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.HOME_SCREEN_ROUTE
import com.application.militarychatproject.presentation.home.HomeScreenPresenterImpl
import com.application.militarychatproject.presentation.home.HomeScreenViewModel


@ExperimentalMaterial3Api
fun NavGraphBuilder.home(navController: NavController) {

    composable(HOME_SCREEN_ROUTE) {

        val viewModel = hiltViewModel<HomeScreenViewModel>()
        val presenter = HomeScreenPresenterImpl(viewModel, navController)

        HomeScreen(presenter)
    }
}

