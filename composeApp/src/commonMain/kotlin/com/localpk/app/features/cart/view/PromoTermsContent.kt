package com.localpk.app.features.cart.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.promo.domain.Promo
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.GreyDivider
import com.localpk.app.theming.LightGray
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.formatDate
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.terms_and_conditions
import org.jetbrains.compose.resources.stringResource

@Composable
fun PromoTermsContent(
    argsData: Promo
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            text = "Promo Details",
            fontFamily = PoppinsFontFamily(),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.2).sp,
            color = ColorPrimary
        )

        Divider(
            color = GreyDivider,
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = argsData.promoCode.uppercase(),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            letterSpacing = (-0.2).sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "• ${argsData.promoDescription}",
            fontFamily = PoppinsFontFamily(),
            fontSize = 11.sp,
            color = LightGray,
            letterSpacing = (-0.2).sp,
            fontWeight = FontWeight.Normal
        )

        Text(
            text = "• Expiry: ${argsData.expiryDate.formatDate()}",
            fontFamily = PoppinsFontFamily(),
            fontSize = 11.sp,
            color = LightGray,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.2).sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(Res.string.terms_and_conditions),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = Color.Black,
            textDecoration = TextDecoration.Underline,
            letterSpacing = (-0.2).sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = argsData.termsAndConditions,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = LightGray,
            letterSpacing = (-0.2).sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }
}