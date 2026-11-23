package com.localpk.app.common_components

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator

/**
 * Composable that provides iOS swipe-to-go-back gesture detection.
 * Creates a 20dp wide invisible area on the left edge of the screen
 * that detects horizontal drag gestures to trigger navigation back.
 */
@Composable
fun SwipeBackGestureArea(navigator: Navigator) {
    var offsetState by remember { mutableFloatStateOf(0f) }
    
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(20.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetState > 100) {
                            if (navigator.items.size > 1) navigator.pop()
                        }
                        offsetState = 0f
                    }
                ) { _, dragAmount ->
                    if (offsetState < 0) return@detectHorizontalDragGestures
                    offsetState += dragAmount
                }
            }
    )
} 