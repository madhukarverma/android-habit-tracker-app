package com.habbittracker.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val HabitTrackerColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = TextLight,
    secondary = LightGreen,
    onSecondary = TextDark,
    background = CreamBackground,
    onBackground = TextDark,
    surface = CardBackground,
    onSurface = TextDark,
    surfaceVariant = CreamBackground,
    onSurfaceVariant = TextDark
)

@Composable
fun HabitTrackerTheme(content: @Composable () -> Unit) {
    val colorScheme = HabitTrackerColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CreamBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
