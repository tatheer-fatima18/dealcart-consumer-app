package com.localpk.app.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val brandPrimary: Color,
    val danger: Color,
    val customSurface: Color,
    // Add your custom colors here
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        brandPrimary = Color.Unspecified,
        danger = Color.Unspecified,
        customSurface = Color.Unspecified
    )
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColorPalette() else lightColorPalette()

    MaterialTheme(
        colorScheme = colors.materialColors,
        typography = AppTypography(),
        shapes = AppShapes,
        content = {
            CompositionLocalProvider(
                LocalExtendedColors provides colors.extendedColors,
                content = content
            )
        }
    )
}

private fun darkColorPalette(): AppColors {
    return AppColors(
        materialColors = DarkColorScheme,
        extendedColors = ExtendedColors(
            brandPrimary = Color(0xFF6200EE),
            danger = Color(0xFFCF6679),
            customSurface = Color(0xFF1E1E1E)
        )
    )
}

private fun lightColorPalette(): AppColors {
    return AppColors(
        materialColors = LightColorScheme,
        extendedColors = ExtendedColors(
            brandPrimary = Color(0xFF3700B3),
            danger = Color(0xFFB00020),
            customSurface = Color(0xFFF5F5F5)
        )
    )
}

data class AppColors(
    val materialColors: ColorScheme,
    val extendedColors: ExtendedColors
)