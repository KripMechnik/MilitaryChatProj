package com.application.militarychatproject.presentation.presets

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonPreset(
    modifier: Modifier = Modifier,
    label: String = "",
    content: @Composable (() -> Unit)? = null,
    contentColor: Color = Color.White,
    containerColor: Color,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        if (content == null){
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        } else {
            content()
        }
    }
}

@Composable
fun InputPreset(
    modifier: Modifier = Modifier,
    label: String,
    state: MutableState<String>
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 51.dp),
        value = state.value,
        onValueChange = {
            state.value = it
        },
        label = {
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        },
        shape = RoundedCornerShape(12.dp),
        textStyle = MaterialTheme.typography.labelMedium,
        colors = OutlinedTextFieldDefaults.colors(
            errorBorderColor = MaterialTheme.colorScheme.error,
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.onBackground
        )
    )
}