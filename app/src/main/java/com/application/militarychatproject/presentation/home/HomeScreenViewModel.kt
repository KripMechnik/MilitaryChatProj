package com.application.militarychatproject.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {





    private var _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> get() = _state

    private val sharedPrefs = context.getSharedPreferences("dates", Context.MODE_PRIVATE)

    init {
        getName()
        getDates()
    }

    private fun getName(){
        val name = sharedPrefs.getString("name", "")
        _state.value = HomeState(name = name)
    }

    private fun getDates(){
        val dateStart = sharedPrefs.getString("start", "") + " 00:00:00"
        val dateEnd = sharedPrefs.getString("end", "") + " 00:00:00"
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val dateEndAsDate = LocalDateTime.parse(dateEnd, formatter)
        val dateStartAsDate = LocalDateTime.parse(dateStart, formatter)
        _state.value = _state.value.copy(
            dateStart = dateStartAsDate,
            dateEnd = dateEndAsDate
        )
    }

    fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime){
        val currentDate = LocalDateTime.now()
        val durationLeft = Duration.between(currentDate, dateEnd)

        val durationPast = Duration.between(dateStart, currentDate)

        val daysPast = durationPast.toDays()
        val hoursPast = durationPast.toHours() % 24
        val minutesPast = durationPast.toMinutes() % 60
        val secondsPast = durationPast.seconds % 60

        val daysLeft = durationLeft.toDays()
        val hoursLeft = durationLeft.toHours() % 24
        val minutesLeft = durationLeft.toMinutes() % 60
        val secondsLeft = durationLeft.seconds % 60

        val duration = Duration.between(dateStart, dateEnd)

        val percentage = (durationPast.seconds.toDouble() / duration.seconds * 100).toString()
        val percentageString = ((if (percentage.length > 9) percentage.take(9) else percentage) + "%").replace(".", ",")

        _state.value = _state.value.copy(
            daysLeft = daysLeft,
            hoursLeft = hoursLeft,
            minutesLeft = minutesLeft,
            secondsLeft = secondsLeft,
            daysPast = daysPast,
            hoursPast = hoursPast,
            minutesPast = minutesPast,
            secondsPast = secondsPast,
            percentage = percentageString,
            percentageDouble = percentage.toDouble()
        )
    }
}

data class HomeState(
    val daysLeft: Long? = null,
    val hoursLeft: Long? = null,
    val minutesLeft: Long? = null,
    val secondsLeft: Long? = null,
    val daysPast: Long? = null,
    val hoursPast: Long? = null,
    val minutesPast: Long? = null,
    val secondsPast: Long? = null,
    val name: String? = null,
    val dateStart: LocalDateTime? = null,
    val dateEnd: LocalDateTime? = null,
    val percentage: String? = null,
    val percentageDouble: Double? = null
)