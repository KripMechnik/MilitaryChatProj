package com.application.timer_dmb.data.repository

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.WebSocketRequests
import com.application.timer_dmb.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketRepoImpl @Inject constructor (
    private val webSocketRequests: WebSocketRequests
) : WebSocketRepository {
    override fun listen(authorized: Boolean) : Flow<Resource<String>> {
        return webSocketRequests.listen(authorized)
    }

    override suspend fun close(){
        webSocketRequests.close()
    }
}