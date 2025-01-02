package com.application.militarychatproject.data.calendar_database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.militarychatproject.domain.entity.receive.EventEntity

@Entity
data class Event(
    val timeMillis: String,
    val title: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)

fun Event.toEventEntity() = EventEntity(
    id = this.id.toString(),
    title = this.title,
    timeMillis = this.timeMillis
)