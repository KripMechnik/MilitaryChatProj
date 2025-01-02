package com.application.militarychatproject.data.calendar_database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Event::class],
    version = 1
)
abstract class CalendarDatabase : RoomDatabase() {

    abstract val dao: EventDao

}