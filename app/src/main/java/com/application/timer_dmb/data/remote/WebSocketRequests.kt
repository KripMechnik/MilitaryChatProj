package com.application.timer_dmb.data.remote

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.network.WebSocketClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketRequests @Inject constructor(
    private val client: WebSocketClient
) {
    fun listen(authorized: Boolean) : Flow<Resource<String>> {
        return client.listenToSocket(authorized)
    }

    suspend fun close(): Boolean {
        return client.close()
    }
}