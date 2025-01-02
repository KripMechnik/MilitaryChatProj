package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.common.Resource
import kotlinx.coroutines.flow.Flow


interface WebSocketRepository {

    fun listen() : Flow<Resource<String>>

    suspend fun close()

}