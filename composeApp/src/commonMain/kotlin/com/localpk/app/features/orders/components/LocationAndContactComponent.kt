package com.localpk.app.features.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.StaticMap
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.call_me_at
import dealcart.composeapp.generated.resources.default_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun LocationAndContactComponent(
    defaultPhoneNumber: String,
    secondaryPhoneNumber: String,
    latitude: Double,
    longitude: Double,
    shippingCity: String,
    shippingAddressText: String
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .padding(6.dp)
            .background(
                color = Color.White,
                RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = BorderPrimaryDefault,
                shape = RoundedCornerShape(8.dp)
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = shippingCity,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = TextSubdued,
                        )
                    )
                    Text(
                        text = shippingAddressText,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            color = TextSubdued
                        ),
                    )
                }
            }


            val initialPosition = Geolocation(latitude, longitude)
            StaticMap(
                modifier = Modifier
                    .fillMaxSize()
                    .height(88.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                location = initialPosition
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = SurfaceBackground,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Phone: ")
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                )
                            ) {
                                append(defaultPhoneNumber)
                            }
                        },
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = TextSubdued,
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier.padding(end = 4.dp),
                        text = stringResource(Res.string.default_label),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = TextSubduedHalf,
                        )
                    )
                }
                if (secondaryPhoneNumber.isNotEmpty()) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        thickness = 1.dp,
                        color = BorderPrimaryDefault
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                if (secondaryPhoneNumber.isNotEmpty()) {
                                    withStyle(
                                        SpanStyle(
                                            color = TextSubdued
                                        )
                                    ) {
                                        append(stringResource(Res.string.call_me_at))
                                    }
                                    append(" ")
                                    withStyle(
                                        SpanStyle(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextSubdued
                                        )
                                    ) {
                                        append(secondaryPhoneNumber)
                                    }
                                }
                            },
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily(),
                            )
                        )
                    }
                }
            }
        }
    }
}