package com.application.militarychatproject.domain.usecases.authorization

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetSoldierDataUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(): List<String?> {
        val sharedPrefs = context.getSharedPreferences("dates", Context.MODE_PRIVATE)
        val name = sharedPrefs.getString("name", "")
        val dateStart = sharedPrefs.getString("start", "") + " 00:00:00"
        val dateEnd = sharedPrefs.getString("end", "") + " 00:00:00"
        return listOf(name, dateStart, dateEnd)
    }
}