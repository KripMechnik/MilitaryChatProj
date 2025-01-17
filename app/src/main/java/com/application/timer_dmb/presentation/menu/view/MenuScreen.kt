package com.application.timer_dmb.presentation.menu.view

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import coil3.compose.AsyncImage
import com.application.timer_dmb.R
import com.application.timer_dmb.domain.entity.receive.SelfUserEntity
import com.application.timer_dmb.presentation.menu.MenuScreenPresenter
import com.application.timer_dmb.presentation.menu.MenuState
import com.application.timer_dmb.presentation.presets.ButtonPreset
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun MenuScreen(
    presenter: MenuScreenPresenter
) {
    val registered by presenter.registered.collectAsState()
    presenter.checkAuthorized()

    val context = LocalContext.current

    val imageData = remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            imageData.value = it
        }

    LaunchedEffect (registered) {
        if (registered){
            presenter.getSelfUser()
        }
    }

    LaunchedEffect(imageData.value) {
        imageData.let {

            val uri = it.value
            if (uri != null) {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver, uri)

                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, uri)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let { btm ->
                    presenter.saveBitmapAsFile(btm)
                }
            }

        }
    }

    if (registered){
        Registered(presenter, launcher)
    } else {
        Unregistered(presenter, launcher)
    }
}

@Composable
fun Unregistered(
    presenter: MenuScreenPresenter,
    launcher: ManagedActivityResultLauncher<String, Uri?>? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, bottom = 90.dp),
        contentAlignment = Alignment.TopStart
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.menu),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            presenter.navigateToCalendar()
                        }
                        .padding(top = 12.dp, bottom = 12.dp, start = 25.dp, end = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ){

                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(R.drawable.calendar),
                        contentDescription = "calendar"
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.calendary),
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
                            presenter.navigateToSettings()
                        }
                        .padding(top = 12.dp, bottom = 12.dp, start = 25.dp, end = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(R.drawable.settings),
                        contentDescription = "settings"
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.options),
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
                            launcher?.launch(
                                "image/*"
                            )
                        }
                        .padding(top = 12.dp, bottom = 12.dp, start = 25.dp, end = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ){
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(R.drawable.change_bg),
                        contentDescription = "change_bg"
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.change_background),
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
            }


            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            ButtonPreset(
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp),
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
fun Registered(
    presenter: MenuScreenPresenter,
    launcher: ManagedActivityResultLauncher<String, Uri?>? = null
) {

    val state by presenter.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, bottom = 90.dp),
        contentAlignment = Alignment.TopStart
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
                text = stringResource(R.string.menu),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable(
                        enabled = state is MenuState.Success
                    ) {
                        presenter.navigateToProfile()
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {

                if (state is MenuState.Error){
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = (state as MenuState.Error).code.toString() + ": не удалось войти в аккаунт" ,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
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
                            error = painterResource(R.drawable.no_avatar),
                            placeholder = ColorPainter(MaterialTheme.colorScheme.onPrimaryContainer)
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


            }

            Column{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            presenter.navigateToCalendar()
                        }
                        .padding(top = 12.dp, bottom = 12.dp, start = 25.dp, end = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ){

                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(R.drawable.calendar),
                        contentDescription = "calendar"
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.calendary),
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
                            presenter.navigateToSettings()
                        }
                        .padding(top = 12.dp, bottom = 12.dp, start = 25.dp, end = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ){
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(R.drawable.settings),
                        contentDescription = "settings"
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.options),
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
                            launcher?.launch(
                                "image/*"
                            )
                        }
                        .padding(top = 12.dp, bottom = 12.dp, start = 25.dp, end = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ){
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(R.drawable.change_bg),
                        contentDescription = "change_bg"
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.change_background),
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
            }


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

        override fun navigateToCalendar() {

        }

        override fun checkAuthorized() {

        }

        override fun getSelfUser() {

        }

        override fun navigateToSettings() {

        }

        override fun saveBitmapAsFile(bitmap: Bitmap) {

        }

    }

    MilitaryChatProjectTheme {
        Surface(
            color = White
        ) {
            Registered(presenter)
        }
    }
    
}