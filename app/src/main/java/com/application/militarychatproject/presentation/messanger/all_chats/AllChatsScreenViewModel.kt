package com.application.militarychatproject.presentation.messanger.all_chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.domain.entity.receive.ChatEntity
import com.application.militarychatproject.domain.entity.receive.DeletedMessageWebSocketEntity
import com.application.militarychatproject.domain.entity.receive.EditedMessageWebSocketEntity
import com.application.militarychatproject.domain.entity.receive.NewMessageWebSocketEntity
import com.application.militarychatproject.domain.usecases.messages.GetGlobalChatUseCase
import com.application.militarychatproject.domain.usecases.web_socket.CloseSessionUseCase
import com.application.militarychatproject.domain.usecases.web_socket.ListenToSocketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class AllChatsViewModel @Inject constructor(
    private val getGlobalChatUseCase: GetGlobalChatUseCase,
    private val listenToSocketUseCase: ListenToSocketUseCase,
    private val closeSessionUseCase: CloseSessionUseCase,
    private val json: Json,
) : ViewModel() {

    private val _chatsState = MutableStateFlow<ChatsState?>(null)
    val chatsState = _chatsState.asStateFlow()

    private val _clearedState = MutableStateFlow(false)
    val cleared = _clearedState.asStateFlow()

    private var listening: Job? = null

    val scope = CoroutineScope(SupervisorJob())

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

        listenToSocketUseCase().onEach{ result ->
            Log.i("web_socket_allChats", result.data ?: "Empty")
            try {
                val resp = json.decodeFromString<NewMessageWebSocketEntity>(result.data!!)
                val unreadMessages = (_chatsState.value?.data?.unreadMessagesAmount ?: "0").toInt() + 1
                if (_chatsState.value is ChatsState.Success){
                    _chatsState.value = ChatsState.Success(_chatsState.value!!.data!!.copy(unreadMessagesAmount = unreadMessages.toString()))
                }


            } catch (_: Exception){
            }
        }.launchIn(viewModelScope)


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
}