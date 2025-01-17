package com.application.timer_dmb.presentation.timer_settings.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.TIMER_SETTINGS_SCREEN_ROUTE
import com.application.timer_dmb.presentation.timer_settings.TimerSettingsScreenPresenterImpl
import com.application.timer_dmb.presentation.timer_settings.TimerSettingsScreenViewModel

fun NavGraphBuilder.timerSettings(navController: NavController){
    composable(TIMER_SETTINGS_SCREEN_ROUTE){

        val viewModel = hiltViewModel<TimerSettingsScreenViewModel>()

        val presenter = TimerSettingsScreenPresenterImpl(viewModel, navController)

        TimerSettingsScreen(presenter)

    }
}