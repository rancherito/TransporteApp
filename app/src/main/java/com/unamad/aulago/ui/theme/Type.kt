package com.unamad.aulago.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.sp
import com.unamad.aulago.R

// Set of Material typography styles to start with

val SonoMono = FontFamily(
    Font(R.font.sono_regular, FontWeight.W200),
    Font(R.font.sono_regular, FontWeight.W300),
    Font(R.font.sono_regular, FontWeight.W400),
    Font(R.font.sono_regular, FontWeight.Normal),
    Font(R.font.sono_regular, FontWeight.W500),
    Font(R.font.sono_medium, FontWeight.Medium),
    Font(R.font.sono_semibold, FontWeight.W600),
    Font(R.font.sono_semibold, FontWeight.Bold)
)
val QuickSandFont = FontFamily(
    Font(R.font.quicksand_regular, FontWeight.W200),
    Font(R.font.quicksand_medium, FontWeight.W300),
    Font(R.font.quicksand_medium, FontWeight.W400),
    Font(R.font.quicksand_medium, FontWeight.Normal),
    Font(R.font.quicksand_medium, FontWeight.W500),
    Font(R.font.quicksand_medium, FontWeight.Medium),
    Font(R.font.quicksand_bold, FontWeight.W600),
    Font(R.font.quicksand_bold, FontWeight.Bold)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = QuickSandFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = QuickSandFont,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = QuickSandFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)