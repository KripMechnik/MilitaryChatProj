package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.WebSocketRequests
import com.application.militarychatproject.data.remote.dto.DeletedMessageWebSocketDTO
import com.application.militarychatproject.data.remote.dto.EditedMessageWebSocketDTO
import com.application.militarychatproject.data.remote.dto.NewMessageWebSocketDTO
import com.application.militarychatproject.data.remote.dto.ReadMessageWebSocketDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.repository.WebSocketRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketRequestsImpl @Inject constructor (
    private val webSocketRequests: WebSocketRequests
) : WebSocketRepository {
    override fun listenToDeletedMessage(url: String) : Flow<ApiResponse<DeletedMessageWebSocketDTO>> {
        TODO("Not yet implemented")
    }

    override fun listenToNewMessages(url: String) : Flow<ApiResponse<NewMessageWebSocketDTO>> {
        TODO("Not yet implemented")
    }

    override fun listenToReadMessages(url: String) : Flow<ApiResponse<ReadMessageWebSocketDTO>> {
        TODO("Not yet implemented")
    }

    override fun listenToEditedMessages(url: String) : Flow<ApiResponse<EditedMessageWebSocketDTO>> {
        TODO("Not yet implemented")
    }
}