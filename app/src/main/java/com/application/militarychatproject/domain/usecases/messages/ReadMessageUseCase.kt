package com.application.militarychatproject.domain.usecases.messages

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReadMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(chatId: String) : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = messageRepository.readMessage(chatId)
        if (response is ApiResponse.Success) emit(Resource.Success(Unit))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}