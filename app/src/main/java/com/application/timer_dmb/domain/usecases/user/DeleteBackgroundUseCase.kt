package com.application.timer_dmb.domain.usecases.user

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class DeleteBackgroundUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(){
        withContext(Dispatchers.IO){
            val file = File(context.cacheDir, "background_image.png")
            file.delete()
        }
    }
}