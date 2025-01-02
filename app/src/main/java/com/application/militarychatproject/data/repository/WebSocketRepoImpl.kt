package com.application.militarychatproject.data.repository

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.WebSocketRequests
import com.application.militarychatproject.data.remote.dto.DeletedMessageWebSocketDTO
import com.application.militarychatproject.data.remote.dto.EditedMessageWebSocketDTO
import com.application.militarychatproject.data.remote.dto.NewMessageWebSocketDTO
import com.application.militarychatproject.data.remote.dto.ReadMessageWebSocketDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketRepoImpl @Inject constructor (
    private val webSocketRequests: WebSocketRequests
) : WebSocketRepository {
    override fun listen() : Flow<Resource<String>> {
        return webSocketRequests.listen()
    }

    override suspend fun close(){
        webSocketRequests.close()
    }
}