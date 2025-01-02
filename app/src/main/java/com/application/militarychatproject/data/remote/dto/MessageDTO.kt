package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.MessageEntity
import kotlinx.serialization.Serializable

@Serializable
data class MessageDTO(
    val attachmentLinks: List<String>,
    val chatId: String,
    val creationDate: String,
    val creationTime: String,
    val messageId: String,
    val isEdited: Boolean,
    val isRead: Boolean,
    val isSender: Boolean,
    val senderAvatarLink: String?,
    val senderId: String,
    val senderNickname: String,
    val text: String,
    val repliedMessageId: String?,
    val repliedMessageText: String?,
    val repliedMessageSender: String?
)

fun MessageDTO.toMessageEntity(): MessageEntity = MessageEntity(
    messageId = this.messageId,
    chatId = this.chatId,
    creationDate = this.creationDate,
    attachmentLinks = this.attachmentLinks,
    creationTime = this.creationTime,
    isEdited = this.isEdited,
    isRead = this.isRead,
    isSender = this.isSender,
    senderAvatarLink = this.senderAvatarLink,
    senderId = this.senderId,
    senderNickname = this.senderNickname,
    text = this.text,
    repliedMessageId = this.repliedMessageId,
    repliedMessageText = this.repliedMessageText,
    repliedMessageSender = this.repliedMessageSender
)