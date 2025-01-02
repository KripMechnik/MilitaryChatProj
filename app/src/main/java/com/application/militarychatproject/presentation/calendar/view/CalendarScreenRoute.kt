package com.application.militarychatproject.presentation.calendar.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.CALENDAR_SCREEN_ROUTE
import com.application.militarychatproject.presentation.calendar.CalendarScreenPresenterImpl
import com.application.militarychatproject.presentation.calendar.CalendarScreenViewModel

fun NavGraphBuilder.calendar(navController: NavController){

    composable(CALENDAR_SCREEN_ROUTE){
        val viewModel = hiltViewModel<CalendarScreenViewModel>()

        val presenter = CalendarScreenPresenterImpl(viewModel, navController)
        CalendarScreen(presenter)
    }
}