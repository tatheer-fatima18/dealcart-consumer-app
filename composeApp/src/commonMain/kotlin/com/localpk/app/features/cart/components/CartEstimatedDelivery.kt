package com.localpk.app.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BarlowCondensedFontFamily
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.estimated_delivery
import dealcart.composeapp.generated.resources.is_order_in_progress
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CartEstimatedDelivery(estimatedDeliveryTime: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(all = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .clickable { }
                .size(41.dp),
            painter = painterResource(Res.drawable.is_order_in_progress),
            contentDescription = null,
        )
        Spacer(Modifier.width(2.dp))
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.estimated_delivery),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = TextSubdued,
                )
            )
            Text(
                text = estimatedDeliveryTime,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = BarlowCondensedFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = ButtonSurfaceBrand,
                ),
            )
        }
    }
}