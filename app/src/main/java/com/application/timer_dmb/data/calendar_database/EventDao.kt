package com.application.timer_dmb.data.calendar_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.application.timer_dmb.domain.repository.EventDaoRepository


@Dao
interface EventDao : EventDaoRepository {

    @Insert
    override suspend fun insertEvent(event: Event)

    @Update
    override suspend fun updateEvent(event: Event)

    @Delete
    override suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM event ORDER BY timeMillis")
    override suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM event WHERE id IN (5, 6, 7, 12)")
    override suspend fun getCalculatableEvents(): List<Event>

    @Query("DELETE FROM event")
    override suspend fun deleteAllEvents()

}