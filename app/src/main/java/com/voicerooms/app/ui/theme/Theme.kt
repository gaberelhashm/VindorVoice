package com.voicerooms.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PurplePrimary = Color(0xFF7B2CBF)
private val PurpleDark = Color(0xFF5A189A)
private val PurpleLight = Color(0xFF9D4EDD)
private val PurpleAccent = Color(0xFFC77DFF)
private val BackgroundDark = Color(0xFF10002B)
private val SurfaceDark = Color(0xFF240046)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFE0AAFF)

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = TextPrimary,
    primaryContainer = PurpleDark,
    onPrimaryContainer = TextPrimary,
    secondary = PurpleLight,
    onSecondary = TextPrimary,
    tertiary = PurpleAccent,
    onTertiary = BackgroundDark,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF3C096C),
    onSurfaceVariant = TextSecondary
)

@Composable
fun VoiceRoomsTheme(
    darkTheme: Boolean = true, // Always dark purple theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
