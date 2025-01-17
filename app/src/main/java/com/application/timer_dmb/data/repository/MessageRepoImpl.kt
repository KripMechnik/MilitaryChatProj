package com.application.timer_dmb.data.repository

import com.application.timer_dmb.data.remote.MessageRequests
import com.application.timer_dmb.data.remote.dto.ChatDTO
import com.application.timer_dmb.data.remote.dto.MessageDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.UpdatedMessageEntity
import com.application.timer_dmb.domain.repository.MessageRepository
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

    override suspend fun sendMessage(chatId: String, text: String, replyToId: String, byteArray: ByteArray?): ApiResponse<MessageDTO> {
        return messageRequests.sendMessageRequest(chatId, text, replyToId, byteArray)
    }

    override suspend fun updateMessage(messageId: String, updatedMessage: UpdatedMessageEntity): ApiResponse<Unit> {
        return messageRequests.updateMessageRequest(messageId, updatedMessage)
    }

    override suspend fun getListOfMessagesUnregistered(messageId: String): ApiResponse<List<MessageDTO>> {
        return messageRequests.getListOfMessagesUnregistered(messageId)
    }
}