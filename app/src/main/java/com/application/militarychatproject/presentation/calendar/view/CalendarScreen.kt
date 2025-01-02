package com.application.militarychatproject.presentation.calendar.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.application.militarychatproject.R
import com.application.militarychatproject.presentation.calendar.AllEventsState
import com.application.militarychatproject.presentation.calendar.CalendarScreenPresenter
import com.application.militarychatproject.presentation.messanger.chat.view.MenuItem
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.presentation.presets.InputPreset
import com.application.militarychatproject.presentation.registration.add_soldier.view.DataInput
import com.application.militarychatproject.presentation.registration.add_soldier.view.WheelDatePickerExtension
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import com.application.militarychatproject.ui.theme.White
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle.FULL
import java.time.format.TextStyle.SHORT
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    presenter: CalendarScreenPresenter
) {


    val listOfMenuItems = listOf(
        MenuItem(
            title = "Удалить",
            icon = painterResource(R.drawable.delete)
        ),
        MenuItem(
            title = "Редактировать",
            icon = painterResource(R.drawable.edit)
        )
    )

    val allEvents = presenter.allEvents.collectAsState()

    val allEventsState = presenter.allEventsState.collectAsState()

    val calendarState = presenter.calendarState

    val text = remember {
        mutableStateOf("")
    }

    var showAddEvent by remember {
        mutableStateOf(false)
    }

    var initialDate = remember {
        mutableStateOf("")
    }

    var initialDateMillis = remember {
        mutableStateOf("")
    }

    var id = remember {
        mutableStateOf("")
    }

    var showEditEvent by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(allEventsState) {
        Log.i("selected", "check")
        if (allEventsState.value is AllEventsState.Success){
            allEventsState.value!!.data!!.forEach {
                calendarState.selectionState.onDateSelected(convertMillisToLocalDate(it.timeMillis.toLong()))
                Log.i("selected", it.title)
            }
        }

    }

    if (showEditEvent){
        EditEventDialog(
            onDismissRequest = {
                showEditEvent = false
                initialDate.value = ""
                id.value = ""
                text.value = ""
                initialDateMillis.value = ""
            },
            text = text,
            presenter = presenter,
            initialDate = initialDate,
            id = id,
            initialDateMillis = initialDateMillis.value
        )
    }

    if (showAddEvent){
        AddEventDialog(
            onDismissRequest = {
                showAddEvent = false
                text.value = ""
            },
            text = text,
            presenter = presenter
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = White,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Row (
                            modifier = Modifier
                                .padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Text(
                                text = "Меню",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )


                        }

                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            presenter.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(
                    start = 30.dp,
                    top = innerPadding.calculateTopPadding(),
                    end = 30.dp,
                    bottom = innerPadding.calculateBottomPadding() + WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.calendar),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )



            SelectableCalendar(

                calendarState = calendarState,

                monthHeader = { monthState ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            modifier = Modifier.testTag("Decrement"),
                            enabled = monthState.currentMonth > monthState.minMonth,
                            onClick = { monthState.currentMonth = monthState.currentMonth.minusMonths(1) }
                        ) {
                            Image(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous",
                            )
                        }
                        Text(
                            modifier = Modifier.testTag("MonthLabel"),
                            text = monthState.currentMonth.month
                                .getDisplayName(FULL, Locale.getDefault())
                                .lowercase()
                                .replaceFirstChar { it.titlecase() } + ",",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = monthState.currentMonth.year.toString(), style = MaterialTheme.typography.labelMedium)
                        IconButton(
                            modifier = Modifier.testTag("Decrement"),
                            enabled = monthState.currentMonth < monthState.maxMonth,
                            onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(1) }
                        ) {
                            Image(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Previous",
                            )
                        }
                    }
                },

                daysOfWeekHeader = {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    ) {
                        it.forEach { dayOfWeek ->
                            Text(
                                textAlign = TextAlign.Center,
                                text = dayOfWeek.getDisplayName(SHORT, Locale.getDefault())[0].toString(),
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                dayContent = {state ->
                    val date = state.date
                    val selectionState = state.selectionState

                    val isSelected = selectionState.isDateSelected(date)

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f),

                        contentAlignment = Alignment.TopEnd
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
                        ) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.labelMedium,
                                color = if (state.isFromCurrentMonth) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                            )

                        }

                        if (isSelected){
                            Image(
                                modifier = Modifier
                                    .size(12.dp),
                                painter = painterResource(id = R.drawable.progress_indicator),
                                contentDescription = "progress_indicator"
                            )
                        }

                    }


                }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                HorizontalDivider()
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
                ) {
                    if (allEvents.value.isNotEmpty()){
                        items(allEvents.value){ event ->
                            EventItem(
                                eventName = event.title,
                                date = convertMillisToDateString(event.timeMillis.toLong()),
                                menuItemsList = listOfMenuItems,
                                onDelete = {
                                    presenter.deleteEvent(
                                        event.title,
                                        event.timeMillis,
                                        event.id.toInt()
                                    )
                                },
                                onEdit = {
                                    showEditEvent = true
                                    text.value = event.title
                                    initialDate.value = convertMillisToDateStringAnotherFormat(event.timeMillis.toLong())
                                    id.value = event.id
                                    initialDateMillis.value = event.timeMillis
                                }
                            )
                        }
                    } else if (allEvents.value.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Нет событий",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
                HorizontalDivider()
            }





            ButtonPreset(
                label = "Добавить событие",
                contentColor = White,
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                showAddEvent = true
            }
        }
    }


}

@Composable
fun EventItem(
    eventName: String,
    date: String,
    menuItemsList: List<MenuItem>,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    val density = LocalDensity.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                itemHeight = with(density) {
                    it.height.toDp()
                }
            }
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        isContextMenuVisible = true
                    }
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top)
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    modifier = Modifier
                        .size(12.dp),
                    painter = painterResource(id = R.drawable.progress_indicator),
                    contentDescription = "progress_indicator"
                )
                Text(text = "Событие", style = MaterialTheme.typography.labelSmall)
            }

            Text(
                text = eventName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = date, style = MaterialTheme.typography.labelSmall)
        }

        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            ),
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFFB1B1B1)
        ) {
            menuItemsList.forEach{
                DropdownMenuItem(
                    text = {
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.labelMedium,
                            color = White
                        )
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(16.dp),
                            tint = White,
                            painter = it.icon,
                            contentDescription = "icon_menu"
                        )
                    },
                    onClick = {
                        if (it.title == "Редактировать"){
                            onEdit()
                        }

                        if (it.title == "Удалить"){
                            onDelete()
                        }
                        isContextMenuVisible = false
                    }
                )
                HorizontalDivider(
                    color = White.copy(0.1f)
                )
            }
        }
    }
}



@Preview
@Composable
private fun EventItemPreview() {
    MilitaryChatProjectTheme { 
        EventItem(
            "Половина службы пройдено",
            "20 Декабря, 2024",
            emptyList(),
            {},
            {}
        )
    }
}


fun convertMillisToDateString(millis: Long): String {
    val date = Date(millis)
    val dateFormat = SimpleDateFormat("d MMMM, yyyy", Locale("ru", "RU"))
    return dateFormat.format(date)
}

fun convertMillisToDateStringAnotherFormat(millis: Long): String {
    val date = Date(millis)
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru", "RU"))
    return dateFormat.format(date)
}

fun convertMillisToLocalDate(millis: Long): LocalDate {
    val instant = Instant.ofEpochMilli(millis)
    return instant.atZone(ZoneId.systemDefault()).toLocalDate()
}