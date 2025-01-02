package com.application.militarychatproject.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.militarychatproject.data.calendar_database.Event
import com.application.militarychatproject.data.calendar_database.EventDao
import com.application.militarychatproject.data.calendar_database.toEventEntity
import com.application.militarychatproject.domain.entity.receive.EventEntity
import com.application.militarychatproject.domain.usecases.authorization.GetSoldierDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getSoldierDataUseCase: GetSoldierDataUseCase,
    private val dao: EventDao
) : ViewModel() {

    private var _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> get() = _state

    private var _nearestEvent = MutableStateFlow<EventEntity?>(null)
    val nearestEvent = _nearestEvent.asStateFlow()

    private var _eventAchieve = MutableStateFlow(0)
    val eventAchieve = _eventAchieve.asStateFlow()

    init {
        viewModelScope.launch {
            val events = launch {
                getBaseEvents()
            }
            events.join()
            getSoldierData()
        }

    }

    private suspend fun getBaseEvents(){
        val list = dao.getAllEvents().map { it.toEventEntity() }
        _nearestEvent.update {
            val localDate = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            if (list.isNotEmpty()){
                var entity: EventEntity? = null
                for (i in list.indices){
                    if (list[i].timeMillis.toLong() > localDate){
                        entity = list[i]
                        break
                    }
                }
                entity
            } else {
                null
            }
        }



    }

    private fun getSoldierData(){
        val data = getSoldierDataUseCase()
        _state.value = HomeState(name = data[0])
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val dateEndAsDate = LocalDateTime.parse(data[2], formatter)
        val dateStartAsDate = LocalDateTime.parse(data[1], formatter)
        val millisEnd = dateEndAsDate.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        val millisStart =  dateStartAsDate.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        Log.i("Achieve1", _eventAchieve.value.toString())
        _eventAchieve.value = (((nearestEvent.value?.timeMillis?.toLong()?.minus(millisStart))?.toDouble() ?: 0.toDouble()) / (millisEnd - millisStart) * 100).toInt()
        Log.i("Achieve", _eventAchieve.value.toString())
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