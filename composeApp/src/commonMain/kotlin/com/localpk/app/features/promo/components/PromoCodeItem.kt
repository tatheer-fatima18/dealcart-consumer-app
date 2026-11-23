package com.localpk.app.features.promo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.promo.domain.Promo
import com.localpk.app.features.promo.domain.PromoItemConstants
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.capitalizeFirstLetter
import com.localpk.app.utils.copyTextToClipboardAndShowToast
import com.localpk.app.utils.formatDate
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.coins
import dealcart.composeapp.generated.resources.ic_copy
import dealcart.composeapp.generated.resources.rupees
import dealcart.composeapp.generated.resources.terms_and_symbol_conditions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PromoCodeItem(promo: Promo, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .height(120.27869.dp)
            .paint(
                painter = painterResource(promo.toMapPromoUiData().bgPromo),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 20.74.dp, top = 17.63.dp)
                        .border(
                            width = 1.03689.dp,
                            color = Color(0x1AFFFFFF),
                            shape = RoundedCornerShape(size = 10.36885.dp)
                        )
                        .width(198.04509.dp)
                        .height(47.69672.dp)
                        .background(
                            color = Color(0x1AFFFFFF),
                            shape = RoundedCornerShape(size = 10.36885.dp)
                        )
                        .padding(horizontal = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1.0f),
                        verticalArrangement = Arrangement.spacedBy(
                            (-5.18).dp,
                            Alignment.Top
                        ),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = promo.promoCode,
                            style = TextStyle(
                                fontSize = 16.49.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = promo.promoDescription,
                            style = TextStyle(
                                fontSize = 6.88.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                letterSpacing = 0.01.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        modifier = Modifier.clickable {
                            copyTextToClipboardAndShowToast(
                                textToCopy = promo.promoCode,
                                message = "Promo code copied!",
                            )
                        },
                        painter = painterResource(Res.drawable.ic_copy),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Row(
                    modifier = Modifier.padding(start = 20.74.dp, bottom = 14.dp)
                ) {
                    Text(
                        text = "Expires at ${promo.expiryDate.formatDate()}",
                        style = TextStyle(
                            fontSize = 8.3.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            letterSpacing = 0.01.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(20.74.dp))
                    Text(
                        modifier = Modifier.clickable {
                            onClick()
                        },
                        text = stringResource(Res.string.terms_and_symbol_conditions),
                        style = TextStyle(
                            fontSize = 8.3.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = Color.White,
                            letterSpacing = 0.01.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 12.44.dp, end = 20.74.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        (-16.59).dp,
                        Alignment.Top
                    ),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = stringResource(promo.toMapPromoUiData().discountLabel).uppercase(),
                        style = TextStyle(
                            fontSize = 23.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight(275),
                            color = Color.White,
                            textAlign = TextAlign.Start
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = promo.toMapPromoUiData().discountTxt,
                        style = TextStyle(
                            fontSize = 23.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Start
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    if (
                        promo.discountType == PromoItemConstants.PERCENTAGE_OF_ORDER_COINS.key ||
                        promo.discountType == PromoItemConstants.FLAT_COINS.key || promo.discountType == PromoItemConstants.FLAT.key
                    ) {
                        val textResourceId = when (promo.discountType) {
                            PromoItemConstants.PERCENTAGE_OF_ORDER_COINS.key, PromoItemConstants.FLAT_COINS.key -> Res.string.coins
                            else -> Res.string.rupees
                        }

                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = stringResource(textResourceId).capitalizeFirstLetter(),
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = PoppinsFontFamily(),
                                color = Color.White,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                }
            }
        }
    }
}

