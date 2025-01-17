package com.application.timer_dmb.presentation.registration.registration.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.REGISTRATION_SCREEN_ROUTE
import com.application.timer_dmb.presentation.registration.registration.RegistrationScreenPresenterImpl
import com.application.timer_dmb.presentation.registration.registration.RegistrationScreenViewModel


@ExperimentalMaterial3Api
fun NavGraphBuilder.registration(navController: NavController) {
    composable(REGISTRATION_SCREEN_ROUTE) {

        val viewModel = hiltViewModel<RegistrationScreenViewModel>()

        val presenter = RegistrationScreenPresenterImpl(viewModel, navController)

        RegistrationScreen(presenter)
    }
}