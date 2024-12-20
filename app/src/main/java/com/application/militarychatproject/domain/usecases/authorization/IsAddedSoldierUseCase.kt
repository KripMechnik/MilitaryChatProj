package com.application.militarychatproject.domain.usecases.authorization

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.utils.io.concurrent.shared
import javax.inject.Inject

class IsAddedSoldierUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(): Boolean {
        val sharedPreferences = context.getSharedPreferences("dates", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")
        val dateStart = sharedPreferences.getString("start", "")
        val dateEnd = sharedPreferences.getString("end", "")
        name?.let {currName ->
            dateStart?.let {start ->
                dateEnd?.let {end ->
                    return currName.isNotBlank() && start.isNotBlank() && end.isNotBlank()
                }
            }

        }
        return false
    }
}