package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.SenderEntity
import kotlinx.serialization.Serializable

@Serializable
data class SenderDTO(
    val id: String,
    val senderAvatarLink: String?,
    val senderId: String,
    val senderName: String,
    val senderNickname: String?
)

fun SenderDTO.toSenderEntity() = SenderEntity(
    id = this.id,
    senderAvatarLink = this.senderAvatarLink,
    senderId = this.senderId,
    senderName = this.senderName,
    senderNickname = this.senderNickname
)