package com.localpk.app.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.promo.domain.Promo
import com.localpk.app.features.promo.domain.PromoItemConstants
import com.localpk.app.theming.ColorTermCondition
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.formatDate
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.coins
import dealcart.composeapp.generated.resources.get
import org.jetbrains.compose.resources.stringResource
import dealcart.composeapp.generated.resources.apply
import dealcart.composeapp.generated.resources.promo_gray
import dealcart.composeapp.generated.resources.promo_green
import dealcart.composeapp.generated.resources.promo_orange
import dealcart.composeapp.generated.resources.promo_yellow
import dealcart.composeapp.generated.resources.terms_and_conditions
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun PromoListItem(
    modifier: Modifier = Modifier,
    promo: Promo,
    onPromoClick: (Promo) -> Unit,
    onTermsClick: (Promo) -> Unit
) {
    val discountText: String
    val discountBackground: DrawableResource

    when {
        promo.discountValue.toInt() == 0 -> {
            discountText = PromoItemConstants.PROMO_CAMPAIGN.key
            discountBackground = Res.drawable.promo_orange
        }

        promo.discountType == PromoItemConstants.FLAT.key -> {
            discountText = "Save \n  Rs. ${promo.discountValue}"
            discountBackground = Res.drawable.promo_yellow
        }

        promo.discountType == PromoItemConstants.PERCENTAGE.key -> {
            discountText = "Save \n ${promo.discountValue} %"
            discountBackground = Res.drawable.promo_green
        }

        promo.discountType == PromoItemConstants.PERCENTAGE_OF_ORDER_COINS.key -> {
            discountText = buildString {
                append(stringResource(Res.string.get).uppercase())
                append("\n${promo.discountValue}%")
                append("\n${stringResource(Res.string.coins)}")
            }
            discountBackground = Res.drawable.promo_green
        }

        else -> {
            discountText = buildString {
                append(stringResource(Res.string.get).uppercase())
                append("\n${promo.discountValue}")
                append("\n${stringResource(Res.string.coins)}")
            }
            discountBackground = Res.drawable.promo_green
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
    ) {
        Box(
            modifier = Modifier
                .width(85.dp)
                .height(115.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(discountBackground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Text(
                text = discountText,
                color = Color.White,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )
        }
        Box(
            modifier = Modifier
                .height(115.dp)
                .weight(1f)
        ) {
            // Background image
            Image(
                painter = painterResource(Res.drawable.promo_gray),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // Foreground content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = promo.promoCode.uppercase(),
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 20.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Text(
                    text = promo.promoDescription,
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    letterSpacing = (-0.01).sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Expiry: ${promo.expiryDate.formatDate()}",
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp,
                        lineHeight = 12.sp,
                        maxLines = 1,
                        color = Color.Black,
                        letterSpacing = (-0.1).sp
                    )

                    Button(
                        onClick = { onPromoClick(promo) },
                        modifier = Modifier
                            .height(26.dp)
                            .wrapContentWidth(),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(
                                0xFF5669FF
                            )
                        )
                    ) {
                        Text(
                            text = stringResource(Res.string.apply).uppercase(),
                            fontSize = 10.sp,
                            color = Color.White,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(Res.string.terms_and_conditions),
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 9.sp,
                    color = ColorTermCondition,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { onTermsClick(promo) },
                    letterSpacing = (-0.2).sp
                )
            }
        }
    }
}