package com.application.timer_dmb.presentation.registration.add_soldier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.domain.usecases.authorization.AddSoldierUseCase
import com.application.timer_dmb.domain.usecases.events.CreateBaseEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSoldierScreenViewModel @Inject constructor(
    private val addSoldierUseCase: AddSoldierUseCase,
    private val createBaseEventsUseCase: CreateBaseEventsUseCase
): ViewModel() {

    private val _addedEvents = MutableStateFlow(false)
    val addedEvents = _addedEvents.asStateFlow()

    fun saveData(dateStart: String, dateEnd: String, name: String){
        createEvents(dateStart, dateEnd)
        addSoldierUseCase(dateStart, dateEnd, name)
    }

    private fun createEvents(dateStart: String, dateEnd: String){
        viewModelScope.launch {
            launch {
                createBaseEventsUseCase(dateStart, dateEnd)
            }.join()

            _addedEvents.value = true
        }

    }

}