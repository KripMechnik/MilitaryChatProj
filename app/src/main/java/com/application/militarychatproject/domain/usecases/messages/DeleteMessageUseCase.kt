package com.application.militarychatproject.domain.usecases.messages

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.UpdatedMessageEntity
import com.application.militarychatproject.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
){
    operator fun invoke(messageId: String) : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = messageRepository.deleteMessage(messageId = messageId)
        if (response is ApiResponse.Success) emit(Resource.Success(data = Unit))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}