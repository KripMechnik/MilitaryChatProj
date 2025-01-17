package com.application.timer_dmb.presentation.registration.add_soldier.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.ozcanalasalvar.datepicker.model.Date
import com.ozcanalasalvar.datepicker.ui.theme.colorLightPrimary
import com.ozcanalasalvar.datepicker.ui.theme.colorLightTextPrimary
import com.ozcanalasalvar.datepicker.ui.theme.lightPallet
import com.ozcanalasalvar.datepicker.utils.DateUtils
import com.ozcanalasalvar.datepicker.utils.daysOfDate
import com.ozcanalasalvar.datepicker.utils.monthsOfDate
import com.ozcanalasalvar.datepicker.utils.withDay
import com.ozcanalasalvar.datepicker.utils.withMonth
import com.ozcanalasalvar.datepicker.utils.withYear
import com.ozcanalasalvar.wheelview.SelectorOptions
import com.ozcanalasalvar.wheelview.WheelView
import java.text.DateFormatSymbols

@Composable
fun WheelDatePickerExtension(
    offset: Int = 2,
    yearsRange: IntRange = IntRange(1923, 2121),
    startDate: Date = Date(DateUtils.getCurrentTime()),
    selectorEffectEnabled: Boolean = true,
    onDateChanged: (Int, Int, Int, Long) -> Unit = { _, _, _, _ -> },
    darkModeEnabled: Boolean = true,
) {

    var selectedDate by remember { mutableStateOf(startDate) }

    val months = selectedDate.monthsOfDate()

    val days = selectedDate.daysOfDate()

    val years = mutableListOf<Int>().apply {
        for (year in yearsRange) {
            add(year)
        }
    }

    LaunchedEffect(selectedDate) {
        onDateChanged(selectedDate.day, selectedDate.month, selectedDate.year, selectedDate.date)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(colorLightPrimary),
        contentAlignment = Alignment.Center
    ) {

        val height = 50.dp

        SelectorViewExtension(offset = offset)

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
        ) {


            key(days.size) {
                WheelView(modifier = Modifier.weight(1f),
                    itemSize = DpSize(150.dp, height),
                    selection = maxOf(days.indexOf(selectedDate.day), 0),
                    itemCount = days.size,
                    rowOffset = offset,
                    selectorOption = SelectorOptions().copy(selectEffectEnabled = selectorEffectEnabled, enabled = false),
                    onFocusItem = {
                        selectedDate = selectedDate.withDay(days[it])
                    },
                    content = {
                        Text(
                            text = days[it].toString(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(50.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = colorLightTextPrimary
                        )
                    })
            }

            WheelView(modifier = Modifier.weight(2f),
                itemSize = DpSize(150.dp, height),
                selection = maxOf(months.indexOf(selectedDate.month), 0),
                itemCount = months.size,
                rowOffset = offset,
                selectorOption = SelectorOptions().copy(selectEffectEnabled = selectorEffectEnabled, enabled = false),
                onFocusItem = {
                    selectedDate = selectedDate.withMonth(months[it])
                },
                content = {
                    Text(
                        text = DateFormatSymbols().months[months[it]],
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(120.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = colorLightTextPrimary
                    )
                })


            WheelView(modifier = Modifier.weight(1f),
                itemSize = DpSize(150.dp, height),
                selection = years.indexOf(selectedDate.year),
                itemCount = years.size,
                rowOffset = offset,
                isEndless = years.size > offset * 2,
                selectorOption = SelectorOptions().copy(selectEffectEnabled = selectorEffectEnabled, enabled = false),
                onFocusItem = {
                    selectedDate = selectedDate.withYear(years[it])
                },
                content = {
                    Text(
                        text = years[it].toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = colorLightTextPrimary
                    )
                })
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = lightPallet
                    )
                ),
        ) {}





    }


}

@Composable
fun SelectorViewExtension(modifier: Modifier = Modifier, offset: Int) {
    Row (
        modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.onPrimaryContainer, shape = RoundedCornerShape(8.dp))
        )
        Box(
            modifier = Modifier
                .height(40.dp)
                .weight(2f)
                .background(color = MaterialTheme.colorScheme.onPrimaryContainer, shape = RoundedCornerShape(8.dp))
        )
        Box(
            modifier = Modifier
                .height(40.dp)
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.onPrimaryContainer, shape = RoundedCornerShape(8.dp))
        )
    }
}

@Preview
@Composable
fun DatePickerExtensionPreview() {
    MilitaryChatProjectTheme {
        WheelDatePickerExtension(onDateChanged = { _, _, _, _ -> })
    }
}