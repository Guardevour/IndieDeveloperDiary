package org.guardevour.developerdiary.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import org.guardevour.developerdiary.screens.Module

private val DarkColorScheme = darkColorScheme(
    primary =  Color(0xFF343C4E),
    secondary = Color(0xFF586870),
    tertiary = Color(0xFF909DB1),
    background = Color(0xFF313846),
    surface =Color(0xFF313846),
    onPrimary = Color(0xFFFFFBFE),
    onSecondary = Color(0xFFFFFBFE),
    onTertiary = Color(0xFFB3B6B9),
    onBackground = Color(0xFFFFFBFE),
    onSurface = Color(0xFFFFFBFE),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF999EAA),
    secondary = Color(0xFF8DB2BB),
    tertiary = Color(0xFFBFD4F3),
    background = Color(0xFFDFD9DD),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color(0xDA0F1213),
    onSecondary = Color(0xFF0F1013),
    onTertiary = Color(0xFF0F1013),
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