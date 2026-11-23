package com.localpk.app.features.cart.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.theming.IconDanger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 300,
    swipeDeleteBgColor: Color = IconDanger,
    content: @Composable (T) -> Unit
) {
    var itemToConfirm by remember { mutableStateOf<T?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                coroutineScope.launch {
                    delay(animationDuration.toLong())
                    itemToConfirm = item
                }
                false
            } else {
                true
            }
        }
    )

    SwipeToDismissBox(
        state = state,
        backgroundContent = {
            SwipeDeleteBackground(
                swipeDismissState = state,
                swipeDeleteBgColor = swipeDeleteBgColor
            )
        },
        enableDismissFromStartToEnd = false
    ) {
        content(item)
    }

    itemToConfirm?.let {
        onDelete(item)
        itemToConfirm = null
    }
}

@Composable
fun SwipeDeleteBackground(
    swipeDismissState: SwipeToDismissBoxState,
    swipeDeleteBgColor: Color
) {
    val color by animateColorAsState(
        targetValue = when (swipeDismissState.currentValue) {
            SwipeToDismissBoxValue.Settled, SwipeToDismissBoxValue.EndToStart -> swipeDeleteBgColor
            else -> Color.Transparent
        },
        label = ""
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color, RoundedCornerShape(11.dp))
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}