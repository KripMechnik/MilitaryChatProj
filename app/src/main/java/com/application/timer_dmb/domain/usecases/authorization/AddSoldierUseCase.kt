package com.application.timer_dmb.domain.usecases.authorization

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.application.timer_dmb.presentation.widget.DmbWidget
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AddSoldierUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

    suspend operator fun invoke(dateStart: String, dateEnd: String, name: String){
        val sharedPrefs = context.getSharedPreferences("dates", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        if (dateStart.isNotBlank() && dateEnd.isNotBlank()){
            val dateStartDateTime = LocalDateTime.parse("$dateStart 00:00:00", formatter)
            val dateEndDateTime = LocalDateTime.parse("$dateEnd 00:00:00", formatter)

            val result = countDate(dateEndDateTime, dateStartDateTime)

            editor.putString("start", dateStart)
            editor.putString("end", dateEnd)
            editor.putString("name", name)
            editor.putString("daysLeft", result[0].toString())
            editor.putString("percentage", result[1].toString())
            if (editor.commit()){
                val id = GlanceAppWidgetManager(context).getGlanceIds(DmbWidget::class.java).firstOrNull()

                id?.let{ currId ->
                    updateAppWidgetState(context, currId) { prefs ->
                        prefs[DmbWidget.percentageKey] = result[1].toString()
                        prefs[DmbWidget.daysLeftKey] = result[0].toString()
                    }
                    DmbWidget().update(context, id)
                }
            }
        }

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