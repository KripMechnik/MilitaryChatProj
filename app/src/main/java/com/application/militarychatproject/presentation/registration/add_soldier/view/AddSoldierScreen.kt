package com.application.militarychatproject.presentation.registration.add_soldier.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.presentation.presets.InputPreset
import com.application.militarychatproject.presentation.registration.add_soldier.AddSoldierScreenPresenter
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 60.dp, 30.dp, 90.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Text(
            modifier = Modifier
                .padding(end = 15.dp, top = 154.dp),
            text = "Введите дату",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .padding(end = 15.dp, top = 226.dp),
            text = "Введите дату",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
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
            InputPreset(
                label = "Дата призыва",
                state = dateStart
            )
            InputPreset(
                label = "Дата дембеля",
                state = dateEnd
            )
            InputPreset(
                label = "Имя солдата",
                state = name
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
                    presenter.saveData(dateStart.value, dateEnd.value, name.value)
                    presenter.navigateToHome()
                }
            }




        }
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