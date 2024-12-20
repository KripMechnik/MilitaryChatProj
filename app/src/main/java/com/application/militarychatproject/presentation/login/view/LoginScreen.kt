package com.application.militarychatproject.presentation.login.view

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.militarychatproject.R
import com.application.militarychatproject.presentation.login.LoginScreenPresenter
import com.application.militarychatproject.presentation.login.LoginState
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.presentation.presets.InputPreset
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import com.application.militarychatproject.ui.theme.White
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoginScreen(
    presenter: LoginScreenPresenter
) {
    val eMail = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    val isErrorEmail = remember {
        derivedStateOf {
            if (eMail.value.isBlank()){
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(eMail.value).matches().not()
            }
        }
    }

    val state = presenter.state.collectAsState()

    LaunchedEffect(state.value) {
        if (state.value is LoginState.Success) {
            presenter.navigateToHome()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 120.dp, 30.dp, 45.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.Start,

        ) {
        Text(
            text = stringResource(R.string.enter_acc),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 15.dp, alignment = Alignment.CenterVertically)

        ){
            InputPreset(
                label = stringResource(R.string.e_mail),
                state = eMail,
                isError = isErrorEmail.value,
                errorText = "Ошибка. E-Mail адрес не соответствует формату.",
            )
            InputPreset(
                label = stringResource(R.string.password),
                state = password,
                visualTransformation = PasswordVisualTransformation()
            )
            ButtonPreset(
                content = {
                    when (state.value){
                        is LoginState.Loading -> CircularProgressIndicator(color = White)
                        else -> Text(stringResource(R.string.enter), style = MaterialTheme.typography.labelMedium)
                    }
                },
                containerColor = MaterialTheme.colorScheme.secondary
            ){
                if (!isErrorEmail.value){
                    presenter.loginUser(eMail.value, password.value)
                }


            }
            ButtonPreset(
                label = stringResource(R.string.forget_password),
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ){
                presenter.navigateToReset()
            }
        }

    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    val presenter = object : LoginScreenPresenter{
        override val state: StateFlow<LoginState?>
            get() = MutableStateFlow(null)

        override fun navigateToHome() {

        }

        override fun navigateToReset() {

        }

        override fun loginUser(eMail: String, password: String) {

        }

    }
    MilitaryChatProjectTheme {
        LoginScreen(presenter)
    }
}