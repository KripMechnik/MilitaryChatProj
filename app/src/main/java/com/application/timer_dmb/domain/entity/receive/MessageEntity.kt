package com.application.timer_dmb.domain.entity.receive

import kotlinx.serialization.Serializable

@Serializable
data class MessageEntity(
    val attachmentLinks: List<String>,
    val chatId: String,
    val creationDate: String,
    val creationTime: String,
    val messageId: String,
    var isEdited: Boolean,
    val isRead: Boolean,
    val isSender: Boolean,
    val senderAvatarLink: String?,
    val senderId: String,
    val senderNickname: String,
    var text: String,
    val repliedMessageId: String?,
    val repliedMessageText: String?,
    val repliedMessageSender: String?,
    var isLastInRow: Boolean = false,
    var deleted: Boolean = false
)
