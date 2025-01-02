package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.MessageRequests
import com.application.militarychatproject.data.remote.dto.ChatDTO
import com.application.militarychatproject.data.remote.dto.MessageDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.UpdatedMessageEntity
import com.application.militarychatproject.domain.repository.MessageRepository
import javax.inject.Inject

class MessageRepoImpl @Inject constructor (
    private val messageRequests: MessageRequests
) : MessageRepository {
    override suspend fun getGlobalChat(): ApiResponse<ChatDTO> {
        return messageRequests.getGlobalChatRequest()
    }

    override suspend fun deleteChat(chatId: String): ApiResponse<Unit> {
        return messageRequests.deleteChatRequest(chatId)
    }

    override suspend fun deleteMessage(messageId: String): ApiResponse<Unit> {
        return messageRequests.deleteMessageRequest(messageId)
    }

    override suspend fun getListOfChats(): ApiResponse<List<ChatDTO>> {
        return messageRequests.getListOfChatsRequest()
    }

    override suspend fun getListOfMessages(chatId: String, messageId: String): ApiResponse<List<MessageDTO>> {
        return messageRequests.getListOfMessagesRequest(chatId, messageId)
    }

    override suspend fun readMessage(chatId: String): ApiResponse<Unit> {
        return messageRequests.readMessageRequest(chatId)
    }

    override suspend fun sendMessage(chatId: String, text: String, replyToId: String): ApiResponse<MessageDTO> {
        return messageRequests.sendMessageRequest(chatId, text, replyToId)
    }

    override suspend fun updateMessage(messageId: String, updatedMessage: UpdatedMessageEntity): ApiResponse<Unit> {
        return messageRequests.updateMessageRequest(messageId, updatedMessage)
    }
}