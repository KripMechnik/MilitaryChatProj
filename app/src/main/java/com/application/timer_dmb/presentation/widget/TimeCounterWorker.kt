package com.application.timer_dmb.presentation.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@HiltWorker
class TimeCounterWorker @AssistedInject constructor (
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val sharedPreferences = context.getSharedPreferences("dates", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()


    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

    override suspend fun doWork(): Result {

        val dateStart = sharedPreferences.getString("start", "") + " 00:00:00"
        val dateEnd = sharedPreferences.getString("end", "") + " 00:00:00"

        val dateStartDateTime = LocalDateTime.parse(dateStart, formatter)
        val dateEndDateTime = LocalDateTime.parse(dateEnd, formatter)

        val id = GlanceAppWidgetManager(context).getGlanceIds(DmbWidget::class.java).firstOrNull()
            ?: return Result.failure()

        Log.i("worker", id.toString())

        val result = countDate(dateEndDateTime, dateStartDateTime)
        editor.putString("daysLeft", result[0].toString())
        editor.putString("percentage", result[1].toString())

        if (editor.commit()){
            id.let{ currId ->
                updateAppWidgetState(context, currId) { prefs ->
                    prefs[DmbWidget.percentageKey] = result[1].toString()
                    prefs[DmbWidget.daysLeftKey] = result[0].toString()
                }
                DmbWidget().update(context, id)
            }
        }
        return Result.success()
    }

    private fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime): List<Int>{
        val currentDate = if (LocalDateTime.now() < dateStart) dateStart else LocalDateTime.now()

        val durationLeft = Duration.between(currentDate, dateEnd)

        val durationPast = Duration.between(dateStart, currentDate)


        if (currentDate >= dateEnd){
            return listOf(0, 100)
        }

        val daysLeft = durationLeft.toDays().toInt()


        val duration = Duration.between(dateStart, dateEnd)

        val percentage = (durationPast.seconds.toDouble() / duration.seconds * 100).toInt()

        return listOf(daysLeft, percentage)
    }

}