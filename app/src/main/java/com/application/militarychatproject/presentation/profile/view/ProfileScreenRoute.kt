package com.application.militarychatproject.presentation.profile.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.PROFILE_SCREEN_ROUTE
import com.application.militarychatproject.presentation.profile.ProfileScreenPresenterImpl
import com.application.militarychatproject.presentation.profile.ProfileScreenViewModel

fun NavGraphBuilder.profile(navController: NavController){
    composable(PROFILE_SCREEN_ROUTE){

        val viewModel = hiltViewModel<ProfileScreenViewModel>()

        val presenter = ProfileScreenPresenterImpl(viewModel, navController)

        ProfileScreen(presenter)
    }
}