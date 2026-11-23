package com.localpk.app.features.cart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.localpk.app.features.cart.components.EmptyPromoState
import com.localpk.app.features.cart.components.PromoListItem
import com.localpk.app.features.cart.components.PromoListItemShimmer
import com.localpk.app.features.promo.domain.toPromoTermsBottomSheetScreen
import com.localpk.app.features.promo.view.PromoListingViewModel
import com.localpk.app.theming.BundleBg
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.GreyLight
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.ZERO_LONG
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.apply
import dealcart.composeapp.generated.resources.enter_promo_code
import dealcart.composeapp.generated.resources.promo_codes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PromoListingContent(
    isFood: Boolean,
    onPromoSelected: (promoCode: String, promoId: Long) -> Unit
) {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current

    val viewModel = koinViewModel<PromoListingViewModel>()

    val promoCode by viewModel.promoCode.collectAsState()
    val foodPromos by viewModel.foodPromosListing.collectAsState()
    val groceryPromos by viewModel.groceryPromosListing.collectAsState()

    val promos by remember(foodPromos, groceryPromos) {
        derivedStateOf {
            if (isFood) foodPromos else groceryPromos
        }
    }
    val isPromoLoaded by viewModel.isPromosLoaded.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .background(Color.White, shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(Res.string.promo_codes),
            style = TextStyle(
                fontFamily = PoppinsFontFamily(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(BundleBg)
                .padding(vertical = 5.dp)
        ) {
            BasicTextField(
                value = promoCode,
                onValueChange = { viewModel.promoCode.value = it },
                singleLine = true,
                textStyle = TextStyle(
                    color = ColorPrimaryText,
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 10.sp
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (promoCode.trim().isEmpty()) {
                            Text(
                                stringResource(Res.string.enter_promo_code),
                                color = ColorPrimaryText,
                                fontFamily = PoppinsFontFamily(),
                                fontSize = 10.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Button(
                enabled = promoCode.isNotEmpty(),
                modifier = Modifier.wrapContentSize(),
                onClick = {
                    onPromoSelected(promoCode.trim(), ZERO_LONG)
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (promoCode.isNotEmpty()) Color(
                        0xFF5669FF
                    ) else GreyLight
                ),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Text(
                    stringResource(Res.string.apply),
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        if (isPromoLoaded) {
            if (promos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Available Promo Codes",
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    items(promos) { promo ->
                        PromoListItem(
                            promo = promo,
                            onPromoClick = {
                                onPromoSelected(promo.promoCode, promo.id)
                            },
                            onTermsClick = {
                                bottomSheetNavigator.replace(
                                    promo.toPromoTermsBottomSheetScreen()
                                )
                            }
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(32.dp))
                EmptyPromoState()
            }
        } else {
            // Show shimmer while loading
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Available Promo Codes",
                style = TextStyle(
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                items(3) {
                    PromoListItemShimmer()
                }
            }
        }
    }
}
