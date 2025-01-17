package com.application.timer_dmb.presentation.messanger.chat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.receive.DeletedMessageWebSocketEntity
import com.application.timer_dmb.domain.entity.receive.EditedMessageWebSocketEntity
import com.application.timer_dmb.domain.entity.receive.MessageEntity
import com.application.timer_dmb.domain.entity.receive.NewMessageWebSocketEntity
import com.application.timer_dmb.domain.entity.send.UpdatedMessageEntity
import com.application.timer_dmb.domain.usecases.authorization.GetSoldierDataUseCase
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.timer_dmb.domain.usecases.messages.DeleteMessageUseCase
import com.application.timer_dmb.domain.usecases.messages.GetListOfMessagesUnregisteredUseCase
import com.application.timer_dmb.domain.usecases.messages.GetListOfMessagesUseCase
import com.application.timer_dmb.domain.usecases.messages.ReadMessageUseCase
import com.application.timer_dmb.domain.usecases.messages.SendMessageUseCase
import com.application.timer_dmb.domain.usecases.messages.UpdateMessageUseCase
import com.application.timer_dmb.domain.usecases.timer.GetTimerDataUseCase
import com.application.timer_dmb.domain.usecases.user.GetSelfUserDataUseCase
import com.application.timer_dmb.domain.usecases.web_socket.ListenToSocketUseCase
import com.application.timer_dmb.presentation.home.BackgroundState
import com.application.timer_dmb.presentation.home.HomeState
import com.application.timer_dmb.presentation.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val getListOfMessagesUseCase: GetListOfMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val listenToSocketUseCase: ListenToSocketUseCase,
    private val readMessageUseCase: ReadMessageUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val getSoldierDataUseCase: GetSoldierDataUseCase,
    private val getSelfUserDataUseCase: GetSelfUserDataUseCase,
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
    private val getTimerDataUseCase: GetTimerDataUseCase,
    private val getListOfMessagesUnregisteredUseCase: GetListOfMessagesUnregisteredUseCase,
    @ApplicationContext private val context: Context,
    private val json: Json,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id = savedStateHandle.getStateFlow("id", "")

    private val _authorized = MutableStateFlow(isAuthorizedUseCase())
    val authorized = _authorized.asStateFlow()

    private val _background = MutableStateFlow<BackgroundState?>(null)
    val background = _background.asStateFlow()

    private val _shareImageBitmap = MutableStateFlow<ImageBitmap?>(null)
    val shareImageBitmap = _shareImageBitmap.asStateFlow()

    private var _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> get() = _state

    private val _profileState = MutableStateFlow<ProfileState?>(null)
    val profileState = _profileState.asStateFlow()

    val lastMessages = MutableStateFlow(false)

    private val _replyToId = MutableStateFlow("")
    val replyToId = _replyToId.asStateFlow()

    private val lastMessageId = MutableStateFlow("")

    private val _messagesState = MutableStateFlow<MessagesState?>(null)
    val messagesState = _messagesState.asStateFlow()

    private val _singleMessageState = MutableStateFlow<SingleMessageState?>(null)
    val singleMessageState = _singleMessageState.asStateFlow()

    var listState = SnapshotStateList<MessageEntity>()

    private val _sendState = MutableStateFlow<SendState?>(null)
    val sendState = _sendState.asStateFlow()

    init {
        if (_authorized.value){
            getSoldierData()
            getSelfUser()
            readMessages()
            getImageFromCache()
        }
        listenToSocket()
        getChatMessages()
    }

    fun setBitmap(bitmap: ImageBitmap){
        _shareImageBitmap.value = bitmap
        Log.i("bitmap_share", _shareImageBitmap.value.toString())
    }

    private fun getImageFromCache(){
        if (_authorized.value){
            viewModelScope.launch(Dispatchers.IO) {
                _background.value = BackgroundState.Loading()
                val file = File(context.cacheDir, "background_image.png")
                if (file.exists()) {
                    _background.value = BackgroundState.Success(data = BitmapFactory.decodeFile(file.absolutePath))
                } else {
                    _background.value = BackgroundState.Error()
                }
            }
        }
    }

    fun updateMessage(newText: String){
        val updatedMessageEntity = UpdatedMessageEntity(newText)
        val tempId = replyToId.value
        updateMessageUseCase(tempId, updatedMessageEntity).onEach {result ->
            when(result){
                is Resource.Error -> {
                    Log.e("chat_send_message", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> {
                    _sendState.value = SendState.Loading()
                }
                is Resource.Success -> {
                    _sendState.value = SendState.Success()

                    listState.forEachIndexed { index, item->
                        if (item.messageId == tempId){
                            listState[index] = item.copy(isEdited = true, text = newText)
                        }
                    }

                }
            }
        }.launchIn(viewModelScope)
    }

    private fun readMessages() {

        if (_authorized.value) {
            readMessageUseCase(id.value).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("read", result.code.toString() + " " + result.message)
                    }

                    is Resource.Loading -> {}
                    is Resource.Success -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun listenToSocket(){

        if (_authorized.value){
            listenToSocketUseCase(true).onEach { result ->
                Log.i("web_socket", result.data ?: "Empty")
                try {
                    val resp = json.decodeFromString<NewMessageWebSocketEntity>(result.data!!)
                    if (listState.first().senderId == resp.data.senderId){
                        listState.first().isLastInRow = false
                    }
                    resp.data.isLastInRow = true

                    if (!resp.data.isSender){
                        listState.add(0, resp.data)
                    }
                    readMessages()
                    return@onEach
                } catch (_: Exception){
                    try {
                        val resp = json.decodeFromString<EditedMessageWebSocketEntity>(result.data!!)
                        listState.forEach { item->
                            if (item.messageId == resp.data.messageId.toString()){
                                item.isEdited = true
                                item.text = resp.data.text
                            }
                        }
                        return@onEach
                    } catch (_: Exception){
                        try {
                            val resp = json.decodeFromString<DeletedMessageWebSocketEntity>(result.data!!)

                            listState.forEach { item->
                                if (item.messageId == resp.data.messageId.toString()){
                                    item.deleted = true
                                }
                            }
                            return@onEach
                        } catch (_: Exception){}
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            listenToSocketUseCase(false).onEach { result ->
                Log.i("web_socket", result.data ?: "Empty")
                try {
                    val resp = json.decodeFromString<NewMessageWebSocketEntity>(result.data!!)
                    if (listState.first().senderId == resp.data.senderId){
                        listState.first().isLastInRow = false
                    }
                    resp.data.isLastInRow = true

                    if (!resp.data.isSender){
                        listState.add(0, resp.data)
                    }
                    readMessages()
                    return@onEach
                } catch (_: Exception){
                    try {
                        val resp = json.decodeFromString<EditedMessageWebSocketEntity>(result.data!!)
                        listState.forEach { item->
                            if (item.messageId == resp.data.messageId.toString()){
                                item.isEdited = true
                                item.text = resp.data.text
                            }
                        }
                        return@onEach
                    } catch (_: Exception){
                        try {
                            val resp = json.decodeFromString<DeletedMessageWebSocketEntity>(result.data!!)

                            listState.forEach { item->
                                if (item.messageId == resp.data.messageId.toString()){
                                    item.deleted = true
                                }
                            }
                            return@onEach
                        } catch (_: Exception){}
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun setReplyToId(newId: String){
        _replyToId.value = newId
    }

    private fun getSelfUser(){

        if (_authorized.value){
            getSelfUserDataUseCase().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _profileState.value = ProfileState.Error(message = result.message ?: "Unknown error", code = result.code)
                        Log.e("menu", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> _profileState.value = ProfileState.Loading()
                    is Resource.Success -> _profileState.value = ProfileState.Success(data = result.data!!)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun sendMessage(text: String){
        sendMessageUseCase(id.value, text, replyToId.value).onEach {result ->
            when(result){
                is Resource.Error -> {
                    _singleMessageState.value = SingleMessageState.Error(
                        message = result.message ?: "Unknown error",
                        code = result.code
                    )
                    Log.e("chat_send_message", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> {
                    _singleMessageState.value = SingleMessageState.Loading()
                    _sendState.value = SendState.Loading()
                }
                is Resource.Success -> {
                    Log.i("send", result.data.toString())
                    _sendState.value = SendState.Success()

                    _singleMessageState.value = SingleMessageState.Success(data = result.data!!)

                    listState.add(0, result.data)
                }
            }
        }.launchIn(viewModelScope)


    }

    fun sendTimer(){
        viewModelScope.launch {
            val image = async(Dispatchers.Default) {
                toByteArray()
            }.await()

            sendMessageUseCase(text = "", replyToId = "", chatId = id.value,  image = image).collect{ result ->
                when (result) {
                    is Resource.Error -> {
                        _singleMessageState.value = SingleMessageState.Error(
                            message = result.message ?: "Unknown error",
                            code = result.code
                        )
                        Log.e("timer_send", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> {
                        _singleMessageState.value = SingleMessageState.Loading()
                        _sendState.value = SendState.Loading()
                    }
                    is Resource.Success -> {
                        Log.i("send", result.data.toString())
                        _sendState.value = SendState.Success()

                        _singleMessageState.value = SingleMessageState.Success(data = result.data!!)

                        listState.add(0, result.data)
                    }
                }
            }
        }
    }

    fun deleteMessage(messageId: String){
        deleteMessageUseCase(messageId).onEach {result ->
            when(result){
                is Resource.Error -> {Log.e("delete", result.code.toString() + " " + result.message)}
                is Resource.Loading -> {
                    _sendState.value = SendState.Loading()
                }
                is Resource.Success -> {

                    listState.forEachIndexed {index,  item->
                        if (item.messageId == messageId){
                            listState[index] = item.copy(deleted = true)
                        }
                    }
                    _sendState.value = SendState.Success()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getChatMessages(){

        if (_authorized.value){
            if (!lastMessages.value){
                getListOfMessagesUseCase(id.value, messageId = lastMessageId.value).onEach {result ->
                    when(result){
                        is Resource.Error -> {
                            _messagesState.value = MessagesState.Error(
                                message = result.message ?: "Unknown error",
                                code = result.code
                            )
                            Log.e("chat_get_messages", result.code.toString() + " " + result.message)
                        }
                        is Resource.Loading -> _messagesState.value = MessagesState.Loading()
                        is Resource.Success -> {
                            _messagesState.value = MessagesState.Success(data = result.data!!)
                            if (result.data.size < 10){
                                lastMessages.value = true
                                Log.i("last", lastMessages.value.toString())
                            }
                            if (result.data.isNotEmpty()) listState.addAll(result.data.reversed())
                            lastMessageId.value = if (result.data.isNotEmpty()) result.data.first().messageId else lastMessageId.value
                            Log.i("list", listState.toString())
                        }
                    }
                }.launchIn(viewModelScope)
            }
        } else {
            if (!lastMessages.value){
                getListOfMessagesUnregisteredUseCase(messageId = lastMessageId.value).onEach {result ->
                    when(result){
                        is Resource.Error -> {
                            _messagesState.value = MessagesState.Error(
                                message = result.message ?: "Unknown error",
                                code = result.code
                            )
                            Log.e("chat_get_messages_unreg", result.code.toString() + " " + result.message)
                        }
                        is Resource.Loading -> _messagesState.value = MessagesState.Loading()
                        is Resource.Success -> {
                            _messagesState.value = MessagesState.Success(data = result.data!!)
                            if (result.data.size < 10){
                                lastMessages.value = true
                                Log.i("last", lastMessages.value.toString())
                            }
                            if (result.data.isNotEmpty()) listState.addAll(result.data.reversed())
                            lastMessageId.value = if (result.data.isNotEmpty()) result.data.first().messageId else lastMessageId.value
                            Log.i("list", listState.toString())
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }



    }

    private fun getSoldierData(){
        if (_authorized.value){
            getTimerDataUseCase().onEach { result ->
                when(result){
                    is Resource.Error -> {
                        Log.e("get_timer_e", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val instantStart = Instant.ofEpochMilli(result.data!!.startTime)
                        val instantEnd = Instant.ofEpochMilli(result.data.endTime)

                        val localDateTimeStart = LocalDateTime.ofInstant(instantStart, ZoneId.systemDefault())
                        val localDateTimeEnd = LocalDateTime.ofInstant(instantEnd, ZoneId.systemDefault())
                        if (localDateTimeStart != _state.value.dateStart || localDateTimeEnd != _state.value.dateEnd){
                            _state.value = _state.value.copy(
                                dateStart = localDateTimeStart,
                                dateEnd = localDateTimeEnd
                            )
                        }
                        countDate(state.value.dateEnd!!, state.value.dateStart!!)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    private fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime){
        if (_authorized.value){
            val currentDate = LocalDateTime.now()
            val durationLeft = Duration.between(currentDate, dateEnd)

            val durationPast = Duration.between(dateStart, currentDate)

            val daysPast = durationPast.toDays()
            val hoursPast = durationPast.toHours() % 24
            val minutesPast = durationPast.toMinutes() % 60
            val secondsPast = durationPast.seconds % 60

            val daysLeft = durationLeft.toDays()
            val hoursLeft = durationLeft.toHours() % 24
            val minutesLeft = durationLeft.toMinutes() % 60
            val secondsLeft = durationLeft.seconds % 60

            val duration = Duration.between(dateStart, dateEnd)

            val percentage = (durationPast.seconds.toDouble() / duration.seconds * 100).toString()
            val percentageString = ((if (percentage.length > 9) percentage.take(9) else percentage) + "%").replace(".", ",")

            _state.value = _state.value.copy(
                daysLeft = daysLeft,
                hoursLeft = hoursLeft,
                minutesLeft = minutesLeft,
                secondsLeft = secondsLeft,
                daysPast = daysPast,
                hoursPast = hoursPast,
                minutesPast = minutesPast,
                secondsPast = secondsPast,
                percentage = percentageString,
                percentageDouble = percentage.toDouble()
            )
        }
    }

    private fun toByteArray(): ByteArray{
        val stream = ByteArrayOutputStream()
        _shareImageBitmap.value?.asAndroidBitmap()?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        stream.close()
        return image
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}

sealed class MessagesState(val data: List<MessageEntity>? = null, val message: String? = null, val code: Int? = null){
    class Success(data: List<MessageEntity>) : MessagesState(data)
    class Error(message: String, code: Int?) : MessagesState(message = message, code = code)
    class Loading : MessagesState()
}

sealed class SendState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success : SendState()
    class Error(message: String, code: Int?) : SendState(message = message, code = code)
    class Loading : SendState()
}

sealed class SingleMessageState(val data: MessageEntity? = null, val message: String? = null, val code: Int? = null){
    class Success(data: MessageEntity) : SingleMessageState(data)
    class Error(message: String, code: Int?) : SingleMessageState(message = message, code = code)
    class Loading : SingleMessageState()
}