package com.application.timer_dmb.presentation.home.view

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.timer_dmb.R
import com.application.timer_dmb.domain.entity.receive.EventEntity
import com.application.timer_dmb.presentation.home.BackgroundState
import com.application.timer_dmb.presentation.home.HomeScreenPresenter
import com.application.timer_dmb.presentation.home.HomeState
import com.application.timer_dmb.presentation.presets.ButtonPreset
import com.application.timer_dmb.ui.theme.ButtonGrey
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.Transparent
import com.application.timer_dmb.ui.theme.TransparentBlack
import com.application.timer_dmb.ui.theme.TransparentBlackCard
import com.application.timer_dmb.ui.theme.White
import com.application.timer_dmb.ui.theme.manropeFamily
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
    presenter: HomeScreenPresenter,
    innerPadding: PaddingValues
){

    presenter.setAllData()

    var secondCardSize by remember { mutableStateOf(IntSize.Zero) }


    val sheetHeight by remember {
        mutableIntStateOf(210)
    }

    val background = presenter.background.collectAsState()

    presenter.getImageFromCache()

    val nearestEvent = presenter.nearestEvent.collectAsState()

    Log.i("home", sheetHeight.toString())

    val state by presenter.state.collectAsState()

    LaunchedEffect(Unit) {
        state.dateEnd?.let {end ->
            state.dateStart?.let{start ->
                presenter.countDate(end, start)
            }
        }
        while (true){
            state.dateEnd?.let {end ->
                state.dateStart?.let{start ->
                    presenter.countDate(end, start)
                }
            }
            delay(1000)
        }
    }

    val eventAchieve = presenter.eventAchieve.collectAsState()

    val scaffoldSheetState = rememberBottomSheetScaffoldState(
        SheetState(
            skipPartiallyExpanded = false,
            density = LocalDensity.current,
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )

    val hazeState = remember { HazeState() }
    BottomSheetScaffold(
        modifier = Modifier
            .haze(
                state = hazeState,
                backgroundColor = TransparentBlack,
                tint = if (Build.VERSION.SDK_INT > 31 ){
                    Color.Black.copy(alpha = .2f)
                } else {
                    Color.Black.copy(alpha = .4f)
                },
                blurRadius = 15.dp,
            ),
        scaffoldState = scaffoldSheetState,
        sheetContent = {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(740.dp)
                    .hazeChild(
                        state = hazeState,
                        shape = BottomSheetDefaults.ExpandedShape
                    )
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            ) {


                if (state.finished){
                    Gradient()
                }


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
                        .padding(top = 30.dp, start = 10.dp, end = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 15.dp, alignment = Alignment.Top)
                ) {

                    if (state.dateStart == null || state.dateEnd == null || state.percentage == null){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 40.dp),
                            color = White
                        )
                    } else {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(
                                    fontFamily = manropeFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 68.sp,
                                    letterSpacing = 0.sp,
                                    color = White
                                )
                                ) {
                                    append(state.percentage?.substringBefore(',')  ?: " ")
                                }
                                append(
                                    state.percentage?.let {
                                        "," + it.substringAfter(',')
                                    } ?: " "
                                )
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                        ShowProgress(
                            score = state.percentageDouble?.toInt() ?: 0, nearestEvent = eventAchieve.value - 3
                        )

                        Text(
                            text = if (!state.finished)
                            {
                                if (scaffoldSheetState.bottomSheetState.targetValue == SheetValue.PartiallyExpanded) "Смахни вверх, чтобы увидеть детали" else state.name ?: ""
                            } else {
                                "Поздравляем с ДМБ!"
                            },
                            style = if (!state.finished){
                                if (scaffoldSheetState.bottomSheetState.targetValue == SheetValue.PartiallyExpanded) MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp) else MaterialTheme.typography.bodySmall
                            } else {
                                MaterialTheme.typography.bodySmall
                            },
                            color = if (state.finished) White else MaterialTheme.colorScheme.onBackground
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                ,
                            colors = CardDefaults.cardColors(
                                containerColor = TransparentBlackCard
                            )
                        ){
                            FirstCardContent(presenter, state)
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onSizeChanged {
                                    secondCardSize = it
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = TransparentBlackCard
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                if (state.finished){
                                    Gradient(
                                        modifier = Modifier.then(
                                            with(LocalDensity.current) {
                                                Modifier.size(
                                                    width = secondCardSize.width.toDp(),
                                                    height = secondCardSize.height.toDp(),
                                                )
                                            }
                                        )
                                    )
                                }
                                SecondCardContent(nearestEvent, presenter)
                            }

                        }

                        ButtonPreset(
                            containerColor = White,

                            contentColor = MaterialTheme.colorScheme.secondary,
                            label = "Поделиться таймером"
                        ) {
                            presenter.navigateToSharePicture()
                        }
                    }

                }
            }
        },
        sheetDragHandle = {},
        sheetContainerColor = Transparent,
        sheetPeekHeight = sheetHeight.dp + innerPadding.calculateBottomPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    when (background.value) {

                        null -> {
                            ColorPainter(White)
                        }

                        is BackgroundState.Loading -> {
                            ColorPainter(White)
                        }

                        is BackgroundState.Success -> {
                            BitmapPainter(background.value!!.data!!.asImageBitmap())
                        }

                        else -> {
                            painterResource(id = R.drawable.background_profile)
                        }
                    },
                    contentScale = ContentScale.Crop
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
    override val nearestEvent: StateFlow<EventEntity?>
        get() = MutableStateFlow(null)
    override val eventAchieve: StateFlow<Int>
        get() = MutableStateFlow(0)
    override val background: StateFlow<BackgroundState?>
        get() = MutableStateFlow(null)

    override fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime) {

    }

    override fun setAllData() {

    }

    override fun getImageFromCache() {

    }

    override fun navigateToCalendar() {

    }

    override fun isAuthorized(): Boolean {
        return false
    }

    override fun navigateToSharePicture() {

    }
}



@Preview
@Composable
fun ShowProgress(
    modifier: Modifier = Modifier,
    score: Int = 50, nearestEvent: Int = 70
){
    val progressFactor by remember(score) {
        mutableFloatStateOf(score*0.01f)
    }

    val nearestEventState by remember(nearestEvent) {
        mutableFloatStateOf(nearestEvent*0.01f)
    }



    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(18.dp)
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


            if (nearestEvent in 1..100){
                Image(
                    painter = painterResource(id = R.drawable.progress_indicator),
                    contentDescription = "progress_indicator"
                )
            }
        }

    }

}

@ExperimentalMaterial3Api
@Preview
@Composable
fun HomeScreenPreview(){

    MilitaryChatProjectTheme {
        HomeScreen(defaultPresenter(), PaddingValues(0.dp))
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
                        append(state.daysPast?.toString() ?: "-")
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
                        append(state.hoursPast?.toString() ?: "-")
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
                        append(state.minutesPast?.toString() ?: "-")
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
                        append(state.secondsPast?.toString() ?: "-")
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
                        append(state.daysLeft?.toString() ?: "-")
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
                        append(state.hoursLeft?.toString() ?: "-")
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
                        append(state.minutesLeft?.toString() ?: "-")
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
                        append(state.secondsLeft?.toString() ?: "-")
                    }
                    append(" секунд")
                },
                style = MaterialTheme.typography.labelSmall,
            )

        }

    }
}

@Composable
fun SecondCardContent(
    nearestEvent: State<EventEntity?>,
    presenter: HomeScreenPresenter
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    modifier = Modifier
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.progress_indicator),
                    contentDescription = "progress_indicator"
                )
                Text(text = "Событие", style = MaterialTheme.typography.labelSmall)
            }
            Text(
                text = nearestEvent.value?.let {
                    if (it.title.length > 20){
                        it.title.take(20) + "..."
                    } else {
                        it.title
                    }
                } ?: "Нет событий",
                style = MaterialTheme.typography.bodySmall,
                color = White
            )
        }
        Button(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                presenter.navigateToCalendar()
            },
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