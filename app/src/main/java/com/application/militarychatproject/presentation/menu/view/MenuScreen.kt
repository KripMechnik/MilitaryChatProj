package com.application.militarychatproject.presentation.menu.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.application.militarychatproject.R
import com.application.militarychatproject.domain.entity.receive.SelfUserEntity
import com.application.militarychatproject.presentation.menu.MenuScreenPresenter
import com.application.militarychatproject.presentation.menu.MenuState
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import com.application.militarychatproject.ui.theme.White
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MenuScreen(
    presenter: MenuScreenPresenter
) {
    val registered by presenter.registered.collectAsState()
    presenter.checkAuthorized()

    LaunchedEffect (registered) {
        if (registered){
            presenter.getSelfUser()
        }
    }

    if (registered){
        Registered(presenter)
    } else {
        Unregistered(presenter)
    }
}

@Composable
fun Unregistered(
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
                    text = stringResource(R.string.calendary),
                    style = MaterialTheme.typography.headlineMedium,
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
                    style = MaterialTheme.typography.headlineMedium,
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
                    style = MaterialTheme.typography.headlineMedium,
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

@Composable
fun Registered(presenter: MenuScreenPresenter) {

    val state by presenter.state.collectAsState()

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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable {
                        presenter.navigateToProfile()
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    AsyncImage(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        model = state?.data?.avatarLink,
                        contentDescription = "Avatar image",
                        error = painterResource(R.drawable.no_avatar)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = state?.data?.nickname ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Настройки профиля",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Icon(
                                modifier = Modifier
                                    .size(16.dp),
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "to_profile",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }

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
                    style = MaterialTheme.typography.headlineMedium,
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
                    style = MaterialTheme.typography.headlineMedium,
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
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )
            }

            HorizontalDivider()
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun RegisteredPreview() {

    val presenter = object : MenuScreenPresenter{
        override val state: StateFlow<MenuState>
            get() = MutableStateFlow(MenuState.Success(SelfUserEntity("", "", "@Example_login", "@Example_login", "")))
        override val registered: StateFlow<Boolean>
            get() = MutableStateFlow(true)

        override fun navigateToRegister() {

        }

        override fun navigateToProfile() {

        }

        override fun checkAuthorized() {

        }

        override fun getSelfUser() {

        }

    }

    MilitaryChatProjectTheme {
        Registered(presenter)
    }
    
}