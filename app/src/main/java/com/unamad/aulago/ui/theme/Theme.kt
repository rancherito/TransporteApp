package com.unamad.aulago.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.unamad.aulago.ColorSchemeCustom


private val myLightThemeColor = ColorSchemeCustom()
private val myDarkThemeColor = ColorSchemeCustom(
    textDark = Color(0xFFFFFFFF),
    background = Color(0xFF191D1F),
    card = Color(0xFF272A2B),
    secondary = Color.White,
    body = Color(0xFFF7F7F7),
    text = Color(0xFFF7F7F7),
)

private val darkColorScheme = darkColorScheme(
    primary = myDarkThemeColor.primary,
    secondary = myDarkThemeColor.secondary,
    surfaceVariant = myDarkThemeColor.card,
    background = myDarkThemeColor.background,
    surface = myDarkThemeColor.text,
    onSurface = myDarkThemeColor.body,
)
private val lightColorScheme = lightColorScheme(
    primary = myLightThemeColor.primary,
    secondary = myLightThemeColor.secondary,
    surfaceVariant = myLightThemeColor.card,
    background = myLightThemeColor.background,
    surface = myLightThemeColor.text,
    onSurface = myLightThemeColor.body,

)

fun myColors(isDark: Boolean): ColorSchemeCustom {
    return if (isDark) myDarkThemeColor else myLightThemeColor
}
@Composable
fun myColors(): ColorSchemeCustom {
    return if (isDark()) myDarkThemeColor else myLightThemeColor
}

@Composable
fun isDark(): Boolean = isSystemInDarkTheme()

@Composable
fun WrapperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    primaryColor: Color? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }


    val theme = if (primaryColor != null) colorScheme.copy(primary = primaryColor) else colorScheme
    MaterialTheme(
        colorScheme = theme,
        typography = Typography,
        content = content,
        shapes = MaterialTheme.shapes.copy()
    )

}