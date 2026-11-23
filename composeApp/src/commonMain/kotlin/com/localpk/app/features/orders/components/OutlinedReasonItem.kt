package com.localpk.app.features.orders.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.orders.data.CancelOrderReason
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubduedHalf

@Composable
fun OutlinedReasonItem(
    reason: CancelOrderReason,
    isSelected: Boolean,
    onSelected: (CancelOrderReason) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .padding(start = 6.dp)
        ) {
            RadioButton(
                selected = isSelected,
                onClick = {
                    reason.isSelected = true
                    onSelected(reason)
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = ButtonSurfaceBrand,
                    unselectedColor = TextSubduedHalf
                )
            )
        }

        Text(
            text = reason.reason,
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 14.sp,
            color = Color.Black,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily()
        )
    }
}

