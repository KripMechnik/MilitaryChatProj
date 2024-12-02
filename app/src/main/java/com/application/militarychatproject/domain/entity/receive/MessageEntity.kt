package com.application.militarychatproject.domain.entity.receive

data class MessageEntity(
    val attachmentLinks: List<String>,
    val chatId: String,
    val creationDate: String,
    val creationTime: String,
    val id: String,
    val isEdited: String,
    val isRead: String,
    val isSender: String,
    val senderAvatarLink: String,
    val senderId: String,
    val senderName: String,
    val text: String
)
