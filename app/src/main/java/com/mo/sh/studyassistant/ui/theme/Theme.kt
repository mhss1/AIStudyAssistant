package com.mo.sh.studyassistant.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = PrimaryColor,
    tertiary = TertiaryColor,
    secondary = SecondaryColor,
    surface = DarkGray,
    surfaceTint = DarkGray,
    surfaceVariant = SurfaceVariantDark,
    background = Color.Black,
    onSurface = Color.White,
    onBackground = Color.White,
)

private val LightColorPalette = lightColorScheme(
    primary = PrimaryColor,
    tertiary = TertiaryColor,
    secondary = SecondaryColor,
    background = Color.White,
    surface = SurfaceColorLight,
    surfaceTint = SurfaceColorLight,
    surfaceVariant = SurfaceColorLight
)

@Composable
fun StudyAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (darkTheme) DarkColorPalette
        else LightColorPalette

    val systemUiController = rememberSystemUiController()
    SideEffect {
    // On samsung devices, Color.Transparent does not make it fully transparent, so we use AlmostTransparent (#01FFFFFF)
        val color = if (Build.MANUFACTURER.equals("samsung", ignoreCase = true)) AlmostTransparent else Color.Transparent
        systemUiController.setSystemBarsColor(
            color = color,
            darkIcons = !darkTheme
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}