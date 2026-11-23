package com.localpk.app.common_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.localpk.app.theming.TextSuccess
import com.localpk.app.theming.ErrorRedColor
import com.localpk.app.theming.PurpleNew
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.coroutines.delay

/**
 * Enum representing the different types of snackbar states
 */
enum class SnackbarType {
    SUCCESS,
    ERROR,
    DEFAULT
}

/**
 * Custom Snackbar composable that can be used across the app
 *
 * @param message The message to display in the Snackbar
 * @param type The type of snackbar (SUCCESS, ERROR, DEFAULT)
 * @param duration Duration in milliseconds to show the Snackbar (default: 2000ms)
 * @param showSnackbar Boolean that triggers the Snackbar when set to true
 * @param onSnackbarDismissed Callback function to reset the showSnackbar state to false
 */
@Composable
fun CustomSnackbar(
    message: String,
    type: SnackbarType = SnackbarType.DEFAULT,
    duration: Long = 2000L,
    showSnackbar: Boolean = false,
    onSnackbarDismissed: () -> Unit = {}
) {
    var isVisible by remember { mutableStateOf(false) }

    // Get background color based on snackbar type
    val backgroundColor = when (type) {
        SnackbarType.SUCCESS -> TextSuccess
        SnackbarType.ERROR -> ErrorRedColor
        SnackbarType.DEFAULT -> PurpleNew
    }

    // Get emoji and text color based on snackbar type
    val (emoji, textColor) = when (type) {
        SnackbarType.SUCCESS -> "😊" to Color.White
        SnackbarType.ERROR -> "😔" to Color.White
        SnackbarType.DEFAULT -> EMPTY_STRING to Color.White
    }

    // Watch for changes in showSnackbar parameter
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            isVisible = true
            delay(duration)
            isVisible = false
            onSnackbarDismissed() // Call the callback to reset the state
        }
    }

    // Display Snackbar when visible
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1000f), // High z-index to appear above other content
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 100.dp // More space above bottom navigation
                    )
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = backgroundColor.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (type != SnackbarType.DEFAULT)
                        Text(
                            text = emoji,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    Text(
                        text = message,
                        color = textColor,
                        fontSize = if (type == SnackbarType.DEFAULT) 16.sp else 14.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
} 