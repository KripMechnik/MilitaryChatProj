package com.application.militarychatproject.domain.usecases.messages

import android.util.Log
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.dto.toMessageEntity
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.receive.MessageEntity
import com.application.militarychatproject.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetListOfMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
){
    operator fun invoke(chatId: String, messageId: String) : Flow<Resource<List<MessageEntity>>> = flow {
        Log.i("last_id", messageId)
        emit(Resource.Loading())
        val response = messageRepository.getListOfMessages(chatId, messageId)
        if (response is ApiResponse.Success) {
            response.data?.let { data ->
                val resList = data.map { it.toMessageEntity() }
                if (resList.isNotEmpty()){
                    for (i in 1..< resList.size){
                        if (resList[i].senderId != resList[i - 1].senderId){
                            resList[i - 1].isLastInRow = true
                        }
                    }
                    resList[resList.size - 1].isLastInRow = true
                }
                emit(Resource.Success(data = resList))
            } ?: emit(Resource.Success(data = emptyList()))
        }
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}