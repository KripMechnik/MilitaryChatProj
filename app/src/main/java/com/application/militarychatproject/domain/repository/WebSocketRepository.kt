package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.data.remote.dto.DeletedMessageWebSocketDTO
import com.application.militarychatproject.data.remote.dto.EditedMessageWebSocketDTO
import com.application.militarychatproject.data.remote.dto.NewMessageWebSocketDTO
import com.application.militarychatproject.data.remote.dto.ReadMessageDTO
import com.application.militarychatproject.data.remote.dto.ReadMessageWebSocketDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import kotlinx.coroutines.flow.Flow


interface WebSocketRepository {

    fun listenToDeletedMessage(url: String) : Flow<ApiResponse<DeletedMessageWebSocketDTO>>

    fun listenToNewMessages(url: String) : Flow<ApiResponse<NewMessageWebSocketDTO>>

    fun listenToReadMessages(url: String) : Flow<ApiResponse<ReadMessageWebSocketDTO>>

    fun listenToEditedMessages(url: String) : Flow<ApiResponse<EditedMessageWebSocketDTO>>

}