package com.application.timer_dmb.domain.repository

import com.application.timer_dmb.common.Resource
import kotlinx.coroutines.flow.Flow


interface WebSocketRepository {

    fun listen(authorized: Boolean) : Flow<Resource<String>>

    suspend fun close(): Boolean

}