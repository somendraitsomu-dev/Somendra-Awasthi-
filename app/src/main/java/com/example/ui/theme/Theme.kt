package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ApexCyan,
    onPrimary = ApexTextInverse,
    primaryContainer = ApexCyanDim,
    onPrimaryContainer = ApexTextPrimary,
    secondary = ApexEmerald,
    onSecondary = ApexTextInverse,
    secondaryContainer = ApexEmeraldDark,
    onSecondaryContainer = ApexEmerald,
    tertiary = ApexCrimson,
    onTertiary = ApexTextPrimary,
    background = ApexCanvas,
    onBackground = ApexTextPrimary,
    surface = ApexSurface,
    onSurface = ApexTextPrimary,
    surfaceVariant = ApexSurfaceCard,
    onSurfaceVariant = ApexTextSecondary,
    outline = ApexOutline,
    outlineVariant = ApexOutlineVariant
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
