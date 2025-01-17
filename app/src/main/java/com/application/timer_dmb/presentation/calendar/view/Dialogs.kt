package com.application.timer_dmb.presentation.calendar.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.application.timer_dmb.presentation.calendar.CalendarScreenPresenter
import com.application.timer_dmb.presentation.presets.ButtonPreset
import com.application.timer_dmb.presentation.presets.InputPreset
import com.application.timer_dmb.presentation.registration.add_soldier.view.DataInput
import com.application.timer_dmb.presentation.registration.add_soldier.view.WheelDatePickerExtension
import com.application.timer_dmb.ui.theme.White
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog

@Composable
fun AddEventDialog(
    onDismissRequest: () -> Unit,
    text: MutableState<String>,
    presenter: CalendarScreenPresenter

) {

    var show by remember {
        mutableStateOf(false)
    }

    val dateStart = remember {
        mutableStateOf("")
    }

    var selectedDate by remember {
        mutableStateOf(dateStart)
    }

    Dialog(
        onDismissRequest = {onDismissRequest()},
    ) {
        Card(

            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            shape = RoundedCornerShape(32.dp)

        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = "Добавить событие",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                InputPreset(
                    label = "Название события",
                    state = text
                )
                DataInput("Дата события", date = dateStart.value.ifBlank { "Введите дату" }){
                    show = true
                    selectedDate = dateStart
                }
                ButtonPreset(
                    contentColor = White,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    content = {
                        Text(
                            text = "Добавить событие",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                ) {
                    presenter.insertEvent(title = text.value, date = selectedDate.value)
                    onDismissRequest()
                }
            }
        }
    }

    if (show) BottomSheetDialog(
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
                show = false
            }
        }
    }
}

@Composable
fun EditEventDialog(
    onDismissRequest: () -> Unit,
    text: MutableState<String>,
    presenter: CalendarScreenPresenter,
    initialDateMillis: String,
    initialDate: MutableState<String>,
    id: MutableState<String>
) {

    var show by remember {
        mutableStateOf(false)
    }

    val oldDate by remember {
        mutableStateOf(initialDate.value)
    }

    val dateStart = remember {
        mutableStateOf(initialDate.value)
    }

    var selectedDate by remember {
        mutableStateOf(dateStart)
    }

    Dialog(
        onDismissRequest = {onDismissRequest()},
    ) {
        Card(

            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            shape = RoundedCornerShape(32.dp)

        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = "Добавить событие",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                InputPreset(
                    label = "Название события",
                    state = text
                )
                DataInput("Дата события", date = selectedDate.value.ifBlank { "Введите дату" }){
                    show = true
                    selectedDate = dateStart
                }
                ButtonPreset(
                    contentColor = White,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    content = {
                        Text(
                            text = "Редактировать",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                ) {
                    presenter.updateEvent(oldDate = initialDateMillis, newTitle = text.value, newDate = selectedDate.value, id = id.value)
                    onDismissRequest()
                }
            }
        }
    }

    if (show) BottomSheetDialog(
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
                show = false
            }
        }
    }
}