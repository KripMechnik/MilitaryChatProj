package com.application.militarychatproject.presentation.registration.add_soldier.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.presentation.presets.InputPreset
import com.application.militarychatproject.presentation.registration.add_soldier.AddSoldierScreenPresenter
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import com.application.militarychatproject.ui.theme.White
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AddSoldierScreen(
    presenter: AddSoldierScreenPresenter
) {

    val dateStart = remember {
        mutableStateOf("")
    }

    val dateEnd = remember {
        mutableStateOf("")
    }

    val name = remember {
        mutableStateOf("")
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

    var nameError by remember {
        mutableStateOf(false)
    }

    fun checkData(){
        dateStartError = false
        dateEndError = false
        nameError = false
        if (name.value.isBlank()) nameError = true
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        if (dateStart.value.isBlank() || dateEnd.value.isBlank()){
            dateStartError = true
            dateEndError = true
            return
        }
        val dateEndAsDate = LocalDateTime.parse(dateEnd.value + " 00:00:00", formatter)
        val dateStartAsDate = LocalDateTime.parse(dateStart.value + " 00:00:00", formatter)
        if (dateStartAsDate > dateEndAsDate){
            dateStartError = true
            dateEndError = true
            return
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
                dateStartError = false
                dateEndError = false
                nameError = false
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
                text = "Добавление солдата",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Заполните поля",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
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

            DataInput("Дата призыва", date = dateStart.value.ifBlank { "Введите дату" }, isError = dateStartError){
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

            DataInput("Дата дембеля", date = dateEnd.value.ifBlank { "Введите дату" }, isError = dateEndError){
                show = true
                selectedDate = dateEnd
            }

            AnimatedVisibility(
                visible = nameError,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Ошибка! Неверное имя пользователя.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            InputPreset(
                label = "Имя солдата",
                state = name,
                isError = nameError
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                ButtonPreset(
                    label = "Добавить",
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    checkData()
                    if (!dateStartError && !dateEndError && !nameError){
                        presenter.saveData(dateStart.value, dateEnd.value, name.value)
                        presenter.navigateToHome()
                    }
                }
            }




        }
    }





}

@Composable
fun DataInput(
    label: String,
    isError: Boolean = false,
    date: String = "Введите дату",
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (!isError) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier
                .weight(1f),
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (!isError) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.error
        )
        Text(
            text = date,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun DataInputPreview() {
    MilitaryChatProjectTheme {
        DataInput("Дата призыва"){}
    }
}

@Preview
@Composable
fun AddSoldierScreenPreview() {

    val presenter = object : AddSoldierScreenPresenter{
        override fun navigateToHome() {

        }

        override fun saveData(dateStart: String, dateEnd: String, name: String) {

        }

    }

    MilitaryChatProjectTheme {
        AddSoldierScreen(presenter)
    }
}