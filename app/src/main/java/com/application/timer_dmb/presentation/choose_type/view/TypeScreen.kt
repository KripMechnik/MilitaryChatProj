package com.application.timer_dmb.presentation.choose_type.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.presentation.choose_type.SetTypeState
import com.application.timer_dmb.presentation.choose_type.TypeScreenPresenter
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TypeScreen(
    presenter: TypeScreenPresenter
) {

    val setTypeState = presenter.setTypeState.collectAsState()

    LaunchedEffect(setTypeState.value) {
        if (setTypeState.value is SetTypeState.Success){
            presenter.navigateToHome()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 25.dp, end = 25.dp, top = 30.dp, bottom = 30.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Кто вы?",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Start
        )
        Text(
            text = "Выберите один из пунктов",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )



        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (setTypeState.value is SetTypeState.Error){
                Text(
                    modifier = Modifier
                        .align(Alignment.Start),
                    text = "Возникла ошибка при выполнении запроса",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ){
                TypeButtonPreset(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    type = "Солдат"
                ){
                    presenter.setType("SOLDIER")
                }
                TypeButtonPreset(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    type = "Жду солдата"
                ){
                    presenter.setType("WAITING_FOR_SOLDIER")
                }
            }
            TypeButtonPreset(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                type = "Другое"
            ){
                presenter.setType("OTHER")
            }
        }

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = {
                presenter.navigateToHome()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Пропустить",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}


@Composable
fun TypeButtonPreset(
    type: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                .align(Alignment.TopStart)
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomStart),
            text = type,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Preview
@Composable
private fun TypeButtonPresetPreview() {
    MilitaryChatProjectTheme {
        TypeButtonPreset(
            modifier = Modifier
                .size(150.dp),
            type = "Другое"
        ){}
    }

}

@Preview
@Composable
private fun TypeScreenPreview() {
    val presenter = object : TypeScreenPresenter {
        override val setTypeState: StateFlow<SetTypeState?>
            get() = MutableStateFlow(null)

        override fun setType(type: String) {

        }

        override fun navigateToHome() {

        }

    }
    MilitaryChatProjectTheme {
        Surface (
            color = White
        ){
            TypeScreen(presenter)
        }
    }
}