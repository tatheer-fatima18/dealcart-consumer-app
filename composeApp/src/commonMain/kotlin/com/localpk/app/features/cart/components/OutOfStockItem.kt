package com.localpk.app.features.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.cart.data.ProductOOSDetails
import com.localpk.app.theming.ColorPrimary30
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.DcRed
import com.localpk.app.theming.DealcartGreen
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.available_stock_colon
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.requested_colon
import dealcart.composeapp.generated.resources.x_pack
import org.jetbrains.compose.resources.stringResource

@Composable
fun OutOfStockItem(item: ProductOOSDetails) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(15.dp)
            )
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .border(
                        BorderStroke(
                            1.dp,
                            ColorPrimary30
                        ),
                        RoundedCornerShape(11.dp)
                    )
                    .padding(horizontal = 13.5.dp, vertical = 6.dp)
                    .height(57.dp)
                    .width(44.dp),
                contentAlignment = Alignment.Center
            ) {
                loadImagePainterWithValidation(
                    imageUrl = item.product?.images?.firstOrNull()?.path.default(),
                    fallbackRes = Res.drawable.ic_dealcart_placeholder,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = item.product?.name.default(),
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = ColorPrimaryText
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(Res.string.requested_colon))
                        append("          ")
                        withStyle(
                            style = SpanStyle(
                                color = DealcartGreen,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(
                                stringResource(
                                    Res.string.x_pack,
                                    item.requestedQuantity.getValueOrDefault()
                                )
                            )
                        }
                    },
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 12.sp,
                        color = ColorPrimaryText
                    )
                )
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(Res.string.available_stock_colon))
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = DcRed,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(
                                stringResource(
                                    Res.string.x_pack,
                                    item.availableQuantity.getValueOrDefault()
                                )
                            )
                        }
                    },
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 12.sp,
                        color = ColorPrimaryText
                    )
                )
            }
        }
    }
}