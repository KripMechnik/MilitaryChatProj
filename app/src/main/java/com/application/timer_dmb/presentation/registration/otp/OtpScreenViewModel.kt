package com.application.timer_dmb.presentation.registration.otp

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.receive.TokenEntity
import com.application.timer_dmb.domain.entity.send.GetOtpBodyEntity
import com.application.timer_dmb.domain.entity.send.NewEventEntity
import com.application.timer_dmb.domain.entity.send.SendOtpBodyEntity
import com.application.timer_dmb.domain.entity.send.UpdatedTimerEntity
import com.application.timer_dmb.domain.usecases.authorization.GetOtpCodeUseCase
import com.application.timer_dmb.domain.usecases.authorization.GetSoldierDataUseCase
import com.application.timer_dmb.domain.usecases.authorization.SaveTokenUseCase
import com.application.timer_dmb.domain.usecases.authorization.SendOtpUseCase
import com.application.timer_dmb.domain.usecases.events.CreateEventUseCase
import com.application.timer_dmb.domain.usecases.events.DeleteAllEventsFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.GetAllEventsFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.timer.UpdateTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class OtpViewModel @Inject constructor(
    private val getOtpCodeUseCase: GetOtpCodeUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getSoldierDataUseCase: GetSoldierDataUseCase,
    private val getAllEventsFromDatabaseUseCase: GetAllEventsFromDatabaseUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val deleteAllEventsFromDatabaseUseCase: DeleteAllEventsFromDatabaseUseCase,
    private val updateTimerUseCase: UpdateTimerUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private val email = savedStateHandle.getStateFlow("email", "")

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private val _getCodeState = MutableStateFlow<GetCodeState?>(null)
    val getCodeState = _getCodeState.asStateFlow()

    private val _sendCodeState = MutableStateFlow<SendCodeState?>(null)
    val sendCodeState = _sendCodeState.asStateFlow()

    private val _timerState = MutableStateFlow(Timer())

    init {
        getSoldierData()
    }

    fun getCode(){
        Log.e("otp", email.value)
        val otpBody = GetOtpBodyEntity(email.value)
        getOtpCodeUseCase(otpBody).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _getCodeState.value = GetCodeState.Error(message = result.message ?: "Unknown error", code = result.code)
                    Log.e("otp", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _getCodeState.value = GetCodeState.Loading()
                is Resource.Success -> _getCodeState.value = GetCodeState.Success(data = result.data!!)
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: OtpAction) {
        when(action) {
            is OtpAction.OnChangeFieldFocused -> {
                _state.update { it.copy(
                    focusedIndex = action.index
                ) }
            }
            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }
            OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)
                _state.update { it.copy(
                    code = it.code.mapIndexed { index, number ->
                        if(index == previousIndex) {
                            null
                        } else {
                            number
                        }
                    },
                    focusedIndex = previousIndex
                ) }
            }
        }
    }

    private fun getSoldierData(){
        val data = getSoldierDataUseCase()

        val dateEndAsDate = data[2]?.let { format.parse(it) }
        val dateStartAsDate = data[1]?.let { format.parse(it) }

        val dateStartMillis = dateStartAsDate?.time
        val dateEndMillis = dateEndAsDate?.time

        if (dateStartMillis != null && dateEndMillis != null){
            _timerState.value = _timerState.value.copy(
                timeStart = dateStartMillis,
                timeEnd = dateEndMillis
            )
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if(currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _state.update { it ->
            it.copy(
            code = newCode,
            focusedIndex = if(wasNumberRemoved || it.code.getOrNull(index) != null) {
                it.focusedIndex
            } else {
                getNextFocusedTextFieldIndex(
                    currentCode = it.code,
                    currentFocusedIndex = it.focusedIndex
                )
            },
            isValid = if(newCode.none { it == null }) {
                val otpBody = SendOtpBodyEntity(email.value, newCode.joinToString("").toInt())
                Log.i("send_otp", otpBody.toString())
                sendOtpUseCase(otpBody).onEach { result ->
                    when(result) {
                        is Resource.Error -> {
                            _sendCodeState.value = SendCodeState.Error(message = result.message ?: "Unknown error", code = result.code)
                            Log.e("otp", result.code.toString() + " " + result.message)
                        }
                        is Resource.Loading -> _sendCodeState.value = SendCodeState.Loading()
                        is Resource.Success -> {

                            Log.i("otp_success", result.data.toString())

                            viewModelScope.launch {
                                val data = result.data!!
                                var accessTokenString = data.accessToken.drop(7)
                                if (accessTokenString.startsWith("Bearer")) accessTokenString = accessTokenString.drop(7)
                                val savingToken = result.data.run {
                                    saveTokenUseCase(accessTokenString, refreshToken, accessTokenExpiresAt, refreshTokenExpiresAt)
                                }


                                val events = async {
                                    getAllEventsFromDatabaseUseCase()
                                }.await()
                                val clearingDatabase = launch {
                                    deleteAllEventsFromDatabaseUseCase()
                                }

                                if (savingToken){
                                    val updatingTimer = launch {
                                        _timerState.value.timeStart?.let { start ->
                                            _timerState.value.timeEnd?.let { end ->
                                                val updatedTimerEntity = UpdatedTimerEntity(startTimeMillis = start, endTimeMillis = end)
                                                updateTimerUseCase(updatedTimerEntity).collect{ result ->
                                                    when(result){
                                                        is Resource.Error -> {
                                                            Log.e("update_timer", result.code.toString() + " " + result.message)
                                                        }
                                                        is Resource.Loading -> {}
                                                        is Resource.Success -> {}
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    events.forEach {
                                        launch { val newEventEntity = NewEventEntity(title = it.title, timeMillis = it.timeMillis.toLong())
                                            Log.i("event_entity", newEventEntity.toString())
                                            createEventUseCase(newEventEntity).collect{result->
                                                if (result is Resource.Error){
                                                    Log.e("events", result.code.toString() + " " + result.message)
                                                }
                                                if (result is Resource.Success){
                                                    Log.e("events", "success" + newEventEntity.title)
                                                }

                                            }
                                        }.join()
                                    }

                                    updatingTimer.join()
                                    clearingDatabase.join()
                                    _sendCodeState.value = SendCodeState.Success(data = result.data)
                                } else {
                                    _sendCodeState.value = SendCodeState.Error(message = "Didn't save token", code = null)
                                }

                            }


                        }
                    }
                }.launchIn(viewModelScope)
                true
            } else null
        ) }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if(currentFocusedIndex == null) {
            return null
        }

        if(currentFocusedIndex == 5) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if(index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if(number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }
}

sealed class GetCodeState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success(data: Unit) : GetCodeState(data)
    class Error(message: String, code: Int?) : GetCodeState(message = message, code = code)
    class Loading : GetCodeState()
}

sealed class SendCodeState(val data: TokenEntity? = null, val message: String? = null, val code: Int? = null){
    class Success(data: TokenEntity) : SendCodeState(data)
    class Error(message: String, code: Int?) : SendCodeState(message = message, code = code)
    class Loading : SendCodeState()
}

data class OtpState(
    val code: List<Int?> = (1..6).map { null },
    val focusedIndex: Int? = null,
    val isValid: Boolean? = null
)

data class Timer(
    val timeStart: Long? = null,
    val timeEnd: Long? = null
)

sealed interface OtpAction {
    data class OnEnterNumber(val number: Int?, val index: Int): OtpAction
    data class OnChangeFieldFocused(val index: Int): OtpAction
    data object OnKeyboardBack: OtpAction
}