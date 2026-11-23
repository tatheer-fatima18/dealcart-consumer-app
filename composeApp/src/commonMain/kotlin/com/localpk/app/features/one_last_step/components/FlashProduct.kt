package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.GreenBox
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.flash_deals.data.PaymentStatus
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.OrderOutBg
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.baqaya_waqt
import dealcart.composeapp.generated.resources.expired
import dealcart.composeapp.generated.resources.flash_deal
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.ic_persons_members
import dealcart.composeapp.generated.resources.ic_time_remaining
import dealcart.composeapp.generated.resources.members
import dealcart.composeapp.generated.resources.partial_refund
import dealcart.composeapp.generated.resources.pending
import dealcart.composeapp.generated.resources.refunded
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FlashProduct(
    flashDeal: FlashDealUiData,
    //onShareClick: () -> Unit = {}
) {

    var remainingTime by remember { mutableLongStateOf(flashDeal.timeLeft.getValueOrDefault() / 1000) }

    LaunchedEffect(key1 = flashDeal.timeLeft) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime--
        }
    }

    val hours = (remainingTime / 3600).toString().padStart(2, '0')
    val minutes = ((remainingTime % 3600) / 60).toString().padStart(2, '0')
    val seconds = (remainingTime % 60).toString().padStart(2, '0')

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(Res.string.flash_deal),
            style = TextStyle(
                textAlign = TextAlign.Left,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = ButtonSurfaceBrand,
            )
        )

        if (flashDeal.dealStatus != "expired") {

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.members),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubdued,
                        textAlign = TextAlign.Center,
                    )
                )
                Image(
                    painter = painterResource(Res.drawable.ic_persons_members),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = ButtonSurfaceBrand
                            )
                        ) {
                            append(
                                "${flashDeal.totalPersonsJoined}"
                            )
                        }
                        withStyle(
                            style = SpanStyle(
                                color = TextSubdued
                            )
                        ) {
                            append(
                                "/${
                                    flashDeal.totalPersonsRequired
                                }"
                            )
                        }
                    },
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "|",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = SurfaceBackground,
                    )
                )
                Text(
                    text = stringResource(Res.string.baqaya_waqt),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubdued
                    )
                )
                Image(
                    painter = painterResource(Res.drawable.ic_time_remaining),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "$hours : $minutes : $seconds",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = IconDanger
                    )
                )
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .border(
                        BorderStroke(
                            2.dp,
                            BorderPrimaryDefault
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ),
            ) {
                loadImagePainterWithValidation(
                    imageUrl = flashDeal.productImage,
                    fallbackRes = Res.drawable.ic_dealcart_placeholder,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = flashDeal.productName,
                    style = TextStyle(
                        textAlign = TextAlign.Left,
                        color = TextSubdued,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 12.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = flashDeal.formattedDealPrice(),
                        style = TextStyle(
                            textAlign = TextAlign.Left,
                            color = TextSubdued,
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Text(
                        text = flashDeal.formattedOriginalPrice(),
                        style = TextStyle(
                            textDecoration = TextDecoration.LineThrough,
                            color = TextSubdued,
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily()
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (flashDeal.paymentStatus == PaymentStatus.REFUNDED.paymentStatus || flashDeal.paymentStatus == PaymentStatus.PARTIAL_REFUND.paymentStatus || flashDeal.dealStatus == "expired" || flashDeal.dealStatus == "pending") {
                        Text(
                            modifier = Modifier
                                .background(
                                    color = OrderOutBg,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 2.dp),
                            text = stringResource(if (flashDeal.paymentStatus == PaymentStatus.PARTIAL_REFUND.paymentStatus) Res.string.partial_refund else if (flashDeal.paymentStatus == PaymentStatus.REFUNDED.paymentStatus) Res.string.refunded else if (flashDeal.dealStatus == "expired") Res.string.expired else Res.string.pending),
                            color = IconDanger,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                            lineHeight = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Top),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Box {
                    GreenBox(
                        modifier = Modifier
                            .background(
                                Color(0xFF179C48),
                                shape = RoundedCornerShape(topEnd = 8.dp, bottomStart = 8.dp)
                            )
                            .padding(start = 6.dp, end = 6.dp)
                            .wrapContentWidth()
                            .height(21.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = flashDeal.dealPercentageOff() + " OFF",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 10.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                            )
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .weight(.1f)
                )
                Text(
                    modifier = Modifier.padding(end = 5.dp, bottom = 5.dp),
                    text = "Qty: x${flashDeal.quantityBought.toString()}",
                    style = TextStyle(
                        textAlign = TextAlign.Left,
                        color = TextSubdued,
                        fontSize = 14.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PoppinsFontFamily(),
                    )
                )
            }
        }
    }

}