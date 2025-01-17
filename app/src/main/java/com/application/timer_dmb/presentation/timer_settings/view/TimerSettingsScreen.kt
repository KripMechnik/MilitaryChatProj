package com.application.timer_dmb.presentation.timer_settings.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.presentation.presets.ButtonPreset
import com.application.timer_dmb.presentation.registration.add_soldier.view.DataInput
import com.application.timer_dmb.presentation.registration.add_soldier.view.WheelDatePickerExtension
import com.application.timer_dmb.presentation.timer_settings.SoldierTime
import com.application.timer_dmb.presentation.timer_settings.TimerSettingsScreenPresenter
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("SuspiciousIndentation")
@Composable
fun TimerSettingsScreen(
    presenter: TimerSettingsScreenPresenter
) {

    val timeState = presenter.timeState.collectAsState()





    val dateStart = remember {
        mutableStateOf(timeState.value.dateStart)
    }

    val dateEnd = remember {
        mutableStateOf(timeState.value.dateEnd)
    }


    var show by remember {
        mutableStateOf(false)
    }

    var selectedDate by remember {
        mutableStateOf(dateStart)
    }

    var dateEndError by remember {
        mutableStateOf(false)
    }

    var dateStartError by remember {
        mutableStateOf(false)
    }



    fun checkData(){
        dateStartError = false
        dateEndError = false
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        if (timeState.value.dateStart.isBlank() || timeState.value.dateEnd.isBlank()){
            dateStartError = true
            dateEndError = true
            return
        }
        val dateEndAsDate = LocalDateTime.parse(timeState.value.dateEnd + " 00:00:00", formatter)
        val dateStartAsDate = LocalDateTime.parse(timeState.value.dateStart + " 00:00:00", formatter)
        if (dateStartAsDate > dateEndAsDate){
            dateStartError = true
            dateEndError = true
            return
        }
    }

    if (show)
    BottomSheetDialog(
        onDismissRequest = {
            show = false
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White, RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Выберите дату",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            WheelDatePickerExtension(
                onDateChanged = {day, month, year, date ->
                    selectedDate.value = "${if (day < 10) "0${day}" else "$day"}.${if (month + 1 < 10) "0${month + 1}" else "${month + 1}"}.${year}"
                },
            )
            ButtonPreset(
                contentColor = White,
                containerColor = MaterialTheme.colorScheme.secondary,
                content = {
                    Text(
                        text = "Согласен",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            ) {
                if (selectedDate == dateEnd){
                    presenter.setSoldierEndState(dateEnd.value)
                } else {
                    presenter.setSoldierStartState(dateStart.value)
                }


                show = false
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 60.dp, 30.dp, 90.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "Настройки таймера",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )


            AnimatedVisibility(
                visible = dateStartError,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Ошибка! Неверная дата призыва.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            DataInput("Дата призыва", date = timeState.value.dateStart.ifBlank { "Введите дату" }, isError = dateStartError){
                dateStartError = false
                dateEndError = false
                show = true
                selectedDate = dateStart

            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
            )

            AnimatedVisibility(
                visible = dateEndError,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Ошибка! Неверная дата дембеля.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            DataInput("Дата дембеля", date = timeState.value.dateEnd.ifBlank { "Введите дату" }, isError = dateEndError){
                dateStartError = false
                dateEndError = false
                show = true
                selectedDate = dateEnd
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                ButtonPreset(
                    label = "Сохранить",
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = White
                ) {
                    checkData()
                    if (!dateStartError && !dateEndError){
                        presenter.updateSoldierData()
                        presenter.navigateToMenu()
                    }
                }
            }




        }
    }
}

@Preview
@Composable
private fun TimerSettingsScreenPreview() {

    val presenter = object : TimerSettingsScreenPresenter{
        override val timeState: StateFlow<SoldierTime>
            get() = MutableStateFlow(SoldierTime())

        override fun updateSoldierData() {

        }

        override fun setSoldierStartState(dateStart: String) {

        }

        override fun setSoldierEndState(dateEnd: String) {

        }

        override fun navigateToMenu() {

        }

    }

    MilitaryChatProjectTheme {
        Surface(
            color = White
        ) {
            TimerSettingsScreen(presenter)
        }
    }
}