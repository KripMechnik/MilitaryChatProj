package com.application.timer_dmb.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.receive.EventEntity
import com.application.timer_dmb.domain.usecases.authorization.GetSoldierDataUseCase
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.timer_dmb.domain.usecases.authorization.UpdateSoldierUseCase
import com.application.timer_dmb.domain.usecases.events.GetAllEventsFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.GetAllEventsUseCase
import com.application.timer_dmb.domain.usecases.timer.GetTimerDataUseCase
import com.application.timer_dmb.presentation.widget.DmbWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getSoldierDataUseCase: GetSoldierDataUseCase,
    @ApplicationContext private val context: Context,
    private val getAllEventsFromDatabaseUseCase: GetAllEventsFromDatabaseUseCase,
    private val getAllEventsUseCase: GetAllEventsUseCase,
    private val updateSoldierUseCase: UpdateSoldierUseCase,
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
    private val getTimerDataUseCase: GetTimerDataUseCase
) : ViewModel() {

    private val _background = MutableStateFlow<BackgroundState?>(null)
    val background = _background.asStateFlow()

    private var _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> get() = _state

    private var _nearestEvent = MutableStateFlow<EventEntity?>(null)
    val nearestEvent = _nearestEvent.asStateFlow()

    private var _eventAchieve = MutableStateFlow(0)
    val eventAchieve = _eventAchieve.asStateFlow()

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

    fun setAllData(){
        viewModelScope.launch {


            val events = launch {
                if (!isAuthorizedUseCase()){
                    getBaseEvents()
                } else {
                    getEventsFromServer()
                }
            }
            getSoldierData()
            events.join()
            setEventAchieve()
        }
    }

    fun isAuthorized() = isAuthorizedUseCase()

    private suspend fun getEventsFromServer(){
        getAllEventsUseCase().collect{result ->
            when(result){
                is Resource.Error -> {
                    Log.e("get_events_e", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val list = result.data!!
                    setNearestEvent(list)
                }
            }

        }
    }

    private suspend fun getBaseEvents(){
        val list = getAllEventsFromDatabaseUseCase()

        setNearestEvent(list)
    }


    private fun setNearestEvent(list: List<EventEntity>){
        var localNearestEvent: EventEntity? = null

        val localDate = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        for (i in list.indices){
            if (list[i].timeMillis.toLong() > localDate){
                localNearestEvent = list[i]
                break
            }
        }

        if (localNearestEvent != _nearestEvent.value && localNearestEvent != null){
            _nearestEvent.update {
                localNearestEvent
            }
        }
    }

    private fun setEventAchieve(){
        val data = getSoldierDataUseCase()
        val dateStartAsDate = LocalDateTime.parse(data[1], formatter)
        val dateEndAsDate = LocalDateTime.parse(data[2], formatter)
        val millisStart =  dateStartAsDate.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        val millisEnd = dateEndAsDate.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        Log.i("Achieve1", _eventAchieve.value.toString())
        _eventAchieve.value = (((nearestEvent.value?.timeMillis?.toLong()?.minus(millisStart))?.toDouble() ?: 0.toDouble()) / (millisEnd - millisStart) * 100).toInt()
        Log.i("Achieve", _eventAchieve.value.toString())
    }

    private fun getSoldierData(){
        val data = getSoldierDataUseCase()

        if (data[0] != _state.value.name){
            _state.value = HomeState(name = data[0])
        }

        if (!isAuthorizedUseCase()){
            val dateEndAsDate = LocalDateTime.parse(data[2], formatter)
            val dateStartAsDate = LocalDateTime.parse(data[1], formatter)
            if (dateStartAsDate != _state.value.dateStart || dateEndAsDate != _state.value.dateEnd){
                _state.value = _state.value.copy(
                    dateStart = dateStartAsDate,
                    dateEnd = dateEndAsDate
                )
            }

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val formattedDateTimeStart = dateStartAsDate.format(formatter)
            val formattedDateTimeEnd = dateEndAsDate.format(formatter)

            viewModelScope.launch(Dispatchers.IO) {
                updateSoldierUseCase(formattedDateTimeStart, formattedDateTimeEnd)
            }


        } else {
            getTimerDataUseCase().onEach { result ->
                when(result){
                    is Resource.Error -> {
                        Log.e("get_timer_e", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val instantStart = Instant.ofEpochMilli(result.data!!.startTime)
                        val instantEnd = Instant.ofEpochMilli(result.data.endTime)

                        val localDateTimeStart = LocalDateTime.ofInstant(instantStart, ZoneId.systemDefault())
                        val localDateTimeEnd = LocalDateTime.ofInstant(instantEnd, ZoneId.systemDefault())

                        if (localDateTimeStart != _state.value.dateStart || localDateTimeEnd != _state.value.dateEnd){
                            _state.value = _state.value.copy(
                                dateStart = localDateTimeStart,
                                dateEnd = localDateTimeEnd
                            )

                        }

                        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        val formattedDateTimeStart = localDateTimeStart.format(formatter)
                        val formattedDateTimeEnd = localDateTimeEnd.format(formatter)

                        updateSoldierUseCase(formattedDateTimeStart, formattedDateTimeEnd)
                    }
                }

            }.launchIn(viewModelScope)
        }


    }

    fun getImageFromCache(){
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(context.cacheDir, "background_image.png")
            if (file.exists()) {
                Log.i("Uri", Uri.fromFile(file).toString())
                val data = BitmapFactory.decodeFile(file.absolutePath)
                if (data != _background.value?.data){
                    _background.value = BackgroundState.Success(data = BitmapFactory.decodeFile(file.absolutePath))
                }
            } else {
                _background.value = BackgroundState.Error()
            }
        }
    }

    fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime){
        val currentDate = if (LocalDateTime.now() < dateStart) dateStart else LocalDateTime.now()

        val durationLeft = Duration.between(currentDate, dateEnd)

        val durationPast = Duration.between(dateStart, currentDate)

        val daysPast = durationPast.toDays()


        if (currentDate >= dateEnd){
            _state.value = _state.value.copy(
                finished = true,
                daysLeft = 0,
                hoursLeft = 0,
                minutesLeft = 0,
                secondsLeft = 0,
                daysPast = daysPast,
                hoursPast = 0,
                minutesPast = 0,
                secondsPast = 0,
                percentage = "100,000000%",
                percentageDouble = 100.toDouble()
            )
            return
        }



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
            finished = false,
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

sealed class BackgroundState(val data: Bitmap? = null, val message: String? = null){
    class Success(data: Bitmap) : BackgroundState(data = data)
    class Error(message: String? = null) : BackgroundState(message = message)
    class Loading : BackgroundState()
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
    val percentageDouble: Double? = null,
    val finished: Boolean = false
)