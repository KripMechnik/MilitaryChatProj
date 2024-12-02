package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.ChatEntity
import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO(
    val id: String,
    val imageLink: String?,
    val isGroupChat: String,
    val isOnline: String,
    val lastMessageCreationTime: String?,
    val lastMessageSenderName: String?,
    val lastMessageText: String?,
    val name: String,
    val unreadMessagesAmount: String?
)

fun ChatDTO.toChatEntity() = ChatEntity (
    id = this.id,
    imageLink = this.imageLink,
    isGroupChat = this.isGroupChat,
    isOnline = this.isOnline,
    lastMessageCreationTime = this.lastMessageCreationTime,
    lastMessageSenderName = this.lastMessageSenderName,
    lastMessageText = this.lastMessageText,
    name = this.name,
    unreadMessagesAmount = this.unreadMessagesAmount
)