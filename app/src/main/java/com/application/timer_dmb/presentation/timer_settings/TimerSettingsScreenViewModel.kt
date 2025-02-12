package com.application.timer_dmb.presentation.timer_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.send.UpdatedTimerEntity
import com.application.timer_dmb.domain.usecases.authorization.GetSoldierDataUseCase
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.timer_dmb.domain.usecases.authorization.UpdateSoldierUseCase
import com.application.timer_dmb.domain.usecases.events.UpdateBaseEventsUseCase
import com.application.timer_dmb.domain.usecases.timer.UpdateTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TimerSettingsScreenViewModel @Inject constructor(
    private val getSoldierDataUseCase: GetSoldierDataUseCase,
    private val updateSoldierUseCase: UpdateSoldierUseCase,
    private val updateBaseEventsUseCase: UpdateBaseEventsUseCase,
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
    private val updateTimerUseCase: UpdateTimerUseCase,
) : ViewModel() {

    private val _timeState = MutableStateFlow(SoldierTime())
    val timeState = _timeState.asStateFlow()

    private val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    init {
        getSoldierData()
    }

    private fun getSoldierData(){
        val data = getSoldierDataUseCase()
        _timeState.value = _timeState.value.copy(dateStart = data[1]?.dropLast(9) ?: "", dateEnd = data[2]?.dropLast(9) ?: "")
    }

    fun setSoldierStartState(dateStart: String){
        _timeState.value = _timeState.value.copy(dateStart = dateStart)
        Log.i("setStart", dateStart)
    }

    fun setSoldierEndState(dateEnd: String){
        _timeState.value = _timeState.value.copy(dateEnd = dateEnd)
        Log.i("setEnd", dateEnd)
    }

    fun updateSoldierData(){

        viewModelScope.launch {
            updateSoldierUseCase(_timeState.value.dateStart, _timeState.value.dateEnd)
            updateBaseEventsUseCase(_timeState.value.dateStart, _timeState.value.dateEnd)
        }
        if (isAuthorizedUseCase()) {
            val startMillis = dateToMillis(_timeState.value.dateStart)
            val endMillis = dateToMillis(_timeState.value.dateEnd)
            startMillis?.let { start ->
                endMillis?.let { end ->
                    val updatedTimerEntity = UpdatedTimerEntity(startTimeMillis = start, endTimeMillis = end)
                    updateTimerUseCase(updatedTimerEntity).onEach{ result ->
                        when(result){
                            is Resource.Error -> {
                                Log.e("update_timer", result.code.toString() + " " + result.message)
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {}
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    private fun dateToMillis(date: String): Long?{


        val dateAsDate = format.parse(date)
        val dateAsMillis = dateAsDate?.time
        return dateAsMillis
    }

}

data class SoldierTime(
    val dateStart: String = "",
    val dateEnd: String = ""
)