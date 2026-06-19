package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    secondary = BlueSecondary,
    tertiary = AmberTertiary,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    primaryContainer = Color(0xFFE6E1FF),
    secondaryContainer = Color(0xFFDDF8FB),
    tertiaryContainer = Color(0xFFE0F8E9),
    onPrimaryContainer = LightOnSurface,
    onSecondaryContainer = LightOnSurface,
    onTertiaryContainer = LightOnSurface,
    errorContainer = Color(0xFFFFE5E9),
    onErrorContainer = Color(0xFF8B1E12),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    primaryContainer = DarkSurfaceVariant,
    secondaryContainer = Color(0xFF1D4D55),
    tertiaryContainer = Color(0xFF24523A),
    onPrimaryContainer = DarkOnSurface,
    onSecondaryContainer = DarkOnSurface,
    onTertiaryContainer = DarkOnSurface,
    errorContainer = Color(0xFF5B241C),
    onErrorContainer = Color(0xFFFFDAD3),
    onPrimary = DarkBackground,
    onSecondary = DarkBackground,
    onTertiary = DarkBackground,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant
)
@Composable
fun HomeworkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = Typography
    )
}
