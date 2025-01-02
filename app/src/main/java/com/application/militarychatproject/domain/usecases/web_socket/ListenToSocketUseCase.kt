package com.application.militarychatproject.domain.usecases.web_socket

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenToSocketUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke() : Flow<Resource<String>>{
        return webSocketRepository.listen()
    }
}

