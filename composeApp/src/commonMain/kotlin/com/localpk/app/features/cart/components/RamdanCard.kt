package com.localpk.app.features.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.cart.data.CartUiData
import com.localpk.app.features.cart.data.CartUiModel
import com.localpk.app.theming.Background
import com.localpk.app.theming.BlackText
import com.localpk.app.theming.BlueBg
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimary5
import com.localpk.app.theming.ColorPrimary60
import com.localpk.app.theming.DcTextGray
import com.localpk.app.theming.DiscountColor
import com.localpk.app.theming.Pinkish
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.PurpleLight
import com.localpk.app.theming.TextGray70
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.ramzan_rashan_bundle
import org.jetbrains.compose.resources.stringResource

@Composable
fun RamadanCard(
    cartViewData: CartUiData?,
    cartProductSingleItems: List<CartUiModel>?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, DcTextGray),
                RoundedCornerShape(16.dp)
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = ColorPrimary5,
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(top = 10.dp, bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(Res.string.ramzan_rashan_bundle),
                        style = TextStyle(
                            color = BlackText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    if (cartViewData?.dcCityCart?.bundle?.discountPercentage.getValueOrDefault() > ZERO) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = DiscountColor,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${cartViewData?.dcCityCart?.bundle?.discountPercentage.getValueOrDefault()} OFF",
                                style = TextStyle(
                                    color = ColorPrimary60,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 12.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            text = cartViewData?.dcCityCart?.bundle?.formattedDiscountPrice()
                                .default(),
                            style = TextStyle(
                                color = Pinkish,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        if (cartViewData?.dcCityCart?.bundle?.formattedDiscountPrice()
                                .default() != cartViewData?.dcCityCart?.bundle?.formattedFinalPrice()
                                .default()
                        ) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = cartViewData?.dcCityCart?.bundle?.formattedFinalPrice()
                                    .default(),
                                style = TextStyle(
                                    color = TextGray70,
                                    fontSize = 13.sp,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )
                        }
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.LightGray
            )
            cartProductSingleItems?.forEachIndexed { index, it ->
                SingleProductItem(it)
                if (index + 1 < cartProductSingleItems.size) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = Background
                    )
                }
            }
        }
    }
}


@Composable
fun SingleProductItem(singleProduct: CartUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        loadImagePainterWithValidation(
            imageUrl = singleProduct.productImage,
            fallbackRes = Res.drawable.ic_dealcart_placeholder,
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.White
                )
                .border(
                    BorderStroke(2.dp, BlueBg),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(4.dp)
                .size(72.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .wrapContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ID ${singleProduct.productId}",
                    style =
                    TextStyle(
                        textAlign = TextAlign.Left,
                        color = BlackText,
                        fontSize = 13.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal
                    )
                )
                if (singleProduct.isFlashDeal || singleProduct.showMrp?.not().default()) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = PurpleLight,
                                shape = RoundedCornerShape(18.dp)
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                                text = singleProduct.percentageOff(),
                                style = TextStyle(
                                    color = ColorPrimary,
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = singleProduct.productName,
                modifier = Modifier.padding(end = 2.dp),
                style =
                TextStyle(
                    textAlign = TextAlign.Left,
                    color = BlackText,
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        text = singleProduct.formattedDiscountedPrice(),
                        style = TextStyle(
                            textAlign = TextAlign.Left,
                            color = Pinkish,
                            fontSize = 13.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    if (singleProduct.isFlashDeal || singleProduct.showMrp?.not()
                            .default()
                    ) {
                        Text(
                            text = singleProduct.formattedActualPrice(),
                            style = TextStyle(
                                textAlign = TextAlign.Left,
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Black,
                                fontSize = 13.sp,
                                fontFamily = PoppinsFontFamily(),
                            )
                        )
                    }
                }
            }
        }
    }
}