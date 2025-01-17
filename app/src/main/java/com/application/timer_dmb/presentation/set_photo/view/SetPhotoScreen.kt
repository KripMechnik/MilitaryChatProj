package com.application.timer_dmb.presentation.set_photo.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.R
import com.application.timer_dmb.presentation.presets.ButtonPreset
import com.application.timer_dmb.presentation.profile.ResultCropState
import com.application.timer_dmb.presentation.profile.SendCropState
import com.application.timer_dmb.presentation.profile.photo_picker.Controls
import com.application.timer_dmb.presentation.profile.photo_picker.setAspect
import com.application.timer_dmb.presentation.set_photo.SetPhotoScreenPresenter
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import com.mr0xf00.easycrop.AspectRatio
import com.mr0xf00.easycrop.CircleCropShape
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.rememberImagePicker
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun SetPhotoScreen(
    presenter: SetPhotoScreenPresenter
) {



    val scope = rememberCoroutineScope()

    val imageCropper = rememberImageCropper()

    val context = LocalContext.current

    val localCropState = imageCropper.cropState

    val cropState = presenter.cropState.collectAsState()

    val sendCropState = presenter.sendCropState.collectAsState()

    LaunchedEffect(sendCropState.value) {
        if (sendCropState.value is SendCropState.Success){
            presenter.navigateToChooseType()
        }
    }

    if (localCropState != null){
        val aspect = AspectRatio(1, 1)
        ImageCropperDialog(
            state = localCropState,
            cropControls = { Controls(it) }
        )
        localCropState.shape = CircleCropShape
        localCropState.region = localCropState.region.setAspect(aspect)
        localCropState.aspectLock = true
    }

    val imagePicker = rememberImagePicker(onImage = { uri ->
        scope.launch {
            when (val result = imageCropper.crop(uri = uri, context = context)) {
                CropResult.Cancelled -> { }
                is CropError -> { }
                is CropResult.Success -> { presenter.setCropState(result.bitmap) }
            }

        }
    })

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 25.dp, end = 25.dp, top = 30.dp, bottom = 30.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Фото профиля",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Start
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ){
            Image(
                modifier = Modifier
                    .size(200.dp)

                    .background(color = MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        imagePicker.pick()
                    }
                    .padding(cropState.value?.data?.let { 0.dp } ?: 70.dp),
                painter = cropState.value?.data?.let { BitmapPainter(it) } ?: painterResource(R.drawable.add_avatar),
                contentDescription = "Avatar image",
            )
        }

        ButtonPreset(
            contentColor = White,
            enabled = cropState.value is ResultCropState.Success,
            containerColor = MaterialTheme.colorScheme.secondary,
            content = {
                    when (sendCropState.value) {
                    is SendCropState.Loading -> CircularProgressIndicator(color = White)
                    else -> Text(
                        "Сохранить",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        ) {
            presenter.sendPhoto()
        }

        ButtonPreset(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            label = "Пропустить",
        ) {
            presenter.navigateToChooseType()
        }

    }
}

@Preview
@Composable
private fun SetPhotoScreenPreview() {
    val presenter = object : SetPhotoScreenPresenter {

        override val sendCropState: StateFlow<SendCropState?>
            get() = MutableStateFlow(null)

        override val cropState: StateFlow<ResultCropState?>
            get() = MutableStateFlow(null)

        override fun setCropState(bitmap: ImageBitmap) {

        }

        override fun sendPhoto() {

        }

        override fun navigateToChooseType() {

        }

    }
    MilitaryChatProjectTheme {
        Surface (
            color = White
        ){
            SetPhotoScreen(presenter)
        }
    }
}