package com.application.timer_dmb.presentation.reset_password.reset.view

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
import com.application.timer_dmb.presentation.reset_password.reset.GetOtpResetState
import com.application.timer_dmb.presentation.reset_password.reset.ResetScreenPresenter
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetScreen(
    presenter: ResetScreenPresenter
) {

    val email = remember {
        mutableStateOf("")
    }

    val getOtpResetState = presenter.getOtpResetState.collectAsState()

    LaunchedEffect(getOtpResetState.value) {
        if (getOtpResetState.value is GetOtpResetState.Success){
            presenter.navigateToOtpResetScreen(email.value)
            presenter.resetState()
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
                                text = "Меню",
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
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding(), start = 25.dp, end = 25.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
        ){
            Text(
                text = "Сбросить пароль",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            InputPreset(
                labelColor = MaterialTheme.colorScheme.onBackground,
                singleLine = true,
                label = "E-mail",
                enabled = getOtpResetState.value !is GetOtpResetState.Loading,
                state = email,
                isError = getOtpResetState.value is GetOtpResetState.Error,
            )

            ButtonPreset(
                contentColor = White,
                containerColor = MaterialTheme.colorScheme.secondary,
                content = {
                    when (getOtpResetState.value) {
                        is GetOtpResetState.Loading -> CircularProgressIndicator(color = White)
                        else -> Text(
                            "Отправить код",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            ) {
                presenter.getOtpForResetPassword(email.value)
            }

            AnimatedVisibility(
                visible = getOtpResetState.value is GetOtpResetState.Error,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = if (getOtpResetState.value?.code?.equals(-2) == true) "Не удалось подключиться к серверу" else "Не удалось отправить код на почту.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview
@Composable
private fun ResetScreenPreview() {
    val presenter = object : ResetScreenPresenter{
        override val getOtpResetState: StateFlow<GetOtpResetState?>
            get() = MutableStateFlow(null)

        override fun navigateToOtpResetScreen(email: String) {

        }

        override fun navigateUp() {

        }

        override fun getOtpForResetPassword(email: String) {

        }

        override fun resetState() {

        }

    }
    MilitaryChatProjectTheme {
        Surface (
            color = White
        ){
            ResetScreen(presenter)
        }
    }
}