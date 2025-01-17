package com.application.timer_dmb.domain.usecases.messages

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.dto.toMessageEntity
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.receive.MessageEntity
import com.application.timer_dmb.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(chatId: String, text: String, replyToId: String, image: ByteArray? = null) : Flow<Resource<MessageEntity>> = flow {
        emit(Resource.Loading())
        val response = messageRepository.sendMessage(chatId, text, replyToId, image)
        if (response is ApiResponse.Success) emit(Resource.Success(data = response.data!!.toMessageEntity()))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}