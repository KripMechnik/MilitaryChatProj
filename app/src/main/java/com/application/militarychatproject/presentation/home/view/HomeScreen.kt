package com.application.militarychatproject.presentation.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.militarychatproject.R
import com.application.militarychatproject.presentation.home.HomeScreenPresenter
import com.application.militarychatproject.presentation.home.HomeState
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import com.application.militarychatproject.ui.theme.Transparent
import com.application.militarychatproject.ui.theme.TransparentBlack
import com.application.militarychatproject.ui.theme.TransparentBlackCard
import com.application.militarychatproject.ui.theme.ButtonGrey
import com.application.militarychatproject.ui.theme.White
import com.application.militarychatproject.ui.theme.manropeFamily
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    presenter: HomeScreenPresenter
){
    LaunchedEffect(Unit) {
        while (true){
            presenter.state.value.dateEnd?.let {end ->
                presenter.state.value.dateStart?.let{start ->
                    presenter.countDate(end, start)
                }
            }
            delay(1000)
        }
    }

    val state by presenter.state.collectAsState()

    val hazeState = remember { HazeState() }
    BottomSheetScaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .haze(
                state = hazeState,
                backgroundColor = TransparentBlack,
                tint = Color.Black.copy(alpha = .2f),
                blurRadius = 15.dp,
            ),
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(750.dp)
                    .hazeChild(
                        state = hazeState,
                        shape = BottomSheetDefaults.ExpandedShape
                    )
                    .padding(start = 15.dp, end = 15.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BottomSheetDefaults.DragHandle()
                }
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 15.dp, alignment = Alignment.Top)
                ) {
                    Text(
                        buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            fontFamily = manropeFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 68.sp,
                            letterSpacing = 0.sp,
                            color = White
                        )
                        ) {
                            append(state.percentage?.take(2))
                        }
                        append(state.percentage?.drop(2))
                    },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    ShowProgress(score = state.percentageDouble?.toInt() ?: 0)

                    Text(
                        text = presenter.state.value.name ?: "Name not found",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = TransparentBlackCard
                        )
                    ){
                        FirstCardContent(presenter, state)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = TransparentBlackCard
                        )
                    ) {
                        SecondCardContent()
                    }

                    ButtonPreset(
                        containerColor = White,
                        contentColor = MaterialTheme.colorScheme.secondary,
                        label = "Поделиться таймером"
                    ) {

                    }
                }
            }
        },
        sheetDragHandle = {},
        sheetContainerColor = Transparent,
        sheetPeekHeight = 330.dp //250
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.background_profile),
                    contentScale = ContentScale.FillBounds
                )
        )
    }
}

fun defaultPresenter() = object : HomeScreenPresenter{
    override val state: StateFlow<HomeState> = MutableStateFlow(
        HomeState(
            daysLeft = 15L,
            hoursLeft = 23L,
            minutesLeft = 46L,
            secondsLeft = 15L,
            name = "Дмитрий"
        )
    )

    override fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime) {

    }
}

@Composable
fun TimerDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = "More options")
        }

        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(surface = ButtonGrey)
        ) {
            DropdownMenu(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            15.dp
                        )
                    ),
                expanded = expanded,
                onDismissRequest = { expanded = false },
                ) {
                DropdownMenuItem(
                    text = { Text("Всего", color = White) },
                    onClick = { expanded = false }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("В часах", color = White)},
                    onClick = { expanded = false }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("В минутах", color = White) },
                    onClick = { expanded = false }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("В секундах", color = White) },
                    onClick = { expanded = false }
                )
            }
        }
    }
}

@Preview
@Composable
fun ShowProgress(score: Int = 50, nearestEvent: Int = 70){
    val progressFactor by remember(score) {
        mutableFloatStateOf(score*0.01f)
    }

    val nearestEventState by remember(nearestEvent) {
        mutableFloatStateOf(nearestEvent*0.01f)
    }



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(18.dp)
            .padding(start = 16.dp, end = 16.dp)
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = 50,
                    bottomStartPercent = 50
                )
            )
            .background(Transparent),
        contentAlignment = Alignment.CenterStart
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .padding(start = 16.dp, end = 16.dp)
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = 50,
                    bottomStartPercent = 50
                )
            )
            .background(ButtonGrey),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(progressFactor)
                    .background(
                        White,
                        RoundedCornerShape(10.dp)
                    )

            ){
                Text(text = "")
            }

        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(18.dp)
            .padding(start = 16.dp, end = 16.dp)
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = 50,
                    bottomStartPercent = 50
                )
            )
            .background(Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(nearestEventState),
            )


            Image(
                painter = painterResource(id = R.drawable.progress_indicator),
                contentDescription = "progress_indicator"
            )
        }

    }

}

@ExperimentalMaterial3Api
@Preview
@Composable
fun HomeScreenPreview(){
    MilitaryChatProjectTheme {
        HomeScreen(defaultPresenter())
    }
}

@Composable
fun FirstCardContent(
    presenter: HomeScreenPresenter,
    state: HomeState
) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            TimerDropdownMenu()
            Text(
                text = "Прошло всего",
                style = MaterialTheme.typography.bodySmall,
                color = White
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ){
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = manropeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.sp,
                        color = White
                    )
                    ) {
                        append(state.daysPast.toString())
                    }
                    append(" дней")
                },
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start
            )

            Text(
                modifier = Modifier
                    .weight(1f),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = manropeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.sp,
                        color = White
                    )
                    ) {
                        append(state.hoursPast.toString())
                    }
                    append(" часов")
                },
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .weight(1f),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = manropeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.sp,
                        color = White
                    )
                    ) {
                        append(state.minutesPast.toString())
                    }
                    append(" минут")
                },
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = manropeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.sp,
                        color = White
                    )
                    ) {
                        append(state.secondsPast.toString())
                    }
                    append(" секунд")
                },
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.End
            )

        }

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Text(text = "Осталось",
            style = MaterialTheme.typography.bodySmall,
            color = White
        )

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = manropeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.sp,
                        color = White
                    )
                    ) {
                        append(state.daysLeft.toString())
                    }
                    append(" дней")
                },
                style = MaterialTheme.typography.labelSmall,
            )

            Text(
                modifier = Modifier
                    .weight(1f),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = manropeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.sp,
                        color = White
                    )
                    ) {
                        append(state.hoursLeft.toString())
                    }
                    append(" часов")
                },
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .weight(1f),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = manropeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.sp,
                        color = White
                    )
                    ) {
                        append(state.minutesLeft.toString())
                    }
                    append(" минут")
                },
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontFamily = manropeFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.sp,
                        color = White
                    )
                    ) {
                        append(state.secondsLeft.toString())
                    }
                    append(" секунд")
                },
                style = MaterialTheme.typography.labelSmall,
            )

        }

    }
}

@Composable
fun SecondCardContent() {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
            ){
                Image(
                    painter = painterResource(id = R.drawable.progress_indicator),
                    contentDescription = "progress_indicator"
                )
                Text(text = "Событие", style = MaterialTheme.typography.labelSmall)
            }
            Text(
                text = "Через 300 дней лето",
                style = MaterialTheme.typography.bodySmall,
                color = White
            )
        }
        Button(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {},
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonGrey
            )
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = "forward"
            )
        }
    }
}