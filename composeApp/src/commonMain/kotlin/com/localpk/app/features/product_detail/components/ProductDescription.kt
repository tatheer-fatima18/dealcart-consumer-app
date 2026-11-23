package com.localpk.app.features.product_detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.AppTypography
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_baseline_keyboard_arrow_down_24
import dealcart.composeapp.generated.resources.ic_baseline_keyboard_arrow_up_24
import dealcart.composeapp.generated.resources.product_detail
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProductDescription(
    productDescription: String,
    productShortDescription: String,
    onClick: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.product_detail),
                style = AppTypography().bodyMedium.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            )
            if (productDescription.length > 82) {
                IconButton(
                    onClick = {
                        expanded = expanded.not()
                        onClick(expanded)
                    }
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(if (expanded) Res.drawable.ic_baseline_keyboard_arrow_up_24 else Res.drawable.ic_baseline_keyboard_arrow_down_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(TextSubduedHalf)
                    )
                }
            }
        }
        HorizontalDivider(thickness = 2.dp, color = BorderPrimaryDefault)
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            text = if (expanded) productDescription else productShortDescription,
            style = AppTypography().bodyMedium.copy(
                fontSize = 12.sp,
                color = TextSubdued
            )
        )
    }
}