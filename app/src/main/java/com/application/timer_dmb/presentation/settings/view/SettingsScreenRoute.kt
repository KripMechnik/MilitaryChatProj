package com.application.timer_dmb.presentation.settings.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.SETTINGS_SCREEN_ROUTE
import com.application.timer_dmb.presentation.settings.SettingsScreenPresenterImpl
import com.application.timer_dmb.presentation.settings.SettingsScreenViewModel

fun NavGraphBuilder.settings(navController: NavController){
    composable(SETTINGS_SCREEN_ROUTE){
        val viewModel = hiltViewModel<SettingsScreenViewModel>()

        val presenter = SettingsScreenPresenterImpl(viewModel, navController)

        SettingsScreen(presenter)
    }
}