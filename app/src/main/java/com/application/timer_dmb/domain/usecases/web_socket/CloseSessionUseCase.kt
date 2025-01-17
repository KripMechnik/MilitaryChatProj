package com.application.timer_dmb.domain.usecases.web_socket

import com.application.timer_dmb.domain.repository.WebSocketRepository
import javax.inject.Inject

class CloseSessionUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke(){
        webSocketRepository.close()
    }
}