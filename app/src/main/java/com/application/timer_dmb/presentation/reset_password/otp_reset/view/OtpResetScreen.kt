package com.application.timer_dmb.presentation.reset_password.otp_reset.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.presentation.registration.otp.GetCodeState
import com.application.timer_dmb.presentation.registration.otp.OtpAction
import com.application.timer_dmb.presentation.registration.otp.OtpState
import com.application.timer_dmb.presentation.registration.otp.view.OtpInputField
import com.application.timer_dmb.presentation.reset_password.otp_reset.OtpResetScreenPresenter
import com.application.timer_dmb.presentation.reset_password.otp_reset.SendCodeResetState
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpResetScreen(
    presenter: OtpResetScreenPresenter,
    email: String,
    onAction: (OtpAction) -> Unit,
) {

    val state by presenter.state.collectAsState()
    val focusRequesters = presenter.focusRequesters



    val sendCodeState by presenter.sendCodeState.collectAsState()

    val focusManager = LocalFocusManager.current

    val keyboardManager = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }



    var timeLeft by remember {
        mutableIntStateOf(120)
    }

    val enabled by remember(timeLeft) {
        mutableStateOf(timeLeft == 0)
    }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
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

    LaunchedEffect(sendCodeState) {
        if (sendCodeState is SendCodeResetState.Success && email.isNotEmpty()){
            presenter.navigateToConfirmPassword(email = email)
        } else {
            Log.i("email", email)
        }
    }

    val getCodeState = presenter.getCodeState.collectAsState()


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
                                modifier = Modifier
                                    .clickable {
                                        presenter.navigateUp()
                                    },
                                text = "Назад",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                    },
                    navigationIcon = {
                        IconButton(onClick = { presenter.navigateUp() }) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp, innerPadding.calculateTopPadding(), 25.dp, 90.dp),
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
                    text = if (sendCodeState is SendCodeResetState.Error && state.code.none { it == null }){
                        when ((sendCodeState as SendCodeResetState.Error).code){

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
                if (getCodeState.value is GetCodeState.Loading){
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


}

@Preview
@Composable
private fun OtpResetScreenPreview() {

    val presenter = object : OtpResetScreenPresenter{
        override val state: StateFlow<OtpState>
            get() = MutableStateFlow(OtpState(code = List(6){3}))
        override val focusRequesters: List<FocusRequester>
            get() = List(6) { FocusRequester() }
        override val sendCodeState: StateFlow<SendCodeResetState?>
            get() = MutableStateFlow(null)
        override val getCodeState: StateFlow<GetCodeState?>
            get() = MutableStateFlow(null)

        override fun onAction(action: OtpAction) {

        }

        override fun navigateUp() {

        }

        override fun navigateToConfirmPassword(email: String) {

        }

        override fun getCode() {

        }

    }

    MilitaryChatProjectTheme {
        Surface (
            color = White
        ){
            OtpResetScreen(presenter, "") { }
        }
    }
}