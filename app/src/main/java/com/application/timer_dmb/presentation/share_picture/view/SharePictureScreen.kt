package com.application.timer_dmb.presentation.share_picture.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.application.timer_dmb.R
import com.application.timer_dmb.presentation.home.BackgroundState
import com.application.timer_dmb.presentation.home.HomeState
import com.application.timer_dmb.presentation.messanger.all_chats.ChatsState
import com.application.timer_dmb.presentation.messanger.chat.SingleMessageState
import com.application.timer_dmb.presentation.messanger.chat.view.SharePicture
import com.application.timer_dmb.presentation.presets.ButtonPreset
import com.application.timer_dmb.presentation.profile.ProfileState
import com.application.timer_dmb.presentation.share_picture.SharePictureScreenPresenter
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeApi::class
)
@Composable
fun SharePictureScreen(
    presenter: SharePictureScreenPresenter
) {

    val scope = rememberCoroutineScope()

    val captureController = rememberCaptureController()

    val profileState = presenter.profileState.collectAsState()

    val dataState = presenter.state.collectAsState()

    val messageState = presenter.messageState.collectAsState()

    val bitmap = presenter.shareImageBitmap.collectAsState()

    val context = LocalContext.current

    val chatState = presenter.chatState.collectAsState()

    val background = presenter.backgroundState.collectAsState()

    LaunchedEffect(messageState.value) {
        if (messageState.value is SingleMessageState.Success){
            Toast.makeText(context, "Таймер успешно отправлен", Toast.LENGTH_LONG).show()
        } else if (messageState.value is SingleMessageState.Error){
            Toast.makeText(context, "Ошибка при отправлении таймера", Toast.LENGTH_LONG).show()
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
    ){innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 25.dp,
                    end = 25.dp,
                    bottom = 12.dp
                )
                .navigationBarsPadding()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                text = "Поделиться таймером",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )

            Box(
                modifier = Modifier
                    .zIndex(0f)
                    .shadow(
                        elevation = 5000.dp,
                        spotColor = Color.Black,
                        ambientColor = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .heightIn(max = 450.dp)
                    .widthIn(max = 250.dp)
                    .capturable(captureController)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(16.dp))
            ){
                SharePicture(
                    pictureUrl = profileState.value?.data?.avatarLink ?: "",
                    nickname = profileState.value?.data?.nickname ?: "",
                    background = background.value?.data?.asImageBitmap(),
                    percentage = dataState.value.percentage ?: "",
                    daysLeft = dataState.value.daysLeft.toString(),
                )
            }


            Column(
                modifier = Modifier
                    .zIndex(1f)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        modifier = Modifier
                            .height(60.dp),
                        onClick = {
                            scope.launch {
                                val currentBitmap = captureController.captureAsync().await()
                                try {
                                    val uri = presenter.getImage(currentBitmap)
                                    uri?.let {
                                        val intent = createIntent(uri, "com.vkontakte.android")
                                        context.startActivity(Intent.createChooser(intent, "Share via"))
                                    }
                                } catch (e: Throwable) {
                                    e.printStackTrace()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0077FF)
                        ),
                        enabled = profileState.value is ProfileState.Success || !presenter.isAuthorized(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.vk),
                            contentDescription = "vk"
                        )
                    }

                    Button(
                        modifier = Modifier
                            .height(60.dp),
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),

                        enabled = profileState.value is ProfileState.Success || !presenter.isAuthorized(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(36.dp),
                            painter = painterResource(R.drawable.instagram),
                            contentDescription = "vk",
                            contentScale = ContentScale.Crop
                        )
                    }

                    Button(
                        modifier = Modifier
                            .height(60.dp),
                        onClick = {
                            scope.launch {
                                val currentBitmap = captureController.captureAsync().await()
                                try {
                                    val uri = presenter.getImage(currentBitmap)
                                    uri?.let {
                                        val intent = createIntent(uri, "org.telegram.messenger")
                                        context.startActivity(Intent.createChooser(intent, "Share via"))
                                    }
                                } catch (e: Throwable) {
                                    e.printStackTrace()
                                }
                            }

                        },
                        enabled = profileState.value is ProfileState.Success || !presenter.isAuthorized(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF37AEE2)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Image(

                            painter = painterResource(R.drawable.telegram),
                            contentDescription = "vk"
                        )
                    }
                }

                ButtonPreset(
                    contentColor = White,
                    enabled = chatState.value is ChatsState.Success,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    content = {
                        Text(
                            text = "Отправить в общий чат",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                ) {
                    scope.launch {
                        val currentBitmap = captureController.captureAsync().await()
                        try {
                            presenter.sendBitmap(currentBitmap)
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }
                    }
                }
            }


        }
    }
}

@Preview
@Composable
private fun SharePictureScreenPreview() {

    val presenter = object : SharePictureScreenPresenter{
        override val state: StateFlow<HomeState>
            get() = MutableStateFlow(HomeState())

        override val profileState: StateFlow<ProfileState?>
            get() = MutableStateFlow(null)

        override val shareImageBitmap: StateFlow<ImageBitmap?>
            get() = MutableStateFlow(null)

        override val messageState: StateFlow<SingleMessageState?>
            get() = MutableStateFlow(null)

        override val chatState: StateFlow<ChatsState?>
            get() = MutableStateFlow(null)

        override val backgroundState: StateFlow<BackgroundState?>
            get() = MutableStateFlow(null)

        override fun isAuthorized(): Boolean {
            return true
        }

        override fun getImage(bitmap: ImageBitmap): Uri? {
            return Uri.parse("")
        }

        override fun navigateUp() {

        }

        override fun sendBitmap(bitmap: ImageBitmap) {

        }

        override fun sendTimer() {

        }

    }

    MilitaryChatProjectTheme {
        Surface(
            color = White
        ) {
            SharePictureScreen(presenter)
        }
    }
}

fun createIntent(uri: Uri, app: String): Intent {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.setType("image/png")
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    shareIntent.putExtra(Intent.EXTRA_TEXT, "Мне осталось совсем немного до дембеля!\nЗайди в приложение ДМБ и создай свой таймер: https://duty-timer.sunfesty.ru/privacy-policy"); // Add the text
    shareIntent.setPackage(app) // Optional: Opens Telegram directly

    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    return shareIntent
}
