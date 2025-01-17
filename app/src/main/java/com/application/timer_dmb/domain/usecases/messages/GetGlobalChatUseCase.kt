package com.application.timer_dmb.domain.usecases.messages

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.dto.toChatEntity
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.receive.ChatEntity
import com.application.timer_dmb.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGlobalChatUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke() : Flow<Resource<ChatEntity>> = flow {
        emit(Resource.Loading())
        val response = messageRepository.getGlobalChat()
        if (response is ApiResponse.Success) emit(Resource.Success(data = response.data!!.toChatEntity()))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}