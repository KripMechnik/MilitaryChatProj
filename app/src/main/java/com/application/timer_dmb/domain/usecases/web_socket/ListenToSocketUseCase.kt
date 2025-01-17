package com.application.timer_dmb.domain.usecases.web_socket

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenToSocketUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(authorized: Boolean) : Flow<Resource<String>>{
        return webSocketRepository.listen(authorized)
    }
}

