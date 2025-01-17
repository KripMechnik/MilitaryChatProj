package com.application.timer_dmb.presentation.reset_password.confirm_password.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.presentation.presets.ButtonPreset
import com.application.timer_dmb.presentation.presets.InputPreset
import com.application.timer_dmb.presentation.reset_password.confirm_password.ConfirmPasswordPresenter
import com.application.timer_dmb.presentation.reset_password.confirm_password.ResetPasswordState
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPasswordScreen(
    presenter: ConfirmPasswordPresenter
) {

    val newPassword = remember {
        mutableStateOf("")
    }

    val resetPasswordState = presenter.resetPasswordState.collectAsState()

    LaunchedEffect(resetPasswordState.value) {
        if (resetPasswordState.value is ResetPasswordState.Success){
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
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding(), start = 25.dp, end = 25.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
        ){
            Text(
                text = "Введите\nновый пароль",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            InputPreset(
                labelColor = MaterialTheme.colorScheme.onBackground,
                singleLine = true,
                label = "Новый пароль",
                state = newPassword,
                isError = resetPasswordState.value is ResetPasswordState.Error,
            )

            ButtonPreset(
                contentColor = White,
                containerColor = MaterialTheme.colorScheme.secondary,
                content = {
                    when (resetPasswordState.value) {
                        is ResetPasswordState.Loading -> CircularProgressIndicator(color = White)
                        else -> Text(
                            "Подтвердить",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            ) {
                presenter.resetPassword(newPassword.value)
            }

            AnimatedVisibility(
                visible = resetPasswordState.value is ResetPasswordState.Error,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = if (resetPasswordState.value?.code?.equals(-2) == true) "Не удалось подключиться к серверу" else "Не удалось выполнить запрос.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmPasswordScreenPreview() {

    val presenter = object : ConfirmPasswordPresenter {
        override val resetPasswordState: StateFlow<ResetPasswordState?>
            get() = MutableStateFlow(null)

        override fun resetPassword(newPassword: String) {

        }

        override fun navigateUp() {

        }

        override fun navigateToHome() {

        }
    }

    MilitaryChatProjectTheme {
        Surface (
            color = White
        ){
            ConfirmPasswordScreen(presenter)
        }
    }
}
