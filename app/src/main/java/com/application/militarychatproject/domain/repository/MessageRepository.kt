package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.data.remote.dto.ChatDTO
import com.application.militarychatproject.data.remote.dto.MessageDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.UpdatedMessageEntity

interface MessageRepository {

    suspend fun deleteChat(chatId: String): ApiResponse<Any>

    suspend fun deleteMessage(messageId: String): ApiResponse<Any>

    suspend fun getListOfChats(): ApiResponse<List<ChatDTO>>

    suspend fun getListOfMessages(chatId: String): ApiResponse<List<MessageDTO>>

    suspend fun readMessage(chatId: String): ApiResponse<Any>

    suspend fun sendMessage(chatId: String): ApiResponse<MessageDTO>

    suspend fun updateMessage(messageId: String, updatedMessage: UpdatedMessageEntity): ApiResponse<Any>

}