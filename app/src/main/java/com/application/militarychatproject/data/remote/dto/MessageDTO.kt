package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.MessageEntity
import kotlinx.serialization.Serializable

@Serializable
data class MessageDTO(
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

fun MessageDTO.toMessageEntity(): MessageEntity = MessageEntity(
    id = this.id,
    chatId = this.chatId,
    creationDate = this.creationDate,
    attachmentLinks = this.attachmentLinks,
    creationTime = this.creationTime,
    isEdited = this.isEdited,
    isRead = this.isRead,
    isSender = this.isSender,
    senderAvatarLink = this.senderAvatarLink,
    senderId = this.senderId,
    senderName = this.senderName,
    text = this.text
)