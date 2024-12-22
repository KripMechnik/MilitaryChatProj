package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.PhotoEntity
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDTO(
    val avatarLink: String
)

fun PhotoDTO.toPhotoEntity() = PhotoEntity(
    avatarLink = this.avatarLink
)