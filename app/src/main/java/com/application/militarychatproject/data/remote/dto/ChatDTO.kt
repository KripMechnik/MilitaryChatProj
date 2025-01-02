package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.ChatEntity
import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO(
    val chatId: String,
    val imageLink: String?,
    val chatType: String,
    val isOnline: String,
    val lastMessageCreationTime: String?,
    val lastMessageSenderName: String?,
    val lastMessageText: String?,
    val name: String,
    val unreadMessagesAmount: String?
)

fun ChatDTO.toChatEntity() = ChatEntity (
    id = this.chatId,
    imageLink = this.imageLink,
    chatType = this.chatType,
    isOnline = this.isOnline,
    lastMessageCreationTime = this.lastMessageCreationTime,
    lastMessageSenderName = this.lastMessageSenderName,
    lastMessageText = this.lastMessageText,
    name = this.name,
    unreadMessagesAmount = this.unreadMessagesAmount
)