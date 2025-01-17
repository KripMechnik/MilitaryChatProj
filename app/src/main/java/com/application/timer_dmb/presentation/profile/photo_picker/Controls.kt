package com.application.timer_dmb.presentation.profile.photo_picker

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.application.timer_dmb.R
import com.mr0xf00.easycrop.CropState

@Composable
fun Controls(state: CropState) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ButtonsBar(modifier = Modifier) {
            IconButton(onClick = { state.rotLeft() }) {
                Icon(painter = painterResource(R.drawable.rotate_left), null)
            }
            IconButton(onClick = { state.rotRight() }) {
                Icon(
                    modifier = Modifier
                        .graphicsLayer(scaleX = -1f),
                    painter = painterResource(R.drawable.rotate_left), contentDescription =  null)
            }
            IconButton(onClick = { state.flipHorizontal() }) {
                Icon(painter = painterResource(R.drawable.mirror_horizontally), null)
            }
            IconButton(onClick = { state.flipVertical() }) {
                Icon(
                    modifier = Modifier
                        .graphicsLayer(rotationZ = 90f),
                    painter = painterResource(R.drawable.mirror_horizontally), contentDescription =  null)
            }
        }
    }

}

@Composable
private fun ButtonsBar(
    modifier: Modifier = Modifier,
    buttons: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = .8f),
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom
        ) {
            buttons()
        }
    }
}