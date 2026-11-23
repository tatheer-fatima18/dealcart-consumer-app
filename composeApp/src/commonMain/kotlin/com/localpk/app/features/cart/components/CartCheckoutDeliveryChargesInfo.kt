package com.localpk.app.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.IconDangerHalf
import com.localpk.app.theming.MovGreen
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_delivery_cart_multicity
import dealcart.composeapp.generated.resources.ic_delivery_cart_summary
import dealcart.composeapp.generated.resources.yay_free_delivery
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CartCheckoutDeliveryChargesInfo(
    isPostexDelivery: Boolean,
    formattedDeliveryFee: String
) {
    when (isPostexDelivery) {
        false -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(Res.string.yay_free_delivery),
                        style = TextStyle(
                            color = TextSubdued,
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        color = MovGreen,
                        trackColor = IconDangerHalf,
                        strokeCap = StrokeCap.Round,
                        progress = {
                            1.0f
                        },
                    )
                }
                Icon(
                    painter = painterResource(Res.drawable.ic_delivery_cart_multicity),
                    tint = ButtonSurfaceBrand,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        else -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFCE0B),
                                Color(0x00FFFFFF)
                            )
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(Res.drawable.ic_delivery_cart_summary),
                    contentDescription = null,
                )
                Text(
                    text = formattedDeliveryFee,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued
                    )
                )
            }
        }
    }
}