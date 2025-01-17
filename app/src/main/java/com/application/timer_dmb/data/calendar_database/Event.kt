package com.application.timer_dmb.data.calendar_database

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.application.timer_dmb.domain.entity.receive.EventEntity

@Keep
@Entity
data class Event(
    var timeMillis: String,
    val title: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)

fun Event.toEventEntity() = EventEntity(
    id = this.id.toString(),
    title = this.title,
    timeMillis = this.timeMillis
)