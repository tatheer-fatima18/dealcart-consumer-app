package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.minus
import dealcart.composeapp.generated.resources.plus
import dealcart.composeapp.generated.resources.quantity_productItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = modifier
            .clickable { onClick() }
            .size(32.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = CircleShape
            )
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = null,
            tint = TextSubdued,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun QuantitySelector(
    modifier: Modifier = Modifier,
    quantity: Int,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
    isUpdatingQuantity: Boolean = false
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(24.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = if (isUpdatingQuantity) { {} } else onDecrement,
            enabled = !isUpdatingQuantity,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.minus),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = if (isUpdatingQuantity) Color.Gray else TextSubdued
            )
        }

        Box(
            modifier = Modifier.width(28.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isUpdatingQuantity) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = TextSubdued,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "$quantity",
                    style = TextStyle(
                        color = TextSubdued,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PoppinsFontFamily(),
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        IconButton(
            onClick = if (isUpdatingQuantity) { {} } else onIncrement,
            enabled = !isUpdatingQuantity,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.plus),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = if (isUpdatingQuantity) Color.Gray else TextSubdued
            )
        }
    }
}