package com.application.militarychatproject.presentation.registration.first.view

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val REGISTRATION_SCREEN_ROUTE = "registration"

fun NavGraphBuilder.registrationFirst(navController: NavController) {
    composable(REGISTRATION_SCREEN_ROUTE) {

        //val splashViewModel = hiltViewModel<SplashScreenViewModel>()


//        val presenter = SplashScreenPresenterImpl(
//            viewModel = splashViewModel,
//            navController = navController
//        )
        RegistrationScreen()
    }
}