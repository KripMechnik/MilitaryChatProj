package com.application.militarychatproject.presentation.registration.add_soldier

import androidx.lifecycle.ViewModel
import com.application.militarychatproject.domain.usecases.authorization.AddSoldierUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddSoldierScreenViewModel @Inject constructor(
    private val addSoldierUseCase: AddSoldierUseCase
): ViewModel() {
    fun saveData(dateStart: String, dateEnd: String, name: String){
        addSoldierUseCase(dateStart, dateEnd, name)
    }
}