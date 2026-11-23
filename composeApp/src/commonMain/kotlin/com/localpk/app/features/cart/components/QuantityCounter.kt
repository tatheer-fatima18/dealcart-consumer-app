package com.localpk.app.features.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.minus
import dealcart.composeapp.generated.resources.plus
import org.jetbrains.compose.resources.painterResource

@Composable
fun QuantityCounter(
    quantity: Int,
    decreaseCounter: () -> Unit,
    increaseCounter: () -> Unit,
    isUpdatingQuantity: Boolean = false
) {
    Row(
        modifier = Modifier
            .height(34.dp)
            .border(
                BorderStroke(1.dp, BorderPrimaryDefault),
                RoundedCornerShape(8.dp)
            )
            .background(
                color = SurfaceBackground,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = if (isUpdatingQuantity) {
                {}
            } else {
                decreaseCounter
            },
            enabled = !isUpdatingQuantity
        ) {
            Icon(
                painter = painterResource(Res.drawable.minus),
                contentDescription = "Decrement",
                modifier = Modifier.size(14.dp),
                tint = if (isUpdatingQuantity) Color.Gray else TextSubdued
            )
        }

        if (isUpdatingQuantity) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = TextSubdued,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "$quantity",
                modifier = Modifier
                    .width(28.dp),
                style = TextStyle(
                    color = TextSubdued,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamily(),
                    textAlign = TextAlign.Center
                )
            )
        }

        IconButton(
            onClick = if (isUpdatingQuantity) {
                {}
            } else {
                increaseCounter
            },
            enabled = !isUpdatingQuantity
        ) {
            Icon(
                painter = painterResource(Res.drawable.plus),
                contentDescription = "Increment",
                modifier = Modifier.size(14.dp),
                tint = if (isUpdatingQuantity) Color.Gray else TextSubdued
            )
        }
    }
}
