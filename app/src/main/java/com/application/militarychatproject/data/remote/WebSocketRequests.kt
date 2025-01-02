package com.application.militarychatproject.data.remote

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.data.remote.network.WebSocketClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketRequests @Inject constructor(
    private val client: WebSocketClient
) {
    fun listen() : Flow<Resource<String>> {
        return client.listenToSocket()
    }

    suspend fun close() {
        client.close()
    }
}