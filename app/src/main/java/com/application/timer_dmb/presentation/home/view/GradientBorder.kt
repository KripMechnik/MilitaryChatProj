package com.application.timer_dmb.presentation.home.view


import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max


val gradientColorsBackground = listOf(
    Color(0xFFB40000),
    Color(0xFF222412),
    Color(0xFFB40000),
    Color(0xFF222412),
    Color(0xFFB40000),
)

val gradientColorsFront = listOf(
    Color(0xFFFFFA68),
    Color(0x00222412),
    Color(0xFFFFFA68),
    Color(0x00222412),
    Color(0xFFFFFA68),
)

fun Modifier.drawGradientBorder(
    strokeWidth: Dp,
    shape: Shape,
    brush: (Size) -> Brush = {
        Brush.sweepGradient(gradientColorsBackground)
    },
    durationMillis: Int
) = composed {

    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    Modifier
        .clip(shape)
        .drawWithCache {
            val strokeWidthPx = strokeWidth.toPx()

            val outline: Outline = shape.createOutline(size, layoutDirection, this)

            val pathBounds = outline.bounds

            onDrawWithContent {
                // This is actual content of the Composable that this modifier is assigned to
                drawContent()

                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)

                    // Destination

                    // We draw 2 times of the stroke with since we want actual size to be inside
                    // bounds while the outer stroke with is clipped with Modifier.clip

                    // 🔥 Using a maskPath with op(this, outline.path, PathOperation.Difference)
                    // And GenericShape can be used as Modifier.border does instead of clip
                    drawOutline(
                        outline = outline,
                        color = Color.Gray,
                        style = Stroke(strokeWidthPx * 2)
                    )

                    // Source
                    rotate(angle) {

                        drawCircle(
                            brush = brush(size),
                            radius = max(size.height, size.width),
                            blendMode = BlendMode.SrcIn,
                        )
                    }
                    restoreToCount(checkPoint)
                }
            }
        }
}

@Composable
fun Gradient(modifier: Modifier = Modifier) {

    if (Build.VERSION.SDK_INT > 31 ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .blur(4.dp)
                .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
        ){
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .drawGradientBorder(4.dp, durationMillis = 2000, shape = RoundedCornerShape(20.dp))
            )
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .blur(16.dp)
                .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
        ){
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .drawGradientBorder(4.dp, durationMillis = 2000, shape = RoundedCornerShape(20.dp))
            )
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .blur(32.dp)
                .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
        ){
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .drawGradientBorder(4.dp, durationMillis = 2000, shape = RoundedCornerShape(20.dp))
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
    ){
        Box(
            modifier = modifier
                .fillMaxSize()
                .drawGradientBorder(strokeWidth =  2.dp, brush = { Brush.sweepGradient(gradientColorsFront)}, durationMillis = 2000, shape = RoundedCornerShape(20.dp))
        )
    }
}