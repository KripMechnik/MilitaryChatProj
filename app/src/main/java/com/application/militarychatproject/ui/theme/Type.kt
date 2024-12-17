package com.application.militarychatproject.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.application.militarychatproject.R

// Set of Material typography styles to start with

val manropeFamily = FontFamily(
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_light, FontWeight.Light),
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_extralight, FontWeight.ExtraLight)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 60.sp,
        lineHeight = 60.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    )
)

