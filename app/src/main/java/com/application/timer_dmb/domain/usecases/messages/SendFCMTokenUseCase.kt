package com.application.timer_dmb.domain.usecases.messages

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendFCMTokenUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(token: String) : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = messageRepository.sendFCMToken(token)
        if (response is ApiResponse.Success) emit(Resource.Success(Unit))
        else if (response is ApiResponse.Error) emit (Resource.Error(message = response.errorMessage ?: "Unknown message", code = response.errorCode))
    }
}