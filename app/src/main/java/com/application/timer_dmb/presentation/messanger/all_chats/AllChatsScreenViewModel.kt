package com.application.timer_dmb.presentation.messanger.all_chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.receive.ChatEntity
import com.application.timer_dmb.domain.entity.receive.NewMessageWebSocketEntity
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.timer_dmb.domain.usecases.messages.GetGlobalChatUseCase
import com.application.timer_dmb.domain.usecases.web_socket.ListenToSocketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class AllChatsViewModel @Inject constructor(
    private val getGlobalChatUseCase: GetGlobalChatUseCase,
    private val listenToSocketUseCase: ListenToSocketUseCase,
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
    private val json: Json,
) : ViewModel() {

    private val _chatsState = MutableStateFlow<ChatsState?>(null)
    val chatsState = _chatsState.asStateFlow()

    private val _clearedState = MutableStateFlow(false)
    val cleared = _clearedState.asStateFlow()

    init {
        getChats()
        listenToSocket()
    }

    fun getChats(){
        getGlobalChatUseCase().onEach {result ->
            when(result){
                is Resource.Error -> {
                    _chatsState.value = ChatsState.Error(
                        message = result.message ?: "Unknown error",
                        code = result.code
                    )
                    Log.e("profile", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _chatsState.value = ChatsState.Loading()
                is Resource.Success -> {
                    _chatsState.value = ChatsState.Success(data = result.data!!)
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun listenToSocket(){
        if (isAuthorizedUseCase()){
            listenToSocketUseCase(true).onEach{ result ->
                Log.i("web_socket_allChats", result.data ?: "Empty")
                try {
                    val resp = json.decodeFromString<NewMessageWebSocketEntity>(result.data!!)
                    val unreadMessages = (_chatsState.value?.data?.unreadMessagesAmount ?: "0").toInt() + 1
                    if (_chatsState.value is ChatsState.Success){
                        _chatsState.value = ChatsState.Success(_chatsState.value!!.data!!.copy(
                            unreadMessagesAmount = unreadMessages.toString(),
                            lastMessageCreationTime = resp.data.creationDate,
                            lastMessageSenderName = resp.data.senderNickname,
                            lastMessageText = resp.data.text
                            ))
                    }


                } catch (_: Exception){
                }
            }.launchIn(viewModelScope)
        } else {
            listenToSocketUseCase(false).onEach{ result ->
                Log.i("web_socket_allChats", result.data ?: "Empty")
                try {
                    val resp = json.decodeFromString<NewMessageWebSocketEntity>(result.data!!)
                    val unreadMessages = (_chatsState.value?.data?.unreadMessagesAmount ?: "0").toInt() + 1
                    if (_chatsState.value is ChatsState.Success){
                        _chatsState.value = ChatsState.Success(_chatsState.value!!.data!!.copy(
                            unreadMessagesAmount = unreadMessages.toString(),
                            lastMessageCreationTime = resp.data.creationDate,
                            lastMessageSenderName = resp.data.senderNickname,
                            lastMessageText = resp.data.text
                        ))
                    }


                } catch (_: Exception){
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onNavigating(){
        viewModelScope.cancel()
        _clearedState.value = true
    }
}

sealed class ChatsState(val data: ChatEntity? = null, val message: String? = null, val code: Int? = null){
    class Success(data: ChatEntity) : ChatsState(data)
    class Error(message: String, code: Int?) : ChatsState(message = message, code = code)
    class Loading : ChatsState()
    class Unauthorized : ChatsState()
}