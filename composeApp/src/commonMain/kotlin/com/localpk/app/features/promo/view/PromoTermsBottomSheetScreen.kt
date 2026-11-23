package com.localpk.app.features.promo.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.features.cart.view.PromoTermsContent
import com.localpk.app.features.promo.domain.Promo
import com.localpk.app.utils.EMPTY_STRING

class PromoTermsBottomSheetScreen(
    private val promoId: Long = 0L,
    private val promoName: String = EMPTY_STRING,
    private val promoCode: String = EMPTY_STRING,
    private val startDate: String = EMPTY_STRING,
    private val expiryDate: String = EMPTY_STRING,
    private val minOrderValue: Long = 0L,
    private val discountType: String = EMPTY_STRING,
    private val discountValue: Long = 0L,
    private val isActive: Boolean = false,
    private val promoDescription: String = EMPTY_STRING,
    private val termsAndConditions: String = EMPTY_STRING
) : Screen {

    @Composable
    override fun Content() {
        val argsData = Promo(
            id = promoId,
            promoName = promoName,
            promoCode = promoCode,
            startDate = startDate,
            expiryDate = expiryDate,
            minOrderValue = minOrderValue,
            discountType = discountType,
            discountValue = discountValue,
            isActive = isActive,
            promoDescription = promoDescription,
            termsAndConditions = termsAndConditions
        )
        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
        ) {
            PromoTermsContent(argsData = argsData)
        }
    }
}