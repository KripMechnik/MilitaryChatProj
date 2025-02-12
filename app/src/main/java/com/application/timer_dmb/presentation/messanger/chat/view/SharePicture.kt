package com.application.timer_dmb.presentation.messanger.chat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.application.timer_dmb.R
import com.application.timer_dmb.presentation.home.view.ShowProgress
import com.application.timer_dmb.ui.theme.MilitaryChatProjectTheme
import com.application.timer_dmb.ui.theme.White
import com.application.timer_dmb.ui.theme.manropeFamily

@Composable
fun SharePicture(
    modifier: Modifier = Modifier,
    background: ImageBitmap? = null,
    pictureUrl: String,
    nickname: String,
    percentage: String,
    daysLeft: String
) {
    Box(
        modifier = modifier
            .size(width = 220.dp, height = 400.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(

                    if (background != null){
                        BitmapPainter(background)
                    } else {
                        painterResource(id = R.drawable.background_profile)
                    },
                    contentScale = ContentScale.FillHeight
                )

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        model = pictureUrl,
                        contentDescription = "Avatar image",
                        error = painterResource(R.drawable.no_avatar),
                        placeholder = ColorPainter(MaterialTheme.colorScheme.onPrimaryContainer)
                    )

                    Text(
                        text = nickname,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 11.sp),
                        color = White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(4.dp))
                            .padding(5.dp),
                        painter = painterResource(R.drawable.share),
                        contentDescription = "share",
                        tint = White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.Start
                    ) {

                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){
                            Text(
                                modifier = Modifier
                                    .padding(start = 6.dp),
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(
                                        fontFamily = manropeFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 40.sp,
                                        letterSpacing = 0.sp,
                                        color = White
                                    )
                                    ) {
                                        append(daysLeft)
                                    }

                                },

                            )

                            Text(
                                text = " дней до\nдембеля",
                                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 15.sp, lineHeight = 15.sp),
                                color = White
                            )
                        }


                        ShowProgress(score = if (percentage.isNotBlank()) percentage.takeWhile { it != ',' }.toInt() else 0, nearestEvent =  101)
                    }
                }
            }
        }
    }

}



@Preview
@Composable
private fun SharedPicturePreview() {
    MilitaryChatProjectTheme {
        SharePicture(
            pictureUrl = "",
            nickname = "@random_nickname",
            daysLeft = "60",
            percentage = "15,362883%",
        )
    }
}
