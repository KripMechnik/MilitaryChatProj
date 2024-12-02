package com.application.militarychatproject.data.remote.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.serialization.deserialize
import io.ktor.websocket.close
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class WebSocketClient @Inject constructor(
    @Named("WebSocketClient") val client: HttpClient
) {

    var session: DefaultClientWebSocketSession? = null

    inline fun <reified T> listenToSocket(url: String) : Flow<ApiResponse<T>> {

        return callbackFlow {
            session = client.webSocketSession(
                urlString = url
            )

            session?.let { session ->
                session
                    .incoming
                    .consumeAsFlow()
                    .collect { frame ->
                        try {
                            val content = session.converter?.deserialize<T>(content = frame)
                            send(ApiResponse.Success(content))
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            else {
                                e.printStackTrace()
                                send(ApiResponse.Error(errorCode = -1, errorMessage = "Serialization error"))
                            }
                        }

                    }
            } ?: run {
                session?.close()
                session = null
                close()
            }

            awaitClose {
                launch(NonCancellable) {
                    session?.close()
                    session = null
                }
            }
        }
    }
}