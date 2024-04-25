package org.guardevour.developerdiary.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary =  Color(0xffaac7ff),
    secondary = Color(0xffbec6dc),
    tertiary = Color(0xFFADC7E2),
    background = Color(0xFF313846),
    surface =Color(0xFF111318),
    onPrimary = Color(0xff0a305f),
    onSecondary = Color(0xFF283141),
    onTertiary = Color(0xFF3f2844),
    onBackground = Color(0xFFFFFBFE),
    onSurface = Color(0xFFFFFBFE),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF415f91),
    secondary = Color(0xFF565f71),
    tertiary = Color(0xFF6D7E91),
    background = Color(0xFFDFD9DD),
    surface = Color(0xFFf9f9ff),
    onPrimary = Color(0xFFffffff),
    onSecondary = Color(0xFFffffff),
    onTertiary = Color(0xFFffffff),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)

)

@Composable
fun DeveloperDiaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}