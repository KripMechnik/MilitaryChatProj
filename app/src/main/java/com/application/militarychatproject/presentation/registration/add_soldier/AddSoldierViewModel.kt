package com.application.militarychatproject.presentation.registration.add_soldier

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AddSoldierViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {

    private val sharedPrefs = context.getSharedPreferences("dates", Context.MODE_PRIVATE)

    fun saveData(dateStart: String, dateEnd: String, name: String){
        val editor = sharedPrefs.edit()
        if (dateStart.isNotBlank() && dateEnd.isNotBlank()){
            editor.putString("start", dateStart)
            editor.putString("end", dateEnd)
            editor.putString("name", name)
        }
        editor.apply()
    }
}