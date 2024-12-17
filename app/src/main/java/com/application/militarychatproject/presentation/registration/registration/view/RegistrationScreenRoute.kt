package com.application.militarychatproject.presentation.registration.registration.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.REGISTRATION_SCREEN_ROUTE
import com.application.militarychatproject.presentation.registration.registration.RegistrationScreenPresenterImpl
import com.application.militarychatproject.presentation.registration.registration.RegistrationScreenViewModel
import com.application.militarychatproject.presentation.reset_password.reset.ResetScreenPresenterImpl


@ExperimentalMaterial3Api
fun NavGraphBuilder.registration(navController: NavController) {
    composable(REGISTRATION_SCREEN_ROUTE) {

        val viewModel = hiltViewModel<RegistrationScreenViewModel>()

        val presenter = RegistrationScreenPresenterImpl(viewModel, navController)

        RegistrationScreen(presenter)
    }
}