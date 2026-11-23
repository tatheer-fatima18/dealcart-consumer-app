package com.localpk.app.common_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackgroundSecondary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.disabled
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_delivery_cart_multicity
import dealcart.composeapp.generated.resources.minus
import dealcart.composeapp.generated.resources.plus
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun ProductDetailCounter(
    quantity: Int,
    onAdd: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
    isAddToCartDisabled: Boolean,
    isUpdatingQuantity: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .background(Color.White, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (quantity > 0 || isAddToCartDisabled) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, BorderPrimaryDefault),
                        RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = SurfaceBackgroundSecondary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    onClick = if (isUpdatingQuantity) { {} } else { if (quantity > 1) onDecrement else onRemove },
                    enabled = !isUpdatingQuantity
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.minus),
                        contentDescription = "Decrement",
                        modifier = Modifier.size(14.dp),
                        tint = if (isUpdatingQuantity) Color.Gray else Color.Unspecified
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (isUpdatingQuantity) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = TextSubdued,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "$quantity",
                            color = TextSubdued,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PoppinsFontFamily(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                IconButton(
                    onClick = if (isUpdatingQuantity) { {} } else onIncrement,
                    enabled = !isUpdatingQuantity
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.plus),
                        contentDescription = "Increment",
                        modifier = Modifier.size(14.dp),
                        tint = if (isUpdatingQuantity) Color.Gray else Color.Unspecified
                    )
                }
            }
        } else {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = if (isAddToCartDisabled.not()) ButtonSurfaceBrand else disabled
                    )
                    .then(
                        if (isAddToCartDisabled.not()) Modifier.clickable { onAdd() } else Modifier
                    )
                    .padding(vertical = 15.dp, horizontal = 24.dp),
                text = "Add To Cart",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

