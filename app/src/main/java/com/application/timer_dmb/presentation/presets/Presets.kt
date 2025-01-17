package com.application.timer_dmb.presentation.presets

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme

@Composable
fun ButtonPreset(
    modifier: Modifier = Modifier,
    label: String = "",
    content: @Composable (() -> Unit)? = null,
    contentColor: Color = Color.White,
    containerColor: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        enabled = enabled,
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
    state: MutableState<String>,
    isError: Boolean = false,
    singleLine: Boolean = false,
    labelColor: Color = MaterialTheme.colorScheme.primary,
    errorText: String = "",
    placeholder: String = "",
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 51.dp),
        value = state.value,
        enabled = enabled,
        onValueChange = {
            state.value = it
        },
        placeholder = {
            Text(text = placeholder, style = MaterialTheme.typography.labelMedium)
        },
        singleLine = singleLine,
        isError = isError,
        visualTransformation = visualTransformation,
        supportingText = if (isError && errorText.isNotBlank()){
            {
                if (errorText.isNotBlank()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = errorText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        } else {
            null
        },
        label = {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = labelColor)
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

@Preview
@Composable
private fun InputPresetPreview() {

    val state = remember {
        mutableStateOf("")
    }

    MilitaryChatProjectTheme {
        InputPreset(
            label = "Test",
            state = state,
        )
    }
}