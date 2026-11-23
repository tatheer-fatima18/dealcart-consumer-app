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
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.localpk.app.features.cart.view.PromoListingContent
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import org.koin.compose.koinInject

class PromoListingBottomSheetScreen(private val isFood: Boolean = false) : Screen {

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val cartViewModel = if (!isFood) koinInject<CartViewModel>() else null
        val foodCartViewModel = if (isFood) koinInject<FoodCartViewModel>() else null

        fun onPromoSelected(promoCode: String, promoId: Long) {
            if (isFood) {
                foodCartViewModel?.handlePromoSelection(promoCode, promoId) { bottomSheetNavigator.hide() }
            } else {
                cartViewModel?.handlePromoSelection(promoCode, promoId) { bottomSheetNavigator.hide() }
            }
        }

        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
        ) {
            PromoListingContent(
                isFood = isFood,
                onPromoSelected = ::onPromoSelected
            )
        }
    }
}