package com.application.timer_dmb.presentation.share_picture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.usecases.authorization.GetSoldierDataUseCase
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.timer_dmb.domain.usecases.messages.GetGlobalChatUseCase
import com.application.timer_dmb.domain.usecases.messages.SendMessageUseCase
import com.application.timer_dmb.domain.usecases.timer.GetTimerDataUseCase
import com.application.timer_dmb.domain.usecases.user.GetSelfUserDataUseCase
import com.application.timer_dmb.presentation.home.BackgroundState
import com.application.timer_dmb.presentation.home.HomeState
import com.application.timer_dmb.presentation.messanger.all_chats.ChatsState
import com.application.timer_dmb.presentation.messanger.chat.SingleMessageState
import com.application.timer_dmb.presentation.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SharePictureScreenViewModel @Inject constructor(
    private val getGlobalChatUseCase: GetGlobalChatUseCase,
    private val getSoldierDataUseCase: GetSoldierDataUseCase,
    private val getSelfUserDataUseCase: GetSelfUserDataUseCase,
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
    private val getTimerDataUseCase: GetTimerDataUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private var _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> get() = _state

    private val _profileState = MutableStateFlow<ProfileState?>(null)
    val profileState = _profileState.asStateFlow()

    private val _shareImageBitmap = MutableStateFlow<ImageBitmap?>(null)
    val shareImageBitmap = _shareImageBitmap.asStateFlow()

    private val _singleMessageState = MutableStateFlow<SingleMessageState?>(null)
    val singleMessageState = _singleMessageState.asStateFlow()

    private val _chatsState = MutableStateFlow<ChatsState?>(null)
    val chatsState = _chatsState.asStateFlow()

    private val _background = MutableStateFlow<BackgroundState?>(null)
    val background = _background.asStateFlow()

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

    fun isAuthorized() = isAuthorizedUseCase()

    init {
        getSoldierData()
        getSelfUser()
        getChat()
        getImageFromCache()
    }

    private fun getImageFromCache(){
        viewModelScope.launch(Dispatchers.IO) {
            _background.value = BackgroundState.Loading()
            val file = File(context.cacheDir, "background_image.png")
            if (file.exists()) {
                _background.value = BackgroundState.Success(data = BitmapFactory.decodeFile(file.absolutePath))
            } else {
                _background.value = BackgroundState.Error()
            }
        }
    }

    private fun saveBitmapAsFile(bitmap: Bitmap){

        viewModelScope.launch(Dispatchers.IO) {
            val file = File(context.cacheDir, "send.png")
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)

            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        }
    }

    private fun getImageFromCacheUri(): Uri? {

        val file = File(context.cacheDir, "send.png")
        val uri = FileProvider.getUriForFile(
            context,
            "com.application.timer_dmb.provider",
            file)


        return uri
    }

    fun getImage(bitmap: Bitmap): Uri?{
        saveBitmapAsFile(bitmap)
        return getImageFromCacheUri()
    }

    private fun getChat(){
        if (isAuthorizedUseCase()){
            getGlobalChatUseCase().onEach {result ->
                when(result){
                    is Resource.Error -> {
                        _chatsState.value = ChatsState.Error(
                            message = result.message ?: "Unknown error",
                            code = result.code
                        )
                        Log.e("profile", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> _chatsState.value = ChatsState.Loading()
                    is Resource.Success -> {
                        _chatsState.value = ChatsState.Success(data = result.data!!)
                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    fun sendTimer(){
        viewModelScope.launch {
            val image = async(Dispatchers.Default) {
                toByteArray()
            }.await()

            _chatsState.value?.data?.let {
                sendMessageUseCase(text = "", replyToId = "", chatId = it.id,  image = image).collect{ result ->
                    when (result) {
                        is Resource.Error -> {
                            _singleMessageState.value = SingleMessageState.Error(
                                message = result.message ?: "Unknown error",
                                code = result.code
                            )
                            Log.e("timer_send", result.code.toString() + " " + result.message)
                        }
                        is Resource.Loading -> {
                            _singleMessageState.value = SingleMessageState.Loading()
                        }
                        is Resource.Success -> {
                            Log.i("send", result.data.toString())
                            _singleMessageState.value = SingleMessageState.Success(data = result.data!!)
                            _shareImageBitmap.value = null
                        }
                    }
                }
            } ?: run {
                _singleMessageState.value = SingleMessageState.Error(
                    message = "Не удалось получить данные об общем чате",
                    code = -1
                )
            }


        }
    }

    private fun getSoldierData(){
        if (isAuthorizedUseCase()) {


            getTimerDataUseCase().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("get_timer_e", result.code.toString() + " " + result.message)
                    }

                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val instantStart = Instant.ofEpochMilli(result.data!!.startTime)
                        val instantEnd = Instant.ofEpochMilli(result.data.endTime)

                        val localDateTimeStart =
                            LocalDateTime.ofInstant(instantStart, ZoneId.systemDefault())
                        val localDateTimeEnd =
                            LocalDateTime.ofInstant(instantEnd, ZoneId.systemDefault())
                        if (localDateTimeStart != _state.value.dateStart || localDateTimeEnd != _state.value.dateEnd) {
                            _state.value = _state.value.copy(
                                dateStart = localDateTimeStart,
                                dateEnd = localDateTimeEnd
                            )
                        }
                        countDate(state.value.dateEnd!!, state.value.dateStart!!)
                    }
                }

            }.launchIn(viewModelScope)
        } else {
            val data = getSoldierDataUseCase()
            val dateEndAsDate = LocalDateTime.parse(data[2], formatter)
            val dateStartAsDate = LocalDateTime.parse(data[1], formatter)
            if (dateStartAsDate != _state.value.dateStart || dateEndAsDate != _state.value.dateEnd){
                _state.value = _state.value.copy(
                    dateStart = dateStartAsDate,
                    dateEnd = dateEndAsDate
                )
            }
            countDate(state.value.dateEnd!!, state.value.dateStart!!)
        }

    }

    fun sendBitmap(bitmap: ImageBitmap){
        _shareImageBitmap.value = bitmap
        Log.i("bitmap_share", _shareImageBitmap.value.toString())
        sendTimer()
    }

    private fun toByteArray(): ByteArray{
        val stream = ByteArrayOutputStream()
        _shareImageBitmap.value?.asAndroidBitmap()?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        stream.close()
        return image
    }

    private fun countDate(dateEnd: LocalDateTime, dateStart: LocalDateTime){
        val currentDate = LocalDateTime.now()
        val durationLeft = Duration.between(currentDate, dateEnd)

        val durationPast = Duration.between(dateStart, currentDate)

        val daysPast = durationPast.toDays()
        val hoursPast = durationPast.toHours() % 24
        val minutesPast = durationPast.toMinutes() % 60
        val secondsPast = durationPast.seconds % 60

        val daysLeft = durationLeft.toDays()
        val hoursLeft = durationLeft.toHours() % 24
        val minutesLeft = durationLeft.toMinutes() % 60
        val secondsLeft = durationLeft.seconds % 60

        val duration = Duration.between(dateStart, dateEnd)

        val percentage = (durationPast.seconds.toDouble() / duration.seconds * 100).toString()
        val percentageString = ((if (percentage.length > 9) percentage.take(9) else percentage) + "%").replace(".", ",")

        _state.value = _state.value.copy(
            daysLeft = daysLeft,
            hoursLeft = hoursLeft,
            minutesLeft = minutesLeft,
            secondsLeft = secondsLeft,
            daysPast = daysPast,
            hoursPast = hoursPast,
            minutesPast = minutesPast,
            secondsPast = secondsPast,
            percentage = percentageString,
            percentageDouble = percentage.toDouble()
        )
    }

    private fun getSelfUser(){
        if (isAuthorizedUseCase()){
            getSelfUserDataUseCase().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _profileState.value = ProfileState.Error(message = result.message ?: "Unknown error", code = result.code)
                        Log.e("share_picture", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> _profileState.value = ProfileState.Loading()
                    is Resource.Success -> _profileState.value = ProfileState.Success(data = result.data!!)
                }
            }.launchIn(viewModelScope)
        }
    }

}