package com.application.timer_dmb.presentation.set_photo.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.SET_PHOTO_SCREEN_ROUTE
import com.application.timer_dmb.presentation.set_photo.SetPhotoScreenPresenterImpl
import com.application.timer_dmb.presentation.set_photo.SetPhotoScreenViewModel

fun NavGraphBuilder.setPhoto(navController: NavController){
    composable(SET_PHOTO_SCREEN_ROUTE){
        val viewModel = hiltViewModel<SetPhotoScreenViewModel>()
        val presenter = SetPhotoScreenPresenterImpl(viewModel, navController)
        SetPhotoScreen(presenter)
    }
}