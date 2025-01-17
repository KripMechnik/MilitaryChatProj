package com.application.timer_dmb.presentation.share_picture.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.SHARE_PICTURE_SCREEN_ROUTE
import com.application.timer_dmb.presentation.share_picture.SharePictureScreenPresenterImpl
import com.application.timer_dmb.presentation.share_picture.SharePictureScreenViewModel

fun NavGraphBuilder.sharePicture(navController: NavController){
    composable(SHARE_PICTURE_SCREEN_ROUTE){
        val viewModel = hiltViewModel<SharePictureScreenViewModel>()

        val presenter = SharePictureScreenPresenterImpl(viewModel, navController)

        SharePictureScreen(presenter)
    }
}