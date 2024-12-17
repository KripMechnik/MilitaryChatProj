package com.application.militarychatproject.presentation.registration.registration.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.application.militarychatproject.R
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.presentation.presets.InputPreset
import com.application.militarychatproject.presentation.registration.registration.RegistrationScreenPresenter

@Composable
fun RegistrationScreen(
    presenter: RegistrationScreenPresenter
){

    var textState = remember {
        mutableStateOf("")
    }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 60.dp, 30.dp, 45.dp),
        horizontalAlignment = Alignment.Start,

        ) {
        Text(
            text = stringResource(R.string.create_acc),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 15.dp, alignment = Alignment.CenterVertically)

        ){
            InputPreset(
                label = stringResource(R.string.nick),
                state = textState
            )
            InputPreset(
                label = stringResource(R.string.e_mail),
                state = textState
            )
            InputPreset(
                label = stringResource(R.string.password),
                state = textState
            )
            ButtonPreset(
                label = stringResource(R.string.to_register),
                containerColor = MaterialTheme.colorScheme.secondary
            ){
                presenter.navigateToHome()
            }
            ButtonPreset(
                label = stringResource(R.string.already_have_acc),
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ){
                presenter.navigateToLogin()
            }
        }

    }


}


