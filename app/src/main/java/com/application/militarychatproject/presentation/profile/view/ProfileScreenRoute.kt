package com.application.militarychatproject.presentation.profile.view

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.presentation.registration.first.view.RegistrationScreen

const val PROFILE_SCREEN_ROUTE = "profile"

fun NavGraphBuilder.profile(navController: NavController) {
    composable(PROFILE_SCREEN_ROUTE) {
        RegistrationScreen()
    }
}