package com.application.timer_dmb.data.calendar_database

import androidx.annotation.Keep
import androidx.room.Database
import androidx.room.RoomDatabase

@Keep
@Database(
    entities = [Event::class],
    version = 1
)
abstract class CalendarDatabase : RoomDatabase() {

    abstract val dao: EventDao

}