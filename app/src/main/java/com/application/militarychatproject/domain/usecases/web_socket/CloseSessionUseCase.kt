package com.application.militarychatproject.domain.usecases.web_socket

import com.application.militarychatproject.domain.repository.WebSocketRepository
import javax.inject.Inject

class CloseSessionUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke(){
        webSocketRepository.close()
    }
}