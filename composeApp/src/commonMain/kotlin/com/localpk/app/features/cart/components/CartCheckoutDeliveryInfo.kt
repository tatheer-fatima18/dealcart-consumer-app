package com.localpk.app.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.theming.BarlowCondensedFontFamily
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.formatCartDeliveryDate
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.express_del
import dealcart.composeapp.generated.resources.menu_drop_down
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource

@Composable
fun CartCheckoutDeliveryInfo(
    storeName: String,
    isPostexDelivery: Boolean,
    formattedDeliveryDate: String,
    onClickDeliveryFee: () -> Unit,
    deliveryType: DeliveryType,
    isDeliverySelectionAvailable: Boolean
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = storeName,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued
                    )
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append("Fulfilled by")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            if (isPostexDelivery) append("Partner") else append(
                                "DealCart"
                            )
                        }

                    },
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubdued
                    )
                )
            }

            Row {
                if (deliveryType == DeliveryType.EXPRESS) {
                    Image(
                        painter = painterResource(Res.drawable.express_del),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                }
                Column {
                    Row {
                        Text(
                            text = if (isPostexDelivery.not()) deliveryType.displayName else DeliveryType.STANDARD.displayName,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = BarlowCondensedFontFamily(),
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        )
                        if (isDeliverySelectionAvailable)
                            Image(
                                modifier = Modifier.clickable { onClickDeliveryFee() }
                                    .padding(top = 3.dp),
                                painter = painterResource(Res.drawable.menu_drop_down),
                                contentDescription = null,
                            )
                    }
                    Text(
                        text = if (deliveryType == DeliveryType.EXPRESS) today.formatCartDeliveryDate() else formattedDeliveryDate,
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = if (deliveryType == DeliveryType.EXPRESS) ColorPrimary else TextPrimary
                        )
                    )
                }
            }
        }
    }
}
