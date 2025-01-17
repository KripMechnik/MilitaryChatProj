package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.FriendEntity
import kotlinx.serialization.Serializable

@Serializable
data class FriendDTO(
    val avatarLink: String? = null,
    val id: String,
    val name: String,
    val nickname: String
)

fun FriendDTO.toFriendEntity(): FriendEntity = FriendEntity(
    id = this.id,
    avatarLink = this.avatarLink,
    nickname = this.nickname,
    name = this.name
)