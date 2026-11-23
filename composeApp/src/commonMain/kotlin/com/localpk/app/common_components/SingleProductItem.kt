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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.cart.data.ProductInfo
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.productlist.data.toProductDetailScreen
import com.localpk.app.theming.AppTypography
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.subTotal
import com.localpk.app.theming.transparent
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import org.koin.compose.koinInject

@Composable
fun SingleProductItem(
    swimlaneType: SwimlaneType? = null,
    productList: Boolean,
    data: ProductUiData,
    cardClick: () -> Unit,
    onAddClick: () -> Unit,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
    onDelete: () -> Unit = {},
    displayName: String? = null,
    displayImage: String? = null,
    isCart: Boolean? = false
) {
    val navigator = LocalNavigator.currentOrThrow
    val cartViewModel: CartViewModel = koinInject()
    val width = if (productList) 169.dp else 155.dp

    // Collect localCartItems as state to trigger recomposition
    val localCartItems by cartViewModel.localCartItems.collectAsState()
    val itemQuantity = localCartItems[data.id]?.quantity ?: 0
    val cartProductId = localCartItems[data.id]?.cartProductId
    
    // Local loading state for this specific item
    var isItemLoading by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .width(width)
            .clickable {
                cardClick()
                navigator.push(
                    data.toProductDetailScreen()
                )
            }
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(
                BorderStroke(1.dp, BorderPrimaryDefault),
                shape = RoundedCornerShape(8.dp)
            )
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(width)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        BorderStroke(1.dp, transparent),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                loadImagePainterWithValidation(
                    imageUrl = if (displayImage.isNullOrEmpty()
                            .not()
                    ) displayImage else data.imageUrl,
                    fallbackRes = Res.drawable.ic_dealcart_placeholder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .aspectRatio(1f)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, transparent),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.Fit
                )
                if (data.showMrp?.not().default()) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .align(Alignment.TopEnd)
                    ) {
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
                                text = if (swimlaneType?.type == SwimlaneType.MAX_ABS_DISCOUNT.type) data.formattedValue() else data.percentageOff(),
                                style = AppTypography().bodyMedium.copy(
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(bottom = 2.5.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .height(44.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {

                (if (displayName.isNotNull()) displayName else data.name)?.let {
                    Text(
                        text = it,
                        style = AppTypography().bodyMedium.copy(
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 13.2.sp
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp, end = 6.dp),
                        maxLines = 2,
                    )
                }
            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(
                    3.dp,
                    Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.End
            ) {

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(top = 5.dp),
                    color = BorderPrimaryDefault
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .padding(start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (itemQuantity <= 0 && !isItemLoading) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = data.formattedDiscountedPrice(),
                                style = AppTypography().bodyMedium.copy(
                                    textAlign = TextAlign.Left,
                                    color = TextSubdued,
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            if (data.showMrp?.not().default()) {
                                Text(
                                    text = data.formattedPrice(),
                                    modifier = Modifier,
                                    style = AppTypography().bodyMedium.copy(
                                        textDecoration = TextDecoration.LineThrough,
                                        color = subTotal,
                                        fontSize = 11.sp,
                                        lineHeight = 11.sp,
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(.1f))
                    }

                    NewCounter(
                        itemQuantity = itemQuantity,
                        onAddClick = {
                            isItemLoading = true
                            onAddClick()
                            if (data.hasVariants.not()) {
                                if (isCart == true)
                                    cartViewModel.addToCart(
                                        data,
                                        isCart = isCart,
                                        successCallback = { 
                                            cartViewModel.fetchUserCart()
                                            isItemLoading = false
                                        },
                                        updateCounterLoadingState = {
                                            isItemLoading = false
                                        })
                                else
                                    cartViewModel.addToCart(data, updateCounterLoadingState = {
                                        isItemLoading = false
                                    })
                            } else
                                navigator.push(
                                    data.toProductDetailScreen()
                                )
                        },
                        onDelete = {
                            isItemLoading = true
                            if (cartProductId != null && cartProductId != 0L) {
                                val productData = ProductInfo(
                                    productId = data.id,
                                    cartProductId = cartProductId,
                                    quantity = itemQuantity,
                                    price = data.discountedPrice,
                                    productName = data.name
                                )
                                if (isCart == true)
                                    cartViewModel.removeCartItem(
                                        isCart = isCart,
                                        successCallback = { 
                                            cartViewModel.fetchUserCart()
                                            isItemLoading = false
                                        },
                                        productData = productData,
                                        updateCounterLoadingState = {
                                            isItemLoading = false
                                        })

                                else
                                    cartViewModel.removeCartItem(
                                        productData = productData,
                                        updateCounterLoadingState = {
                                        isItemLoading = false
                                    })
                            }
                            onDelete()
                        },
                        onDecrement = {
                            isItemLoading = true
                            if (cartProductId != null && cartProductId != 0L) {
                                if (isCart == true)
                                    cartViewModel.decreaseItemQuantity(
                                        data.id,
                                        cartProductId,
                                        isCart = isCart,
                                        successCallback = { 
                                            cartViewModel.fetchUserCart()
                                            isItemLoading = false
                                        },
                                        updateCounterLoadingState = {
                                            isItemLoading = false
                                        })
                                else
                                    cartViewModel.decreaseItemQuantity(data.id, cartProductId, updateCounterLoadingState = {
                                        isItemLoading = false
                                    })
                            }
                            onDecrement()
                        },
                        onIncrement = {
                            isItemLoading = true
                            if (cartProductId != null && cartProductId != 0L) {
                                if (isCart == true)
                                    cartViewModel.incrementItemQuantity(
                                        data.id,
                                        cartProductId,
                                        isCart = isCart,
                                        successCallback = { 
                                            cartViewModel.fetchUserCart()
                                            isItemLoading = false
                                        },
                                        updateCounterLoadingState = {
                                            isItemLoading = false
                                        })
                                else
                                    cartViewModel.incrementItemQuantity(
                                        data.id,
                                        cartProductId,
                                        updateCounterLoadingState = {
                                            isItemLoading = false
                                        }
                                    )

                            }
                            onIncrement()
                        },
                        isUpdatingQuantity = isItemLoading
                    )
                }
            }
        }
    }
}