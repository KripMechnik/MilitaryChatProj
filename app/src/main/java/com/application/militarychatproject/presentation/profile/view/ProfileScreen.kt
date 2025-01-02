package com.application.militarychatproject.presentation.profile.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.application.militarychatproject.R
import com.application.militarychatproject.presentation.presets.ButtonPreset
import com.application.militarychatproject.presentation.profile.DeleteAccState
import com.application.militarychatproject.presentation.profile.LogoutState
import com.application.militarychatproject.presentation.profile.ProfileScreenPresenter
import com.application.militarychatproject.presentation.profile.ProfileState
import com.application.militarychatproject.presentation.profile.ResultCropState
import com.application.militarychatproject.presentation.profile.SendCropState
import com.application.militarychatproject.presentation.profile.photo_picker.Controls
import com.application.militarychatproject.presentation.profile.photo_picker.setAspect
import com.application.militarychatproject.ui.theme.MilitaryChatProjectTheme
import com.application.militarychatproject.ui.theme.White
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
fun ProfileScreen(
    presenter: ProfileScreenPresenter
) {

    val scope = rememberCoroutineScope()

    val state by presenter.state.collectAsState()

    val profileState by presenter.profileState.collectAsState()

    val sendCropState by presenter.sendCropState.collectAsState()

    val cropState by presenter.cropState.collectAsState()

    val deleteAccState by presenter.deleteAccState.collectAsState()

    var loginTextField by remember(profileState) {
        mutableStateOf(profileState?.data?.nickname ?: "")
    }

    var emailTextField by remember(profileState) {
        mutableStateOf(profileState?.data?.login ?: "")
    }

    LaunchedEffect(state) {
        if (state is LogoutState.Success){
            presenter.navigateToMenu()
        }
    }

    LaunchedEffect(deleteAccState) {
        if (deleteAccState is DeleteAccState.Success){
            presenter.navigateToMenu()
        }
    }

    LaunchedEffect (cropState) {
        if (cropState is ResultCropState.Error){
            Log.e("crop", cropState?.message ?: "Unknown error")
        } else if (cropState is ResultCropState.Success){
            Log.i("crop", cropState?.data.toString())
            presenter.sendPhoto()
        }
    }

    LaunchedEffect (sendCropState) {
        if (sendCropState is SendCropState.Success){
            presenter.getPhoto()
        }
    }

    val imageCropper = rememberImageCropper()

    val context = LocalContext.current

    val localCropState = imageCropper.cropState



    if (localCropState != null){
        val aspect = AspectRatio(1, 1)
        ImageCropperDialog(
            state = localCropState,
            cropControls = { Controls(it)}
        )
        localCropState.shape = CircleCropShape
        localCropState.region = localCropState.region.setAspect(aspect)
        localCropState.aspectLock = true
    }

    var openAlertDialog by remember { mutableStateOf(false) }

    var dialogTitle by remember {
        mutableStateOf("")
    }

    var dialogText by remember {
        mutableStateOf("")
    }

    var dialogAction by remember {
        mutableStateOf({})
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

    if (openAlertDialog){
        ConfirmDialog(
            dialogTitle = dialogTitle,
            dialogText = dialogText,
            onDismissRequest = {openAlertDialog = false},
            onConfirmation = {
                openAlertDialog = false
                dialogAction()
            }
        )
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 100.dp, 30.dp, 90.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Редактирование\nпрофиля",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        Box (
            modifier = Modifier
                .size(96.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .clickable {
                        imagePicker.pick()
                    },
                model = profileState?.data?.avatarLink,
                contentDescription = "Avatar image",
                error = painterResource(R.drawable.no_avatar),
                placeholder = ColorPainter(MaterialTheme.colorScheme.onPrimaryContainer)
            )
            Icon(
                modifier = Modifier
                    .padding(start = 64.dp, top = 64.dp)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
                imageVector = Icons.Default.Create,
                contentDescription = "edit"
            )
        }

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = loginTextField,
            onValueChange = {loginTextField = it},
            textStyle = MaterialTheme.typography.labelMedium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = if(loginTextField != profileState?.data?.nickname) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    text = "Логин",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            singleLine = true
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = emailTextField,
            onValueChange = {emailTextField = it},
            textStyle = MaterialTheme.typography.labelMedium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = if(emailTextField != profileState?.data?.login) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    text = "Почта",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            singleLine = true
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
        ) {
            ButtonPreset(
                contentColor = White,
                containerColor = MaterialTheme.colorScheme.secondary,
                content = {
                    Text(
                        text = "Сохранить",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            ) { }

            ButtonPreset(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                content = {
                    Text(
                        text = "Удалить аккаунт",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            ) {
                dialogAction = { presenter.delete() }
                dialogTitle = "Удалить аккаунт?"
                dialogText = "Вы уверены, что хотите удалить аккаунт?"
                openAlertDialog = true
            }

            ButtonPreset(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                content = {
                    if (state is LogoutState.Loading){
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = "Выйти из аккаунта",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            ) {
                dialogAction = { presenter.logout() }
                dialogTitle = "Выйти из аккаунта?"
                dialogText = "Вы уверены, что хотите выйти из аккаунта?"
                openAlertDialog = true
            }
        }
    }
}

@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle, style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Text(text = dialogText, style = MaterialTheme.typography.bodySmall)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Подтвердить", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
        },

        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Отменить", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    )
}

@Preview
@Composable
private fun ConfirmDialogPreview() {
    MilitaryChatProjectTheme {
        ConfirmDialog(
            onDismissRequest = {},
            onConfirmation = {},
            dialogTitle = "Удалить аккаунт?",
            dialogText = "Вы уверены, что хотите удалить аккаунт?"
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {

    val presenter = object: ProfileScreenPresenter{

        override val cropState: StateFlow<ResultCropState?>
            get() = MutableStateFlow(null)

        override val state: StateFlow<LogoutState?>
            get() = MutableStateFlow(null)

        override val profileState: StateFlow<ProfileState?>
            get() = MutableStateFlow(ProfileState.Loading())

        override val sendCropState: StateFlow<SendCropState?>
            get() = MutableStateFlow(null)

        override val deleteAccState: StateFlow<DeleteAccState?>
            get() = MutableStateFlow(null)

        override fun logout() {

        }

        override fun navigateToMenu() {

        }

        override fun setCropState(bitmap: ImageBitmap) {

        }

        override fun sendPhoto() {

        }

        override fun getPhoto() {

        }

        override fun delete() {

        }
    }

    MilitaryChatProjectTheme {
        ProfileScreen(presenter)
    }

}