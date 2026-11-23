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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.BundleDetails
import com.localpk.app.features.cart.data.CartUiModel
import com.localpk.app.theming.Background
import com.localpk.app.theming.BlackText
import com.localpk.app.theming.ColorPrimary5
import com.localpk.app.theming.ColorPrimary60
import com.localpk.app.theming.DcTextGray
import com.localpk.app.theming.DiscountColor
import com.localpk.app.theming.Pinkish
import com.localpk.app.theming.TextGray70
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ramzan_rashan_bundle
import org.jetbrains.compose.resources.stringResource

@Composable
fun BundleCard(
    bundle: BundleDetails?,
    //cartViewModel: CartViewModel,
    cartProductSingleItems: List<CartUiModel>?,
    quantity: Int,
    showBundleDialog: Boolean,
) {
//    if (showBundleDialog) {
//        OutOfStockDialog(outOfStockData = cartViewModel.bundleDetails.value) {
//            cartViewModel.showBundleDialog.value = false
//        }
//    }
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
                .padding(bottom = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = ColorPrimary5,
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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
                    if (bundle?.discountPercentage.getValueOrDefault() > ZERO) {
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
                                text = "${bundle?.discountPercentage.getValueOrDefault()} OFF",
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
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            text = bundle?.formattedDiscountPrice()
                                .default(),
                            style = TextStyle(
                                color = Pinkish,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        if (bundle?.formattedDiscountPrice()
                                .default() != bundle?.formattedFinalPrice()
                                .default()
                        ) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = bundle?.formattedFinalPrice().default(),
                                style = TextStyle(
                                    color = TextGray70,
                                    fontSize = 13.sp,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )
                        }
                    }
//                    QuantityCounter(
//                        quantity = quantity,
//                        decreaseCounter = { cartViewModel.decreaseBundleQuantity() },
//                        increaseCounter = { cartViewModel.increaseBundleQuantity() }
//                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.LightGray
            )
            cartProductSingleItems?.forEachIndexed { index, it ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                ) {
                    // SingleCartProductItem(cartViewModel = cartViewModel, singleProduct = it)
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
}