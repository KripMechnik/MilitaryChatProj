package com.application.timer_dmb.presentation.messanger.all_chats.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.application.timer_dmb.R
import com.application.timer_dmb.presentation.messanger.all_chats.AllChatsScreenPresenter
import com.application.timer_dmb.presentation.messanger.all_chats.ChatsState
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import com.application.timer_dmb.ui.theme.manropeFamily
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AllChatsScreen(
    presenter: AllChatsScreenPresenter
) {

    val chatsState = presenter.chatsState.collectAsState()

    val refreshState = rememberSwipeRefreshState(chatsState.value is ChatsState.Loading)

    val cleared = presenter.cleared.collectAsState()

    var navigatingId by remember {
        mutableStateOf("")
    }

    LaunchedEffect(cleared.value) {
        if (cleared.value){
            presenter.navigateToChat(navigatingId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, bottom = 90.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp),
                text = stringResource(R.string.chats),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )

            SwipeRefresh(
                state = refreshState,
                onRefresh = { presenter.getChats() }
            ) {
                LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item{
                        if (chatsState.value is ChatsState.Success){
                            ChatItem(
                                "_",
                                (chatsState.value as ChatsState.Success).data!!.name,
                                (chatsState.value as ChatsState.Success).data!!.lastMessageSenderName ?: "",
                                (chatsState.value as ChatsState.Success).data!!.lastMessageText ?: "Пока нет сообщений",
                                (chatsState.value as ChatsState.Success).data!!.unreadMessagesAmount ?: "0",
                                (chatsState.value as ChatsState.Success).data!!.lastMessageCreationTime ?: ""
                            ){
                                presenter.onNavigating()
                                navigatingId = chatsState.value!!.data!!.id
                            }
                        } else if (chatsState.value is ChatsState.Unauthorized){
                            Text(
                                modifier = Modifier
                                    .fillMaxSize(),
                                textAlign = TextAlign.Center,
                                text = "Необходимо войти в аккаунт",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }


        }
    }
}

@Composable
fun ChatItem(
    imageUrl: String,
    chatTitle: String,
    lastUser: String,
    lastMessage: String,
    unreadMessages: String,
    timeLastMessageSent: String,
    onAction: () -> Unit
) {

    var size by remember { mutableStateOf(IntSize.Zero) }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onAction()
            }
            .padding(top = 16.dp, bottom = 16.dp, start = 25.dp, end = 25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    size = it
                },
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                model = imageUrl,
                contentDescription = "image",
                error = painterResource(R.drawable.group_chat_icon),
                placeholder = ColorPainter(MaterialTheme.colorScheme.onPrimaryContainer)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = chatTitle,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = lastUser,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

        }

        Box(
            modifier = Modifier
                .then(
                    with(LocalDensity.current) {
                        Modifier.size(
                            width = size.width.toDp(),
                            height = size.height.toDp(),
                        )
                    }
                ),
            contentAlignment = Alignment.TopEnd
        ) {
            Card (
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    Text(
                        text = unreadMessages,
                        style = TextStyle(
                            fontFamily = manropeFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            letterSpacing = 0.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

            }

        }

        Box(
            modifier = Modifier
                .then(
                    with(LocalDensity.current) {
                        Modifier.size(
                            width = size.width.toDp(),
                            height = size.height.toDp(),
                        )
                    }
                ),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = timeLastMessageSent,
                style = TextStyle(
                    fontFamily = manropeFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    letterSpacing = 0.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

    }



}

@Preview
@Composable
private fun ChatItemPreview() {
    MilitaryChatProjectTheme {
        Surface(
            color = White
        ) {
            ChatItem(
            "url",
            "Общий чат",
            "Григорий",
            "Всем привет",
                "43 321",
                "07:23"
            ){}
        }

    }
}

@Preview
@Composable
private fun AllChatsScreenPreview() {

    val presenter = object : AllChatsScreenPresenter {
        override val chatsState: StateFlow<ChatsState?>
            get() = MutableStateFlow(ChatsState.Unauthorized())
        override val cleared: StateFlow<Boolean>
            get() = MutableStateFlow(false)

        override fun navigateToChat(id: String) {

        }

        override fun getChats() {

        }

        override fun onNavigating() {

        }
    }

    MilitaryChatProjectTheme {
        Surface(
            color = White
        ){
            AllChatsScreen(presenter)
        }
    }
}