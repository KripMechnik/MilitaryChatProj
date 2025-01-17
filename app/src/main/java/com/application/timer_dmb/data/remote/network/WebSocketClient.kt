package com.application.timer_dmb.data.remote.network

import android.util.Log
import com.application.timer_dmb.common.Constants.BASE_WEBSOCKET_HOST
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.common.UserData
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class WebSocketClient @Inject constructor(
    @Named("WebSocketClient") val client: HttpClient,
    private val userData: UserData
) {

    private var session: WebSocketSession? = null

    fun listenToSocket(authorized: Boolean) : Flow<Resource<String>> {

        return callbackFlow {
            try {
                if (session != null){
                    val closing = launch {
                        session?.close()
                        session = null
                    }
                    closing.join()
                }
                session = client.webSocketSession(
                    urlString = BASE_WEBSOCKET_HOST + if (authorized) "?token=Bearer " + userData.token.value.accessToken else ""
                )


                Log.i("web_socket_session", session?.let {
                    isActive.toString()
                } ?: "Unknown session")

            } catch (e: Exception){
                if (e is CancellationException) throw e
                e.printStackTrace()
            }


            session?.let { session ->
                session
                    .incoming
                    .consumeAsFlow()
                    .collect { frame ->
                        val string = (frame as Frame.Text).readText()
                        Log.i("webSocket_client", string)
                        try {
                            send(Resource.Success(string))
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            else {
                                e.printStackTrace()
                                send(Resource.Error(code = -1, message = "Serialization error"))
                            }
                        }

                    }
            } ?: run {
                Log.i("webSocket_client", "No_session")
                session?.close()
                session = null
                close()
            }

            awaitClose {
                launch(NonCancellable) {
                    session?.close()
                    Log.i("webSocket_session_Await", "closed")
                    session = null
                }
            }
        }
    }

    suspend fun close(){
        session?.close()
        session = null
        Log.i("webSocket_session", "closed")
    }
}