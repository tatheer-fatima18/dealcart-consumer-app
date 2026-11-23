package com.localpk.app.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Android implementation: Sets status bar icon color dynamically
 */
@Composable
actual fun SetStatusBarIconColor(isLightBackground: Boolean) {
    val activity = LocalActivity.current
    
    LaunchedEffect(isLightBackground) {
        if (activity != null) {
            activity.window?.let { window ->
                val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
                // isAppearanceLightStatusBars = true means dark icons (for light backgrounds)
                // isAppearanceLightStatusBars = false means light icons (for dark backgrounds)
                windowInsetsController.isAppearanceLightStatusBars = isLightBackground
            }
        }
    }
}
