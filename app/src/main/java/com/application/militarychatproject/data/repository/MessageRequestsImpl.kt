package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.MessageRequests
import com.application.militarychatproject.data.remote.dto.ChatDTO
import com.application.militarychatproject.data.remote.dto.MessageDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.UpdatedMessageEntity
import com.application.militarychatproject.domain.repository.MessageRepository
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MessageRequestsImpl @Inject constructor (
    private val messageRequests: MessageRequests
) : MessageRepository {
    override suspend fun deleteChat(chatId: String): ApiResponse<Any> {
        return messageRequests.deleteChatRequest(chatId)
    }

    override suspend fun deleteMessage(messageId: String): ApiResponse<Any> {
        return messageRequests.deleteMessageRequest(messageId)
    }

    override suspend fun getListOfChats(): ApiResponse<List<ChatDTO>> {
        return messageRequests.getListOfChatsRequest()
    }

    override suspend fun getListOfMessages(chatId: String): ApiResponse<List<MessageDTO>> {
        return messageRequests.getListOfMessagesRequest(chatId)
    }

    override suspend fun readMessage(chatId: String): ApiResponse<Any> {
        return messageRequests.readMessageRequest(chatId)
    }

    override suspend fun sendMessage(chatId: String): ApiResponse<MessageDTO> {
        return messageRequests.sendMessageRequest(chatId)
    }

    override suspend fun updateMessage(messageId: String, updatedMessage: UpdatedMessageEntity): ApiResponse<Any> {
        return messageRequests.updateMessageRequest(messageId, updatedMessage)
    }
}