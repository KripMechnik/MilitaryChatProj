package com.application.timer_dmb.domain.usecases.authorization

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UpdateSoldierUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(dateStart: String, dateEnd: String){
        val sharedPrefs = context.getSharedPreferences("dates", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        if (dateStart.isNotBlank() && dateEnd.isNotBlank()){
            editor.putString("start", dateStart)
            editor.putString("end", dateEnd)
        }
        editor.apply()
    }
}