package com.localpk.app.common_components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GreenBox(modifier: Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
    ) {
        content()
    }
}