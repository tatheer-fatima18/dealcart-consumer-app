package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.GreenBox
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.restaurant.data.FoodOrderItemDetails
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.capitalizeFirst
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.fallback
import dealcart.composeapp.generated.resources.qty
import org.jetbrains.compose.resources.stringResource

@Composable
fun SingleFoodProduct(
    singleProduct: FoodOrderItemDetails
) {
    val choicesText = singleProduct.orderItemChoices
        ?.mapNotNull { it.choice?.name }
        ?.joinToString(", ")
        .default()
        .capitalizeFirst()

    Column(
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
                            1.dp,
                            BorderPrimaryDefault
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ),
            ) {
                loadImagePainterWithValidation(
                    imageUrl = singleProduct.item?.image,
                    fallbackRes = Res.drawable.fallback,
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
                    text = singleProduct.item?.name.default(),
                    style =
                    TextStyle(
                        textAlign = TextAlign.Left,
                        color = TextSubdued,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 12.sp,
                        lineHeight = 14.4.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (choicesText.isNotEmpty()) {
                    Text(
                        text = choicesText,
                        style = TextStyle(
                            textAlign = TextAlign.Left,
                            color = TextSubdued,
                            fontFamily = PoppinsFontFamily(),
                            fontSize = 12.sp,
                            lineHeight = 14.4.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = singleProduct.formattedDiscountAmount().default(),
                        style = TextStyle(
                            textAlign = TextAlign.Left,
                            color = TextSubdued,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    if (singleProduct.item?.listPrice.getValueOrDefault() > singleProduct.item?.discountPrice.getValueOrDefault()) {
                        Text(
                            text = singleProduct.formattedAmount().default(),
                            style = TextStyle(
                                textAlign = TextAlign.Left,
                                textDecoration = TextDecoration.LineThrough,
                                color = TextSubdued,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = PoppinsFontFamily()
                            )
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
                if (singleProduct.item?.listPrice.getValueOrDefault() > singleProduct.item?.discountPrice.getValueOrDefault()) {
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
                                text = singleProduct.item?.percentageOff().default(),
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(.1f))
                Text(
                    modifier = Modifier.padding(end = 5.dp, bottom = 5.dp),
                    text = stringResource(
                        Res.string.qty,
                        singleProduct.quantity.getValueOrDefault().toString()
                    ),
                    style = TextStyle(
                        textAlign = TextAlign.Left,
                        color = TextSubdued,
                        fontSize = 14.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PoppinsFontFamily()
                    )
                )
            }
        }
    }
}