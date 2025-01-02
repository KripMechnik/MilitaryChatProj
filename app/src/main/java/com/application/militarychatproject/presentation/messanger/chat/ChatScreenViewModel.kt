package com.application.militarychatproject.presentation.messanger.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.domain.entity.receive.DeletedMessageWebSocketEntity
import com.application.militarychatproject.domain.entity.receive.EditedMessageWebSocketEntity
import com.application.militarychatproject.domain.entity.receive.MessageEntity
import com.application.militarychatproject.domain.entity.receive.NewMessageWebSocketEntity
import com.application.militarychatproject.domain.entity.send.UpdatedMessageEntity
import com.application.militarychatproject.domain.usecases.messages.DeleteMessageUseCase
import com.application.militarychatproject.domain.usecases.messages.GetListOfMessagesUseCase
import com.application.militarychatproject.domain.usecases.messages.ReadMessageUseCase
import com.application.militarychatproject.domain.usecases.messages.SendMessageUseCase
import com.application.militarychatproject.domain.usecases.messages.UpdateMessageUseCase
import com.application.militarychatproject.domain.usecases.web_socket.CloseSessionUseCase
import com.application.militarychatproject.domain.usecases.web_socket.ListenToSocketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val getListOfMessagesUseCase: GetListOfMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val listenToSocketUseCase: ListenToSocketUseCase,
    private val readMessageUseCase: ReadMessageUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val closeSessionUseCase: CloseSessionUseCase,
    private val json: Json,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val id = savedStateHandle.getStateFlow("id", "")

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
        listenToSocket()
        getChatMessages()
        readMessages()
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
        readMessageUseCase(id.value).onEach {result ->
            when(result){
                is Resource.Error -> {Log.e("read", result.code.toString() + " " + result.message)}
                is Resource.Loading -> {}
                is Resource.Success -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun listenToSocket(){
        listenToSocketUseCase().onEach { result ->
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

    fun setReplyToId(newId: String){
        _replyToId.value = newId
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