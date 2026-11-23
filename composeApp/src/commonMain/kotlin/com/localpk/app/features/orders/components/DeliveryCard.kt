package com.localpk.app.features.orders.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.formatCartDeliveryDate
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.express_del
import dealcart.composeapp.generated.resources.standard_del
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource

@Composable
fun DeliveryCard(deliveryType: String?, deliveryFee: Int?, deliveryDate: String?) {
    val iconRes =
        if (deliveryType == DeliveryType.EXPRESS.key) Res.drawable.express_del else Res.drawable.standard_del
    val type =
        if (deliveryType == DeliveryType.EXPRESS.key) DeliveryType.EXPRESS.displayName else DeliveryType.STANDARD.displayName
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderPrimaryDefault,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Delivery option",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = TextSubdued
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )


                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = type,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = TextSubdued
                        )

                        Text(
                            text = if (deliveryType == DeliveryType.EXPRESS.key) today.formatCartDeliveryDate() else deliveryDate.formatCartDeliveryDate(
                                showFullMonth = true
                            ),
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                            )
                        )
                    }


                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = if (deliveryType == DeliveryType.EXPRESS.key) deliveryFee.getValueOrDefault()
                            .toDouble().formatPKRCurrency() else "FREE",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubdued
                    )
                }
            }
        }
    }
}
