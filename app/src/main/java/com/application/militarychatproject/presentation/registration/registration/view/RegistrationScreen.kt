package com.application.militarychatproject.presentation.registration.registration.view

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.application.militarychatproject.R
import com.application.militarychatproject.presentation.login.LoginState
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.presentation.presets.InputPreset
import com.application.militarychatproject.presentation.registration.registration.RegistrationScreenPresenter
import com.application.militarychatproject.presentation.registration.registration.RegistrationState
import com.application.militarychatproject.ui.theme.White

@Composable
fun RegistrationScreen(
    presenter: RegistrationScreenPresenter
){

    val email = remember {
        mutableStateOf("")
    }

    val isErrorEmail = remember {
        derivedStateOf {
            if (email.value.isBlank()){
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(email.value).matches().not()
            }
        }
    }

    val nickname = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    val state = presenter.state.collectAsState()

    LaunchedEffect(state.value) {
        if (state.value is RegistrationState.Success) {
            presenter.navigateToOtp(email = email.value)
        }
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
                state = nickname
            )
            InputPreset(
                label = stringResource(R.string.e_mail),
                state = email,
                isError = isErrorEmail.value,
                errorText = "Ошибка. E-Mail адрес не соответствует формату."
            )
            InputPreset(
                label = stringResource(R.string.password),
                state = password
            )
            ButtonPreset(
                content = {
                    when (state.value){
                        is RegistrationState.Loading -> CircularProgressIndicator(color = White)
                        else -> Text(stringResource(R.string.to_register), style = MaterialTheme.typography.labelMedium)
                    }
                },
                containerColor = MaterialTheme.colorScheme.secondary
            ){
                if (!isErrorEmail.value){
                    presenter.signUp(nickname.value, password.value, email.value)
                    //presenter.navigateToOtp(email.value)
                }
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


