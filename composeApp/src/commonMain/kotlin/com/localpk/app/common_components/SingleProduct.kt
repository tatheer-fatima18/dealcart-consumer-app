package com.localpk.app.common_components

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.orders.data.OrderItemDetailUiData
import com.localpk.app.features.orders.data.OrderItemStatus
import com.localpk.app.features.orders.data.PaymentStatus
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.OrderOutBg
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.StatusBgColor
import com.localpk.app.utils.capitalizeFirstLetter
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.cancelled
import dealcart.composeapp.generated.resources.fallback
import dealcart.composeapp.generated.resources.out_of_stock
import dealcart.composeapp.generated.resources.out_of_stock_bg
import dealcart.composeapp.generated.resources.partial_refund
import dealcart.composeapp.generated.resources.qty
import dealcart.composeapp.generated.resources.refunded
import dealcart.composeapp.generated.resources.returned
import dealcart.composeapp.generated.resources.rupees_text_formatted
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SingleProduct(
    singleProduct: OrderItemDetailUiData
) {
    val statusText = when {
        singleProduct.paymentStatus == PaymentStatus.PARTIAL_REFUND.paymentStatus ->
            stringResource(Res.string.partial_refund)

        singleProduct.paymentStatus == PaymentStatus.REFUNDED.paymentStatus -> stringResource(Res.string.refunded)
        singleProduct.itemStatus == OrderItemStatus.CANCELLED.itemStatus -> stringResource(Res.string.cancelled)
        singleProduct.itemStatus == OrderItemStatus.RETURNED.itemStatus -> stringResource(Res.string.returned)
        singleProduct.itemStatus == OrderItemStatus.OUT_OF_STOCK.itemStatus || singleProduct.outOfStock -> stringResource(
            Res.string.out_of_stock
        )

        else -> singleProduct.itemStatus
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
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
                    imageUrl = singleProduct.productImage,
                    fallbackRes = Res.drawable.fallback,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                if (singleProduct.itemStatus == OrderItemStatus.OUT_OF_STOCK.itemStatus || singleProduct.outOfStock) {
                    Image(
                        painter = painterResource(Res.drawable.out_of_stock_bg),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = singleProduct.productName,
                    style =
                        TextStyle(
                            textAlign = TextAlign.Left,
                            color =  TextSubdued,
                            fontFamily = PoppinsFontFamily(),
                            fontSize = 12.sp,
                            lineHeight = 14.4.sp,
                            fontWeight = FontWeight.Normal,
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
                        text = singleProduct.totalAmount.formatPKRCurrency(),
                        style = TextStyle(
                            textAlign = TextAlign.Left,
                            color = TextSubdued,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,

                        )
                    )

                    if (singleProduct.isDeal || singleProduct.showMrp.not().default()) {
                        Text(
                            text = singleProduct.totalUnitsPrice.formatPKRCurrency(),
                            style = TextStyle(
                                textAlign = TextAlign.Left,
                                textDecoration = TextDecoration.LineThrough,
                                color = TextSubdued,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = PoppinsFontFamily(),
                            )
                        )
                    }

                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = when (singleProduct.itemStatus) {
                                    OrderItemStatus.CANCELLED.itemStatus, OrderItemStatus.RETURNED.itemStatus, OrderItemStatus.OUT_OF_STOCK.itemStatus -> OrderOutBg
                                    else -> StatusBgColor
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 2.dp),
                        text = statusText.capitalizeFirstLetter(),
                        color =
                            when (singleProduct.itemStatus) {
                                OrderItemStatus.CANCELLED.itemStatus, OrderItemStatus.RETURNED.itemStatus, OrderItemStatus.OUT_OF_STOCK.itemStatus -> IconDanger
                                else -> ButtonSurfaceBrand
                            },
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        lineHeight = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                    )
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Top),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.End,
            ) {
                if (singleProduct.isDeal || singleProduct.showMrp.not().default()) {
                    Box {
                        GreenBox(
                            modifier = Modifier
                                .background(
                                    Color(0xFF179C48),
                                    shape = RoundedCornerShape(
                                        topEnd = 8.dp,
                                        bottomStart = 8.dp
                                    )
                                )
                                .padding(start = 6.dp, end = 6.dp)
                                .wrapContentWidth()
                                .height(21.dp)
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = singleProduct.percentageOff() + " OFF",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold,

                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(.1f))
                Text(
                    modifier = Modifier.padding(end = 5.dp, bottom = 5.dp),
                    text = "Qty: x${singleProduct.pickedQuantity}",
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