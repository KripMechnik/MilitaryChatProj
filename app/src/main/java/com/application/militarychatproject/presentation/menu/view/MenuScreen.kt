package com.application.militarychatproject.presentation.menu.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.application.militarychatproject.R
import com.application.militarychatproject.presentation.menu.MenuScreenPresenter
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.ui.theme.White

@Composable
fun MenuScreen(
    presenter: MenuScreenPresenter
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 60.dp, 30.dp, 90.dp),
        contentAlignment = Alignment.TopStart
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.menu),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )

            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {},
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.friends),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )
            }

            HorizontalDivider()

            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {},
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.calendary),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )
            }

            HorizontalDivider()

            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {},
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.options),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )
            }

            HorizontalDivider()

            TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {},
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.change_background),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )
            }

            HorizontalDivider()
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            ButtonPreset(
                label = stringResource(R.string.to_register),
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = White
            ) {
                presenter.navigateToRegister()
            }
        }
    }
}