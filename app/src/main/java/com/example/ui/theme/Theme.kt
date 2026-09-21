package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonRed,
    onPrimary = TextWhite,
    primaryContainer = NeonRedDim,
    onPrimaryContainer = TextWhite,
    secondary = NeonGreen,
    onSecondary = CyberBlack,
    secondaryContainer = NeonGreenDim,
    onSecondaryContainer = NeonGreen,
    tertiary = CyberCyan,
    background = CyberBlack,
    onBackground = TextWhite,
    surface = CyberDark,
    onSurface = TextWhite,
    surfaceVariant = CyberCard,
    onSurfaceVariant = TextMuted,
    outline = CyberCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
