package com.application.timer_dmb.presentation.registration.registration.view

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.timer_dmb.R
import com.application.timer_dmb.presentation.Validation
import com.application.timer_dmb.presentation.presets.ButtonPreset
import com.application.timer_dmb.presentation.presets.InputPreset
import com.application.timer_dmb.presentation.registration.registration.RegistrationScreenPresenter
import com.application.timer_dmb.presentation.registration.registration.RegistrationState
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import com.application.timer_dmb.ui.theme.manropeFamily
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RegistrationScreen(
    presenter: RegistrationScreenPresenter
){

    val email = remember {
        mutableStateOf("")
    }

    val isErrorEmail = remember {
        derivedStateOf {
            if (email.value.isBlank()){
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(email.value).matches().not()
            }
        }
    }

    val nickname = remember {
        mutableStateOf("")
    }

    val isErrorNickname = remember {
        derivedStateOf {
            if (nickname.value.isBlank()){
                false
            } else {
                !Validation.validateNickname(nickname.value)
            }
        }
    }

    val password = remember {
        mutableStateOf("")
    }

    val isErrorPassword = remember {
        derivedStateOf {
            if (password.value.isBlank()){
                false
            } else {
                !Validation.validatePassword(password.value)
            }
        }
    }

    val context = LocalContext.current

    val state = presenter.state.collectAsState()

    LaunchedEffect(state.value) {
        if (state.value is RegistrationState.Success) {
            presenter.navigateToOtp(email = email.value)
            presenter.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding()
                .padding(25.dp, 60.dp, 25.dp, 55.dp),
            horizontalAlignment = Alignment.Start,

            ) {
            Text(
                text = stringResource(R.string.create_acc),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically)

            ){
                item {
                    AnimatedVisibility(
                        visible = state.value is RegistrationState.Error && state.value?.code == 409
                    ) {
                        Text(
                            text = "Аккаунт с данной почтой уже зарегистрирован.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                }

                item {
                    InputPreset(
                        modifier = Modifier
                            .imePadding(),
                        label = stringResource(R.string.nick),
                        state = nickname,
                        singleLine = true,
                        isError = isErrorNickname.value,
                        errorText = "Ошибка. Длинна должна быть больше 3 и меньше 30. Никнейм не может содержать символы кроме букв английского алфавита, цифр и \"_\""
                    )
                }

                item {
                    InputPreset(
                        modifier = Modifier
                            .imePadding(),
                        label = stringResource(R.string.e_mail),
                        state = email,
                        singleLine = true,
                        isError = isErrorEmail.value,
                        errorText = "Ошибка. E-Mail адрес не соответствует формату."
                    )
                }
                item {
                    InputPreset(
                        modifier = Modifier
                            .imePadding(),
                        label = stringResource(R.string.password),
                        state = password,
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isErrorPassword.value,
                        errorText = "Ошибка. Пароль должен быть длиннее 5 символов и короче 128"
                    )

                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                    )
                }
                item {
                    ButtonPreset(
                        content = {
                            when (state.value) {
                                is RegistrationState.Loading -> CircularProgressIndicator(color = White)
                                else -> Text(
                                    stringResource(R.string.to_register),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.secondary
                    ) {
                        if (!isErrorEmail.value) {
                            presenter.signUp(nickname.value, password.value, email.value)
                        }
                    }

                }
                item {
                    ButtonPreset(
                        label = stringResource(R.string.already_have_acc),
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        presenter.navigateToLogin()
                    }
                }
            }





        }

        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 25.dp, end = 25.dp, bottom = 50.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse("https://duty-timer.sunfesty.ru/privacy-policy"))
                    context.startActivity(intent)
                },
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    fontFamily = manropeFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                ) {
                    append("Продолжая, вы соглашаетесь с ")
                }
                withStyle(style = SpanStyle(
                    fontFamily = manropeFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                ) {
                    append("правилами пользования ")
                }
                withStyle(style = SpanStyle(
                    fontFamily = manropeFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                ) {
                    append("и ")
                }
                withStyle(style = SpanStyle(
                    fontFamily = manropeFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                ) {
                    append("политикой приватности")
                }
                toAnnotatedString()
            },
            style = MaterialTheme.typography.labelMedium
        )
    }




}

@Preview
@Composable
private fun RegistrationScreenPreview() {

    val presenter = object : RegistrationScreenPresenter {
        override val state: StateFlow<RegistrationState?>
            get() = MutableStateFlow(null)

        override fun resetState() {

        }

        override fun navigateToOtp(email: String) {

        }

        override fun navigateToLogin() {

        }

        override fun signUp(nickname: String, password: String, email: String) {

        }
    }

    MilitaryChatProjectTheme {
        Surface(
            color = White
        ) {
            RegistrationScreen(presenter)
        }
    }
}


