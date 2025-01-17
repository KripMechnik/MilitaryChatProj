package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.AcceptedFriendEntity
import kotlinx.serialization.Serializable


@Serializable
data class AcceptedFriendDTO(
    val id: String,
    val lastUpdateTime: String,
    val unreadMessagesAmount: String
)

fun AcceptedFriendDTO.toAcceptedFriendEntity() = AcceptedFriendEntity(
    id = this.id,
    lastUpdateTime = this.lastUpdateTime,
    unreadMessagesAmount = this.unreadMessagesAmount
)