package com.application.timer_dmb.presentation.login.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.R
import com.application.timer_dmb.presentation.login.LoginScreenPresenter
import com.application.timer_dmb.presentation.login.LoginState
import com.application.timer_dmb.presentation.presets.ButtonPreset
import com.application.timer_dmb.presentation.presets.InputPreset
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
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

    val state = presenter.state.collectAsState()

    LaunchedEffect(state.value) {
        if (state.value is LoginState.Success) {
            presenter.navigateToHome()
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = White,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Row (
                            modifier = Modifier
                                .padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Text(
                                modifier = Modifier
                                    .clickable {
                                        presenter.navigateUp()
                                    },
                                text = "Назад",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                    },
                    navigationIcon = {
                        IconButton(onClick = { presenter.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

        }
    ) {
        innerPadding ->

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp, innerPadding.calculateTopPadding(), 25.dp, 45.dp)
                .imePadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.Start,

            ) {
            Text(
                text = stringResource(R.string.enter_acc),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically)

            ){
                item {
                    AnimatedVisibility(
                        visible = state.value is LoginState.Error,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = if (state.value?.code?.equals(-2) == true) "Не удалось подключиться к серверу" else "Ошибка! Неверный E-mail или пароль.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                item {
                    InputPreset(
                        label = stringResource(R.string.e_mail),
                        state = eMail,
                        singleLine = true,
                        isError = state.value is LoginState.Error,
                    )
                }

                item {

                    InputPreset(
                        label = stringResource(R.string.password),
                        state = password,
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = state.value is LoginState.Error,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                    )
                }

                item {
                    ButtonPreset(
                        content = {
                            when (state.value){
                                is LoginState.Loading -> CircularProgressIndicator(color = White)
                                else -> Text(stringResource(R.string.enter), style = MaterialTheme.typography.labelMedium)
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.secondary
                    ){
                        presenter.loginUser(eMail.value, password.value)
                    }
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                    )
                }

                item {
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
    }


}

@Preview
@Composable
private fun LoginScreenPreview() {
    val presenter = object : LoginScreenPresenter{
        override val state: StateFlow<LoginState?>
            get() = MutableStateFlow(null)

        override fun navigateUp() {

        }

        override fun navigateToHome() {

        }

        override fun navigateToReset() {

        }

        override fun loginUser(eMail: String, password: String) {

        }

    }
    MilitaryChatProjectTheme {
        Surface(
          color = White
        ) {
            LoginScreen(presenter)
        }

    }
}