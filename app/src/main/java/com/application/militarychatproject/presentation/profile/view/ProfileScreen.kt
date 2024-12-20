package com.application.militarychatproject.presentation.profile.view

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.presentation.profile.LogoutState
import com.application.militarychatproject.presentation.profile.ProfileScreenPresenter
import com.application.militarychatproject.ui.theme.White

@Composable
fun ProfileScreen(
    presenter: ProfileScreenPresenter
) {

    val state by presenter.state.collectAsState()

    LaunchedEffect(state) {
        if (state is LogoutState.Success){
            presenter.navigateToMenu()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        ButtonPreset(
            content = {
                if (state is LogoutState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Выйти из аккаунта", style = MaterialTheme.typography.labelMedium)
                }
            },
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = White
        ) {
            presenter.logout()
        }
    }
}