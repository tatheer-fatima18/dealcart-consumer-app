package com.localpk.app.common_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.AppTypography
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.increase
import dealcart.composeapp.generated.resources.minus
import dealcart.composeapp.generated.resources.plus
import org.jetbrains.compose.resources.painterResource

@Composable
fun NewCounter(
    itemQuantity: Int,
    onAddClick: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onDelete: () -> Unit,
    isUpdatingQuantity: Boolean = false
) {
    if (itemQuantity <= 0 && !isUpdatingQuantity) {
        Image(
            painter = painterResource(Res.drawable.increase),
            contentDescription = "Increment",
            modifier = Modifier
                .width(32.dp)
                .height(32.dp)
                .clickable { onAddClick() }
        )
    } else {
        Row(
            modifier = Modifier
                .height(32.dp)
                .fillMaxWidth()
                .border(
                    BorderStroke(1.dp, BorderPrimaryDefault),
                    RoundedCornerShape(8.dp)
                )
                .background(
                    color = SurfaceBackground,
                    shape = RoundedCornerShape(8.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(
                onClick = if (isUpdatingQuantity) { {} } else { if (itemQuantity == 1) onDelete else onDecrement },
                enabled = !isUpdatingQuantity
            ) {
                Icon(
                    painter = painterResource(Res.drawable.minus),
                    contentDescription = "Decrement",
                    modifier = Modifier
                        .width(12.dp)
                        .height(12.dp),
                    tint = if (isUpdatingQuantity) Color.Gray else Color.Unspecified
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally),
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
                        text = "$itemQuantity",
                        style = AppTypography().bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 23.3.sp,
                            color = TextSubdued
                        )
                    )
                }
            }

            IconButton(
                onClick = if (isUpdatingQuantity) { {} } else onIncrement,
                enabled = !isUpdatingQuantity
            ) {
                Icon(
                    painter = painterResource(Res.drawable.plus),
                    contentDescription = "Increment",
                    modifier = Modifier.size(18.dp),
                    tint = if (isUpdatingQuantity) Color.Gray else Color.Unspecified
                )
            }
        }
    }
}