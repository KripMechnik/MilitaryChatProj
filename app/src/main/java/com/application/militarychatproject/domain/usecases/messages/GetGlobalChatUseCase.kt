package com.application.militarychatproject.domain.usecases.messages

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.dto.toChatEntity
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.receive.ChatEntity
import com.application.militarychatproject.domain.repository.MessageRepository
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