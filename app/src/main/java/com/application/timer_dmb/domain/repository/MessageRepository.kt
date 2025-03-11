package com.application.timer_dmb.domain.repository

import com.application.timer_dmb.data.remote.dto.ChatDTO
import com.application.timer_dmb.data.remote.dto.MessageDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.UpdatedMessageEntity

interface MessageRepository {

    suspend fun getGlobalChat(): ApiResponse<ChatDTO>

    suspend fun deleteChat(chatId: String): ApiResponse<Unit>

    suspend fun deleteMessage(messageId: String): ApiResponse<Unit>

    suspend fun getListOfChats(): ApiResponse<List<ChatDTO>>

    suspend fun getListOfMessages(chatId: String, messageId: String): ApiResponse<List<MessageDTO>>

    suspend fun readMessage(chatId: String): ApiResponse<Unit>

    suspend fun sendMessage(chatId: String, text: String, replyToId: String, byteArray: ByteArray? = null): ApiResponse<MessageDTO>

    suspend fun updateMessage(messageId: String, updatedMessage: UpdatedMessageEntity): ApiResponse<Unit>

    suspend fun getListOfMessagesUnregistered(messageId: String) : ApiResponse<List<MessageDTO>>

    suspend fun sendFCMToken(token: String) : ApiResponse<Unit>

}