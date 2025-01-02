package com.application.militarychatproject.data.remote

import com.application.militarychatproject.data.remote.dto.ChatDTO
import com.application.militarychatproject.data.remote.dto.MessageDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.data.remote.network.AuthRequest
import com.application.militarychatproject.data.remote.network.BaseRequest
import com.application.militarychatproject.domain.entity.send.UpdatedMessageEntity
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.util.StringValues
import javax.inject.Inject

class MessageRequests @Inject constructor(
    private val authRequest: AuthRequest,
    private val baseRequest: BaseRequest
){

    private val basePath = "/messenger/"

    //token -> yes
    suspend fun deleteChatRequest(
        chatId: String
    ) : ApiResponse<Unit> {
        return authRequest(
            path = basePath + "delete-chat/",
            params = StringValues.build {
                append("chatId", chatId)
            },
            method = HttpMethod.Delete
        )
    }

    //token -> no
    suspend fun getGlobalChatRequest() : ApiResponse<ChatDTO> {
        return authRequest(
            path = basePath + "global-chat",
            method = HttpMethod.Get
        )
    }

    //token -> yes
    suspend fun deleteMessageRequest(
        messageId: String
    ) : ApiResponse<Unit>{
        return authRequest(
            path = basePath + "delete-message/",
            query = messageId,
            method = HttpMethod.Delete
        )
    }

    //token -> yes
    suspend fun getListOfChatsRequest() : ApiResponse<List<ChatDTO>>{
        return authRequest(
            path = basePath + "chats",
            method = HttpMethod.Get
        )
    }

    //token -> yes
    suspend fun getListOfMessagesRequest(
        chatId: String,
        messageId: String
    ) : ApiResponse<List<MessageDTO>>{
        return authRequest(
            path = basePath + "messages/",
            query = chatId,
            params = StringValues.build {
                append("latestMessageId", messageId)
            },
            method = HttpMethod.Get
        )
    }

    //token -> yes
    suspend fun readMessageRequest(
        chatId: String
    ) : ApiResponse<Unit>{
        return authRequest(
            path = basePath + "update-all-unread-messages/",
            query = chatId,
            method = HttpMethod.Post
        )
    }

    //token -> yes
    suspend fun sendMessageRequest(
        chatId: String,
        text: String,
        replyToId: String
    ) : ApiResponse<MessageDTO>{
        return authRequest(
            path = basePath + "create/",
            query = chatId,
            method = HttpMethod.Post,
            body = MultiPartFormDataContent(
                formData {
                    append("data", text)
                    append("replyToId", replyToId)
                }
            ),
            formData = true
        )
    }

    //token -> yes
    suspend fun updateMessageRequest(
        messageId: String,
        updatedMessage: UpdatedMessageEntity
    ) : ApiResponse<Unit>{
        return authRequest(
            path = basePath + "edit-message/",
            query = messageId,
            method = HttpMethod.Put,
            body = updatedMessage
        )
    }
}