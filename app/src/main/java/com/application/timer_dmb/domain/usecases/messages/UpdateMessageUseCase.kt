package com.application.timer_dmb.domain.usecases.messages

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.UpdatedMessageEntity
import com.application.timer_dmb.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
){
    operator fun invoke(messageId: String, updatedMessage: UpdatedMessageEntity) : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = messageRepository.updateMessage(messageId = messageId, updatedMessage = updatedMessage)
        if (response is ApiResponse.Success) emit(Resource.Success(data = Unit))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}