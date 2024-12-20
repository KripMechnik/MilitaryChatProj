package com.application.militarychatproject.presentation.registration.otp.view

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.application.militarychatproject.domain.entity.receive.TokenEntity
import com.application.militarychatproject.presentation.registration.otp.GetCodeState
import com.application.militarychatproject.presentation.registration.otp.OtpAction
import com.application.militarychatproject.presentation.registration.otp.OtpScreenPresenter
import com.application.militarychatproject.presentation.registration.otp.OtpState
import com.application.militarychatproject.presentation.registration.otp.SendCodeState
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import com.application.militarychatproject.ui.theme.White
import com.application.militarychatproject.ui.theme.manropeFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun OtpScreen(
    presenter: OtpScreenPresenter,
    email: String,
    onAction: (OtpAction) -> Unit,
) {


    val state by presenter.state.collectAsState()
    val focusRequesters = presenter.focusRequesters

    val getCodeState by presenter.getCodeState.collectAsState()

    val sendCodeState by presenter.sendCodeState.collectAsState()

    var timeLeft by remember {
        mutableIntStateOf(120)
    }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    LaunchedEffect(sendCodeState) {
        if (sendCodeState is SendCodeState.Success){
            presenter.navigateToHome()
        }
    }

    val enabled by remember(timeLeft) {
        mutableStateOf(timeLeft == 0)
    }

    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.code, keyboardManager) {
        val allNumbersEntered = state.code.none { it == null }
        if(allNumbersEntered) {
            focusRequesters.forEach {
                it.freeFocus()
            }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 120.dp, 30.dp, 90.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
        ) {
            Text(
                text = "Код-пароль",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Введите код, который пришел вам на почту",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                state.code.forEachIndexed { index, number ->
                    OtpInputField(
                        number = number,
                        focusRequester = focusRequesters[index],
                        onFocusChanged = { isFocused ->
                            if(isFocused) {
                                onAction(OtpAction.OnChangeFieldFocused(index))
                            }
                        },
                        onNumberChanged = { newNumber ->
                            onAction(OtpAction.OnEnterNumber(newNumber, index))
                        },
                        onKeyboardBack = {
                            onAction(OtpAction.OnKeyboardBack)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                    )
                }
            }

            Text(
                text = if (sendCodeState is SendCodeState.Error && state.code.none { it == null }){
                    when ((sendCodeState as SendCodeState.Error).code){

                        404 -> "Ошибка! Введенный код неверный. Пожалуйста, проверьте код, полученный на вашу почту, и введите его снова."
                        500 -> "Ошибка сервера!"
                        else -> "Неизвестная ошибка!"
                    }
                } else "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            onClick = {
                presenter.getCode()
                timeLeft = 120
            },
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (enabled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = if (enabled) White else MaterialTheme.colorScheme.onBackground
            )
        ) {
            if (getCodeState is GetCodeState.Loading){
                CircularProgressIndicator()
            } else {
                if (enabled){
                    Text(text = "Отправить код", style = MaterialTheme.typography.labelMedium)
                } else {
                    Text(text = "Отправить еще раз через ${timeLeft / 60}:${if (timeLeft % 60 < 10) "0${timeLeft % 60}" else "${timeLeft % 60}"}", style = MaterialTheme.typography.labelMedium)
                }
            }

        }
    }



}

@Preview
@Composable
private fun OtpScreenPreview() {
    val presenter = object : OtpScreenPresenter {
        override val state: StateFlow<OtpState>
            get() = MutableStateFlow(OtpState(code = List(6){3}))
        override val sendCodeState: StateFlow<SendCodeState?>
            get() = MutableStateFlow(SendCodeState.Success(TokenEntity("", "", 0L, 0L)))
        override val getCodeState: StateFlow<GetCodeState?>
            get() = MutableStateFlow(GetCodeState.Success(Unit))
        override val focusRequesters: List<FocusRequester>
            get() =  List(6) { FocusRequester() }

        override fun onAction(action: OtpAction) {

        }

        override fun getCode() {

        }

        override fun navigateToHome() {
            TODO("Not yet implemented")
        }
    }
    MilitaryChatProjectTheme {
        OtpScreen(presenter = presenter, email = ""){}
    }
}

@Composable
fun OtpInputField(
    number: Int?,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onNumberChanged: (Int?) -> Unit,
    onKeyboardBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text by remember(number) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString().orEmpty(),
                selection = TextRange(
                    index = if(number != null) 1 else 0
                )
            )
        )
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.onPrimaryContainer, shape = RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                val newNumber = newText.text
                if(newNumber.length <= 1 && newNumber.isDigitsOnly()) {
                    onNumberChanged(newNumber.toIntOrNull())
                }
            },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = manropeFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 32.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
                .padding(10.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                    onFocusChanged(it.isFocused)
                }
                .onKeyEvent { event ->
                    val didPressDelete = event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                    if (didPressDelete && number == null) {
                        onKeyboardBack()
                    }
                    false
                }
        )
    }
}

@Preview
@Composable
private fun OtpInputFieldPreview() {
    MilitaryChatProjectTheme {
        OtpInputField(
            number = 2,
            focusRequester = remember { FocusRequester() },
            onFocusChanged = {},
            onKeyboardBack = {},
            onNumberChanged = {},
            modifier = Modifier
                .size(100.dp)
        )
    }
}