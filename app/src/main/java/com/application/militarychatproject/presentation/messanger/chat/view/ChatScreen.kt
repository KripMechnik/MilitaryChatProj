package com.application.militarychatproject.presentation.messanger.chat.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.application.militarychatproject.R
import com.application.militarychatproject.presentation.messanger.chat.ChatScreenPresenter
import com.application.militarychatproject.presentation.messanger.chat.MessagesState
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import com.application.militarychatproject.ui.theme.White
import kotlinx.coroutines.flow.distinctUntilChanged

data class MenuItem(
    val title: String,
    val icon: Painter,
    val forOthers: Boolean = true
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    presenter: ChatScreenPresenter
) {



    val messageText = remember {
        mutableStateOf("")
    }


    val listOfMenuItems = listOf(
        MenuItem(
            title = "Удалить",
            icon = painterResource(R.drawable.delete),
            forOthers = false
        ),
        MenuItem(
            title = "Редактировать",
            icon = painterResource(R.drawable.edit),
            forOthers = false
        ),
        MenuItem(
            title = "Ответить",
            icon = painterResource(R.drawable.reply)
        )
    )

    val messagesState = presenter.messagesState.collectAsState()
    val listState = presenter.listState

    val chatListState = rememberLazyListState()

    val replyingSender = remember {
        mutableStateOf("")
    }

    val replyingText = remember {
        mutableStateOf("")
    }



    val replyToId = presenter.replyToId.collectAsState()


    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = chatListState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            (lastVisibleItemIndex > (totalItemsNumber - 2))
        }
    }

    Log.i("state", loadMore.value.toString())

    LaunchedEffect(loadMore) {

        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                if (messagesState.value !is MessagesState.Loading) presenter.getMessages()
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

                                text = "Чаты",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Text(
                                modifier = Modifier
                                    .weight(1f),
                                text = "Общий чат",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Image(
                                modifier = Modifier
                                    .size(48.dp),
                                painter = painterResource(R.drawable.group_chat_icon),
                                contentDescription = "chat_icon"
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
        Column(
            modifier = Modifier
                .imePadding()
                .padding(top = innerPadding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                reverseLayout = true,
                state = chatListState
            ){



                items(listState){ message ->
                    AnimatedVisibility(
                        visible = !message.deleted
                    ) {
                        MessageItem(
                            messageId = message.messageId,
                            isSelfMessage = message.isSender,
                            sendMessageAuthorName = message.senderNickname,
                            sendMessageText = message.text,
                            sendMessageTime = message.creationTime,
                            url = message.senderAvatarLink ?: "",
                            isRead = message.isRead,
                            isLastMessagePerRow = message.isLastInRow,
                            menuItemsList = listOfMenuItems,
                            repliedAuthor = message.repliedMessageSender ?: "",
                            repliedText = message.repliedMessageText ?: "",
                            replyId = message.repliedMessageId ?: "",
                            replyingText = replyingText,
                            replyingSender = replyingSender,
                            presenter = presenter,
                            messageText = messageText,
                            isEdited = message.isEdited
                        )
                    }


                }
                if (messagesState.value is MessagesState.Loading){
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 16.dp)
                        )
                    }
                }
            }

            MessageInput(replyToId = replyToId, presenter = presenter, textState = messageText, modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()), replyingText = replyingText, replyingSender = replyingSender){
                messageText.value = ""
            }
        }

    }
}

@Composable
fun MessageInput(
    modifier: Modifier,
    presenter: ChatScreenPresenter,
    textState: MutableState<String>,
    replyingSender: MutableState<String>,
    replyingText: MutableState<String>,
    replyToId: State<String>,
    onSend: () -> Unit,
) {
    Column (
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = CircleShape
                    )
                    .padding(16.dp),
                imageVector = Icons.Default.Share,
                contentDescription = "share_button",

            )

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
            ) {

                if (replyingSender.value.isNotBlank() || replyingText.value.isNotBlank()){
                    Card(
                        modifier = Modifier
                            .clickable {
                                replyingSender.value = ""
                                replyingText.value = ""
                                presenter.setReplyToId("")
                                onSend()
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top)
                        ) {
                            if (replyingSender.value.isNotBlank()){
                                Text(
                                    text = replyingSender.value,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            Text(
                                text = if(replyingText.value.length > 23) replyingText.value.take(23) + "..." else replyingText.value,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                TextField(
                    value = textState.value,
                    onValueChange = {textState.value = it},
                    placeholder = {
                        Text(
                            text = "Сообщение",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(32.dp),
                )
            }

            Icon(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable {
                        if (replyingSender.value.isBlank() && replyToId.value.isNotBlank()) {
                            presenter.updateMessage(textState.value)

                        } else {
                            presenter.sendMessage(textState.value)
                        }
                        replyingSender.value = ""
                        replyingText.value = ""
                        presenter.setReplyToId("")
                        onSend()
                    }
                    .padding(16.dp),
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "send_button"
            )


        }
    }
}

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    messageId: String,
    presenter: ChatScreenPresenter,
    isSelfMessage: Boolean,
    sendMessageAuthorName: String,
    sendMessageText: String,
    sendMessageTime: String,
    isEdited: Boolean,
    url: String = "",
    isRead: Boolean,
    isLastMessagePerRow: Boolean,
    replyId: String = "",
    repliedAuthor: String = "",
    repliedText: String = "",
    menuItemsList: List<MenuItem>,
    replyingSender: MutableState<String>,
    replyingText: MutableState<String>,
    messageText: MutableState<String>
) {



    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged {
                itemHeight = with(density) {
                    it.height.toDp()
                }
            }
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        isContextMenuVisible = true
                    }
                )
            },
        contentAlignment = if (isSelfMessage) Alignment.TopEnd else Alignment.TopStart,

    ){
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(16.dp, if (isSelfMessage) Alignment.End else Alignment.Start)
        ) {



            if (!isSelfMessage && isLastMessagePerRow){
                AsyncImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    model = url,
                    contentDescription = "Avatar image",
                    error = painterResource(R.drawable.no_avatar),
                    placeholder = ColorPainter(MaterialTheme.colorScheme.onPrimaryContainer)
                )
            } else if (!isSelfMessage){
                Spacer(
                    modifier = Modifier
                        .size(40.dp)
                )
            }



            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelfMessage) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = if (isSelfMessage) RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 2.dp
                        ) else RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 2.dp,
                            bottomEnd = 16.dp
                        ),
                    )
                    .widthIn(max = 240.dp),
                contentAlignment = Alignment.BottomEnd
            ){

                Row (
                    modifier = Modifier
                        .padding(end = 8.dp, bottom = 8.dp, start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    if (isEdited){
                        Text(
                            text = "ред.",
                            style = MaterialTheme.typography.labelSmall,
                            color = if(isSelfMessage) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = sendMessageTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = if(isSelfMessage) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onBackground
                    )
                    if (isSelfMessage){
                        Image(
                            modifier = Modifier
                                .size(16.dp),
                            painter = painterResource(if (isRead) R.drawable.read else R.drawable.unread),
                            contentDescription = "read_icon"
                        )
                    }
                }



                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .defaultMinSize(minHeight = 40.dp, minWidth = 50.dp),
                    horizontalAlignment = if (isSelfMessage) Alignment.End else Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
                ) {

                    if (replyId.isNotBlank()){
                        RepliedMessageItem(
                            repliedAuthor,
                            repliedText,
                            isSelfMessage
                        )
                    }

                    if (!isSelfMessage){
                        Text(
                            text = sendMessageAuthorName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = sendMessageText,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelfMessage) White else MaterialTheme.colorScheme.primary
                    )
                    Spacer(
                        modifier = Modifier
                            .height(4.dp)
                    )
                }
            }

        }

        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            ),
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFFB1B1B1)
        ) {
            menuItemsList.forEach{
                if (isSelfMessage || it.forOthers){
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.title,
                                style = MaterialTheme.typography.labelMedium,
                                color = White
                            )
                        },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp),
                                tint = White,
                                painter = it.icon,
                                contentDescription = "icon_menu"
                            )
                        },
                        onClick = {
                            if (it.title == "Ответить"){
                                replyingText.value = sendMessageText
                                replyingSender.value = sendMessageAuthorName
                                presenter.setReplyToId(messageId)
                            }

                            if (it.title == "Редактировать"){
                                messageText.value = sendMessageText
                                replyingText.value = sendMessageText
                                presenter.setReplyToId(messageId)
                            }

                            if (it.title == "Удалить"){
                                presenter.deleteMessage(messageId)
                            }
                            isContextMenuVisible = false
                        }
                    )
                    HorizontalDivider(
                        color = White.copy(0.1f)
                    )
                }

            }
        }

    }
}

@Composable
fun RepliedMessageItem(
    senderName: String,
    text: String,
    isSelfMessage: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if(isSelfMessage) White.copy(alpha = 0.17f) else White.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top)
        ) {

            Text(
                text = senderName,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelfMessage) White.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = if(text.length > 23) text.take(23) + "..." else text,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelfMessage) White else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun RepliedItemPreview() {
    MilitaryChatProjectTheme {
        RepliedMessageItem(
            "@dmitry",
            "Всем привет я классный парень!",
            false
        )
    }
}

//@Preview
//@Composable
//private fun ChatScreenPreview() {
//
//    val presenter = object : ChatScreenPresenter{
//        override val messagesState: StateFlow<MessagesState?>
//            get() = MutableStateFlow(null)
//        override val listState: StateFlow<SnapshotStateMap<String, MessageEntity>>
//            get() = MutableStateFlow(
//                mutableStateMapOf(
//
//                "1" to
//                MessageEntity(
//                    messageId = "1",
//                    chatId = "1",
//                    creationDate = "Октябрь 14",
//                    attachmentLinks = emptyList(),
//                    creationTime = "07:25",
//                    isEdited = false,
//                    isRead = true,
//                    isSender = false,
//                    senderAvatarLink = "",
//                    senderId = "2",
//                    senderNickname = "Дмитрий",
//                    text = "Ладно, не хотите отвечать, не надо",
//                    repliedMessageId = "",
//                    repliedMessageText = "",
//                    repliedMessageSender = "",
//                    isLastInRow = true
//                ),
//                "2" to
//                MessageEntity(
//                    messageId = "1",
//                    chatId = "1",
//                    creationDate = "Октябрь 14",
//                    attachmentLinks = emptyList(),
//                    creationTime = "07:25",
//                    isEdited = false,
//                    isRead = true,
//                    isSender = false,
//                    senderAvatarLink = "",
//                    senderId = "2",
//                    senderNickname = "Дмитрий",
//                    text = "Где яблоки?",
//                    repliedMessageId = "",
//                    repliedMessageText = "",
//                    repliedMessageSender = "",
//                    isLastInRow = false
//                ),
//                "3" to
//                MessageEntity(
//                    messageId = "1",
//                    chatId = "1",
//                    creationDate = "Октябрь 14",
//                    attachmentLinks = emptyList(),
//                    creationTime = "07:23",
//                    isEdited = false,
//                    isRead = true,
//                    isSender = true,
//                    senderAvatarLink = "",
//                    senderId = "1",
//                    senderNickname = "Легенда",
//                    text = "Что за черт?",
//                    repliedMessageId = "",
//                    repliedMessageText = "",
//                    repliedMessageSender = "",
//                    isLastInRow = true
//                ),
//                "4" to
//                MessageEntity(
//                    messageId = "1",
//                    chatId = "1",
//                    creationDate = "Октябрь 14",
//                    attachmentLinks = emptyList(),
//                    creationTime = "07:25",
//                    isEdited = false,
//                    isRead = true,
//                    isSender = false,
//                    senderAvatarLink = "",
//                    senderId = "2",
//                    senderNickname = "Дмитрий",
//                    text = "Ладно, не хотите отвечать, не надо",
//                    repliedMessageId = "",
//                    repliedMessageText = "",
//                    repliedMessageSender = "",
//                    isLastInRow = true
//                ),
//                "5" to
//                MessageEntity(
//                    messageId = "1",
//                    chatId = "1",
//                    creationDate = "Октябрь 14",
//                    attachmentLinks = emptyList(),
//                    creationTime = "07:25",
//                    isEdited = false,
//                    isRead = true,
//                    isSender = false,
//                    senderAvatarLink = "",
//                    senderId = "2",
//                    senderNickname = "Дмитрий",
//                    text = "Л",
//                    repliedMessageId = "",
//                    repliedMessageText = "",
//                    repliedMessageSender = "",
//                    isLastInRow = true
//                )
//            )
//            )
//
//        override val singleMessageState: StateFlow<SingleMessageState?>
//            get() = MutableStateFlow(null)
//        override val replyToId: StateFlow<String>
//            get() = MutableStateFlow("")
//        override val lastMessages: StateFlow<Boolean>
//            get() = MutableStateFlow(false)
//        override val sendState: StateFlow<SendState?>
//            get() = MutableStateFlow(null)
//
//        override fun navigateUp() {
//
//        }
//
//        override fun getMessages() {
//
//        }
//
//        override fun sendMessage(text: String) {
//
//        }
//
//        override fun setReplyToId(newId: String) {
//
//        }
//
//        override fun updateMessage(newText: String) {
//
//        }
//
//        override fun deleteMessage(messageId: String) {
//
//        }
//
//    }
//
//    MilitaryChatProjectTheme {
//        ChatScreen(presenter)
//    }
//}

//@Preview
//@Composable
//private fun SelfMessageItemPreview() {
//
//    val presenter = object : ChatScreenPresenter{
//        override val messagesState: StateFlow<MessagesState?>
//            get() = MutableStateFlow(null)
//        override val listState: StateFlow<SnapshotStateList<String, MessageEntity>>
//            get() = MutableStateFlow(mutableStateMapOf())
//
//        override val singleMessageState: StateFlow<SingleMessageState?>
//            get() = MutableStateFlow(null)
//        override val replyToId: StateFlow<String>
//            get() = MutableStateFlow("")
//        override val lastMessages: StateFlow<Boolean>
//            get() = MutableStateFlow(false)
//        override val sendState: StateFlow<SendState?>
//            get() = MutableStateFlow(null)
//
//        override fun navigateUp() {
//
//        }
//
//        override fun getMessages() {
//
//        }
//
//        override fun sendMessage(text: String) {
//
//        }
//
//        override fun setReplyToId(newId: String) {
//
//        }
//
//        override fun updateMessage(newText: String) {
//
//        }
//
//        override fun deleteMessage(messageId: String) {
//
//        }
//
//    }
//
//    MilitaryChatProjectTheme {
//        MessageItem(
//            Modifier,
//            "",
//            presenter,
//            true,
//            "Я",
//            "Всем привет! Я новенький в этой беседе.",
//            "07:23",
//            "",
//            true,
//            true,
//            menuItemsList = emptyList(),
//            replyingSender = remember{mutableStateOf("")},
//            replyingText = remember{mutableStateOf("")},
//            messageText = remember{mutableStateOf("")}
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun AnotherMessageItemPreview() {
//
//    val presenter = object : ChatScreenPresenter{
//        override val messagesState: StateFlow<MessagesState?>
//            get() = MutableStateFlow(null)
//        override val listState: StateFlow<SnapshotStateMap<String, MessageEntity>>
//            get() = MutableStateFlow(mutableStateMapOf())
//
//        override val singleMessageState: StateFlow<SingleMessageState?>
//            get() = MutableStateFlow(null)
//        override val replyToId: StateFlow<String>
//            get() = MutableStateFlow("")
//        override val lastMessages: StateFlow<Boolean>
//            get() = MutableStateFlow(false)
//        override val sendState: StateFlow<SendState?>
//            get() = MutableStateFlow(null)
//
//        override fun navigateUp() {
//
//        }
//
//        override fun getMessages() {
//
//        }
//
//        override fun sendMessage(text: String) {
//
//        }
//
//        override fun setReplyToId(newId: String) {
//
//        }
//
//        override fun updateMessage(newText: String) {
//
//        }
//
//        override fun deleteMessage(messageId: String) {
//
//        }
//
//    }
//
//    MilitaryChatProjectTheme {
//        MessageItem(
//            Modifier,
//            "",
//            presenter,
//            false,
//            "@anton_verdikov",
//            "Всем привет! Я новенький в этой беседе.",
//            "07:23",
//            "",
//            false,
//            false,
//            replyId = "1",
//            repliedAuthor = "@dmitry",
//            repliedText = "Всем привет, я классный парень!",
//            menuItemsList = emptyList(),
//            replyingSender = remember{mutableStateOf("")},
//            replyingText = remember{mutableStateOf("")},
//            messageText = remember{mutableStateOf("")}
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun AnotherMessageItemWithAvatarPreview() {
//
//    val presenter = object : ChatScreenPresenter{
//        override val messagesState: StateFlow<MessagesState?>
//            get() = MutableStateFlow(null)
//        override val listState: StateFlow<SnapshotStateMap<String, MessageEntity>>
//            get() = MutableStateFlow(SnapshotStateMap())
//
//        override val singleMessageState: StateFlow<SingleMessageState?>
//            get() = MutableStateFlow(null)
//        override val replyToId: StateFlow<String>
//            get() = MutableStateFlow("")
//        override val lastMessages: StateFlow<Boolean>
//            get() = MutableStateFlow(false)
//        override val sendState: StateFlow<SendState?>
//            get() = MutableStateFlow(null)
//
//        override fun navigateUp() {
//
//        }
//
//        override fun getMessages() {
//
//        }
//
//        override fun sendMessage(text: String) {
//
//        }
//
//        override fun setReplyToId(newId: String) {
//
//        }
//
//        override fun updateMessage(newText: String) {
//
//        }
//
//        override fun deleteMessage(messageId: String) {
//
//        }
//
//    }
//
//    MilitaryChatProjectTheme {
//        MessageItem(
//            Modifier,
//            "",
//            presenter,
//            false,
//            "@anton_verdikov",
//            "Всем привет! Я новенький в этой беседе.",
//            "07:23",
//            "",
//            false,
//            true,
//            menuItemsList = emptyList(),
//            replyingSender = remember{mutableStateOf("")},
//            replyingText = remember{mutableStateOf("")},
//            messageText = remember{mutableStateOf("")}
//        )
//    }
//}