package com.application.timer_dmb.presentation.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.R
import com.application.timer_dmb.presentation.settings.SettingsScreenPresenter
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    presenter: SettingsScreenPresenter
) {

    var showResetBg by remember {
        mutableStateOf(false)
    }


    if (showResetBg){
        AlertDialog(
            title = {
                Text(text = "Сбросить задний фон.", style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Text(text = "Вы действительно хотите сьросить задний фон?", style = MaterialTheme.typography.bodySmall)
            },
            onDismissRequest = { showResetBg = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        presenter.resetBackground()
                        showResetBg = false
                    }
                ) {
                    Text("Подтвердить", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showResetBg = false
                    }
                ) {
                    Text("Отменить", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        )
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
    ){innerPadding ->
        Box(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp),
                    text = "Настройки",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )

                Column {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//
//                            }
//                            .padding(top = 12.dp, bottom = 12.dp, end = 30.dp, start = 30.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
//                    ){
//
//                        Icon(
//                            tint = MaterialTheme.colorScheme.primary,
//                            painter = painterResource(R.drawable.darker_bg),
//                            contentDescription = "calendar"
//                        )
//
//                        Text(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            text = "Затемнение фона",
//                            style = MaterialTheme.typography.headlineMedium,
//                            color = MaterialTheme.colorScheme.primary,
//                            textAlign = TextAlign.Start
//                        )
//                    }
//
//                    HorizontalDivider(
//                        modifier = Modifier
//                            .padding(start = 30.dp, end = 30.dp),
//                        color = MaterialTheme.colorScheme.onPrimaryContainer
//                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showResetBg = true
                            }
                            .padding(top = 12.dp, bottom = 12.dp, start = 25.dp, end = 25.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                    ){

                        Icon(
                            tint = MaterialTheme.colorScheme.primary,
                            painter = painterResource(R.drawable.reset_bg),
                            contentDescription = "calendar"
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Сбросить задний фон",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Start
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 25.dp, end = 25.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                presenter.navigateToTimerSettings()
                            }
                            .padding(top = 12.dp, bottom = 12.dp, start = 25.dp, end = 25.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                    ){

                        Icon(
                            tint = MaterialTheme.colorScheme.primary,
                            painter = painterResource(R.drawable.timer_settings),
                            contentDescription = "calendar"
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Настройки таймера",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Start
                        )
                    }
                }


            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {

    val presenter = object : SettingsScreenPresenter{
        override fun navigateUp() {

        }

        override fun navigateToTimerSettings() {

        }

        override fun resetBackground() {

        }

    }

    MilitaryChatProjectTheme {
        Surface(
            color = White
        ) {
            SettingsScreen(presenter)
        }
    }
}