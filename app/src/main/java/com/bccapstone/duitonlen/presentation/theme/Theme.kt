package com.bccapstone.duitonlen.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Accent,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    // Additional commonly used slots to prevent purple defaults
    tertiary = Accent,
    primaryContainer = Primary,
    surfaceVariant = DarkSurface,
    outline = White.copy(alpha = 0.5f)
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Accent,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = White,
    onSecondary = White,
    onBackground = Black,
    onSurface = Black,
    // Additional commonly used slots to prevent purple defaults
    tertiary = Accent,
    primaryContainer = Primary,
    surfaceVariant = LightSurface,
    outline = Black.copy(alpha = 0.5f)
)



@Composable
fun DuitOnlenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}