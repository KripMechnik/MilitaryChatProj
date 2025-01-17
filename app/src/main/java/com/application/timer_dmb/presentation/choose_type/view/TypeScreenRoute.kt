package com.application.timer_dmb.presentation.choose_type.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.TYPE_SCREEN_ROUTE
import com.application.timer_dmb.presentation.choose_type.TypeScreenPresenterImpl
import com.application.timer_dmb.presentation.choose_type.TypeScreenViewModel

fun NavGraphBuilder.type(navController: NavController){
    composable(TYPE_SCREEN_ROUTE){
        val viewModel = hiltViewModel<TypeScreenViewModel>()
        val presenter = TypeScreenPresenterImpl(viewModel, navController)
        TypeScreen(presenter)
    }
}