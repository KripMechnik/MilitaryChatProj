package com.application.militarychatproject.domain.entity.receive

data class ChatEntity(
    val id: String,
    val imageLink: String?,
    val chatType: String,
    val isOnline: String,
    val lastMessageCreationTime: String?,
    val lastMessageSenderName: String?,
    val lastMessageText: String?,
    val name: String,
    val unreadMessagesAmount: String?
)
