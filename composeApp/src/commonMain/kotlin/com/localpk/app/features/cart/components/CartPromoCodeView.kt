package com.localpk.app.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.CartPromo
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.TextSuccess
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_arrow_forward_24
import dealcart.composeapp.generated.resources.ic_promo_applied
import dealcart.composeapp.generated.resources.ic_promo_details_cart
import dealcart.composeapp.generated.resources.remove
import dealcart.composeapp.generated.resources.use_promo_code
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun CartPromoCodeView(
    promoDetails: CartPromo?,
    isPromoCodeValidated: Boolean,
    onAddPromoCode: () -> Unit,
    onRemovePromoCode: () -> Unit
) {
    if (isPromoCodeValidated.not()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onAddPromoCode()
                }
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(Res.drawable.ic_promo_details_cart),
                contentDescription = null
            )
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.use_promo_code),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    color = TextSubdued
                ),
            )
            Image(
                modifier = Modifier
                    .padding(3.dp)
                    .size(12.dp),
                painter = painterResource(Res.drawable.ic_arrow_forward_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = TextSubduedHalf)
            )
        }
    } else {
        Row(
            modifier = Modifier
                .background(
                    color = SurfaceBackground,
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Res.drawable.ic_promo_applied),
                contentDescription = null,
            )
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = promoDetails?.promoCode.default(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextSuccess
                    )
                )
                Text(
                    text = promoDetails?.formattedExpiryDate().default(),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubdued,
                    )
                )
            }
            Text(
                text = stringResource(Res.string.remove),
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = TextSubdued,
                ),
                modifier = Modifier
                    .clickable {
                        onRemovePromoCode()
                    }
                    .padding(2.dp)
            )
        }
    }
}