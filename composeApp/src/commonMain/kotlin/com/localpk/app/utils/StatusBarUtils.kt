package com.localpk.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color

/**
 * Sets the status bar icon color based on background color.
 * 
 * @param backgroundColor The background color of the current screen/component
 * 
 * Usage:
 * ```
 * StatusBarIconColor(backgroundColor = ColorPrimary) // Blue background -> light icons
 * StatusBarIconColor(backgroundColor = Color.White)   // White background -> dark icons
 * ```
 */
@Composable
fun StatusBarIconColor(backgroundColor: Color) {
    val isLight = isLightColor(backgroundColor)
    
    SetStatusBarIconColor(isLightBackground = isLight)
    
    // Reset to default when leaving composition
    DisposableEffect(Unit) {
        onDispose {
            // Reset to default (light background with dark icons) when screen is left
            // Note: This requires a way to call the function from DisposableEffect
            // For now, the next screen will set its own status bar color
        }
    }
}

/**
 * Determines if a color is light (should use dark icons) or dark (should use light icons)
 */
private fun isLightColor(color: Color): Boolean {
    // Calculate luminance to determine if color is light or dark
    // Using relative luminance formula: 0.299*R + 0.587*G + 0.114*B
    val luminance = 0.299 * color.red + 0.587 * color.green + 0.114 * color.blue
    // If luminance > 0.5, it's a light color (use dark icons)
    return luminance > 0.5
}

/**
 * Platform-specific composable function to set status bar icon color
 * @param isLightBackground true for light backgrounds (dark icons), false for dark backgrounds (light icons)
 */
@Composable
expect fun SetStatusBarIconColor(isLightBackground: Boolean)
