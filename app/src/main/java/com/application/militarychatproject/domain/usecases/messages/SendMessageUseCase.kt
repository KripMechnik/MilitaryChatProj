package com.application.militarychatproject.domain.usecases.messages

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.dto.toChatEntity
import com.application.militarychatproject.data.remote.dto.toMessageEntity
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.receive.MessageEntity
import com.application.militarychatproject.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(chatId: String, text: String, replyToId: String) : Flow<Resource<MessageEntity>> = flow {
        emit(Resource.Loading())
        val response = messageRepository.sendMessage(chatId, text, replyToId)
        if (response is ApiResponse.Success) emit(Resource.Success(data = response.data!!.toMessageEntity()))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}