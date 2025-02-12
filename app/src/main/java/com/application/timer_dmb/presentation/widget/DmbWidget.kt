package com.application.timer_dmb.presentation.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import com.application.timer_dmb.MainActivity
import com.application.timer_dmb.R

class DmbWidget: GlanceAppWidget() {

    companion object {
        val percentageKey = stringPreferencesKey("percentage")
        val daysLeftKey = stringPreferencesKey("daysLeft")
    }

    override val sizeMode: SizeMode = SizeMode.Single

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val sharedPreferences = context.getSharedPreferences("dates", Context.MODE_PRIVATE)

        provideContent {



            Log.i("widgetUpdate", "update")

            val percentage = currentState(percentageKey) ?: sharedPreferences.getString("percentage", "0") ?: "100"
            val daysLeft = currentState(daysLeftKey) ?: sharedPreferences.getString("daysLeft", "0") ?: "0"


            Log.i("widget", daysLeft)

            GlanceTheme {
                Content(context, percentage, daysLeft)
            }
        }
    }

    @Composable
    private fun Content(context: Context, percentage: String, daysLeft: String){



        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(actionStartActivity<MainActivity>())
                .background(if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S)  ImageProvider(R.drawable.widget_background_v31) else ImageProvider(R.drawable.widget_background))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = GlanceModifier
                    .fillMaxSize(),
                provider = getImageProvider(context = context, percentage, daysLeft),
                contentDescription = null
            )
        }
    }

    private fun getImageProvider(context: Context, percentage: String, days: String): ImageProvider {
        val circularProgressBar = CircularProgressBar(context = context).apply {
            progress = percentage.toFloat()
            backgroundProgressBarWidth = 20f
            progressBarWidth = 20f
            roundBorder = true
            backgroundProgressBarColor = context.getColor(R.color.white_opacity)
            progressBarColor = context.getColor(R.color.white)
            layoutParams = LayoutParams(100,100)
            daysLeft = days
        }

        val frameLayout = FrameLayout(context).apply {
            layoutParams = LayoutParams(
                100, 100
            )
        }

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT, // Ширина
            FrameLayout.LayoutParams.WRAP_CONTENT // Высота
        )

        layoutParams.gravity = Gravity.CENTER

        frameLayout.addView(circularProgressBar)

        circularProgressBar.measure(
            View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.EXACTLY))
        circularProgressBar.layout(0, 0, circularProgressBar.measuredWidth, circularProgressBar.measuredHeight)

        val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        frameLayout.draw(canvas)
        return ImageProvider(bitmap)
    }
}

class DmbWidgetReceiver: GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget
        get() = DmbWidget()
}
