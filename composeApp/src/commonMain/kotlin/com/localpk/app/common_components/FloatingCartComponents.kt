package com.localpk.app.common_components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.ProductInfo
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import com.localpk.app.features.product_detail.data.CartProductType
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.restaurant.data.AddItemRequestBody
import com.localpk.app.features.restaurant.data.CategoryItemUIDataAndRestaurantDetails
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import org.koin.compose.koinInject


@Composable
fun CartContainer(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(14.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize(),
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

@Composable
fun FloatingCartContainer(isNavigationPaddingRequired: Boolean, content: @Composable ColumnScope.() -> Unit) {
    CartContainer(
        modifier = if (isNavigationPaddingRequired) Modifier.navigationBarsPadding() else Modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        content()
    }
}

@Composable
fun FloatingCartSection(
    cartTotal: Int,
    cartMinOrderValue: Float,
    cartDiscount: Double,
    dcCityCartTotal: Double,
    isProductDetail: Boolean,
    isFoodDetail: Boolean = false,
    itemCount: Int = 0,
    onCartClick: () -> Unit,
    productDetailArgs: ProductUiData? = null,
    foodDetailArgs: CategoryItemUIDataAndRestaurantDetails? = null,
    foodCartViewModel: FoodCartViewModel? = null,
    showOnlyProductDetailCounter: Boolean = false,
) {
    val eventLogger: EventLogger = koinInject()
    val totalAmount = cartTotal.toDouble()
    val isLessMOV = dcCityCartTotal < cartMinOrderValue

    if (!showOnlyProductDetailCounter) {
        FloatingCartCompose(
            totalAmount = totalAmount,
            isLessMOV = isLessMOV,
            dcCityCartTotal = dcCityCartTotal.toInt(),
            dcCartMinOrderVal = cartMinOrderValue,
            overAllDiscount = cartDiscount,
            isProductDetail = isProductDetail || isFoodDetail,
            isFood = isFoodDetail,
            itemCount = itemCount,
            onClick = onCartClick,
        )
    }


    val cartViewModel: CartViewModel = koinInject()

    if (isProductDetail && productDetailArgs != null) {

        val localCartItems = cartViewModel.localCartItems.collectAsState().value
        val cartItem = localCartItems[productDetailArgs.id]
        val quantity = cartItem?.quantity ?: 0
        val cartProductId = cartItem?.cartProductId

        // Local loading state for this specific product detail counter
        var isProductDetailLoading by remember { mutableStateOf(false) }

        if (productDetailArgs.isFlashDeal) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .background(Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            color = ButtonSurfaceBrand
                        ).then(
                            if (!cartViewModel.isUpdatingQuantity.value) Modifier.clickable {
                                if (quantity <= 0) {
                                    cartViewModel.addToCart(
                                        cartProductType = CartProductType.DEAL,
                                        productData = productDetailArgs
                                    ) {
                                        eventLogger.addToCartMergn(
                                            productDetailArgs.id.getValueOrDefault(),
                                            productDetailArgs.name,
                                            quantity,
                                            productDetailArgs.finalPrice.getValueOrDefault(),
                                            "product_detail",
                                            type = "pdp"
                                        )
                                    }
                                } else {
                                    GlobalToastManager.showToast(
                                        "Aap ye deal cart mein add kar chuke hain",
                                        SnackbarType.ERROR
                                    )
                                }
                            } else Modifier
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
        } else {
            ProductDetailCounter(
                quantity = quantity,
                onAdd = {
                    isProductDetailLoading = true
                    cartViewModel.addToCart(
                        productData = productDetailArgs,
                        updateCounterLoadingState = {
                            isProductDetailLoading = false
                        }
                    ) {
                        eventLogger.addToCartMergn(
                            productDetailArgs.id.getValueOrDefault(),
                            productDetailArgs.name,
                            quantity,
                            productDetailArgs.finalPrice.getValueOrDefault(),
                            "product_detail",
                            type = "pdp"
                        )
                    }
                },
                onIncrement = {
                    isProductDetailLoading = true
                    if (cartProductId != null) cartViewModel.incrementItemQuantity(
                        productDetailArgs.id,
                        cartProductId,
                        updateCounterLoadingState = {
                            isProductDetailLoading = false
                        }
                    )
                },
                onDecrement = {
                    isProductDetailLoading = true
                    if (cartProductId != null) cartViewModel.decreaseItemQuantity(
                        productDetailArgs.id,
                        cartProductId,
                        updateCounterLoadingState = {
                            isProductDetailLoading = false
                        }
                    )
                },
                onRemove = {
                    isProductDetailLoading = true
                    if (cartProductId != null) cartViewModel.removeCartItem(
                        productData = ProductInfo(
                            productId = productDetailArgs.id,
                            cartProductId = cartProductId,
                            quantity = quantity,
                            price = productDetailArgs.finalPrice.getValueOrDefault(),
                            productName = productDetailArgs.name
                        ),
                        updateCounterLoadingState = {
                            isProductDetailLoading = false
                        }
                    )
                },
                isAddToCartDisabled = isProductDetailLoading,
                isUpdatingQuantity = isProductDetailLoading
            )
        }
    } else if (isFoodDetail && foodCartViewModel != null) {
        val selectedVariationId =
            GlobalFoodDetailStateManager.selectedVariationId.collectAsState().value
        val selectedChoicesIds =
            GlobalFoodDetailStateManager.selectedChoicesIds.collectAsState().value
        val specialInstructions =
            GlobalFoodDetailStateManager.specialInstructions.collectAsState().value
        val isAddToCartDisabled =
            GlobalFoodDetailStateManager.isAddToCartDisabled.collectAsState().value

        val foodItemId = foodDetailArgs?.id
        val localFoodCartItems = foodCartViewModel.localFoodCartItems.collectAsState().value
        val cartItem = localFoodCartItems[foodItemId]
        val cartProductId = cartItem?.cartProductId

        // Local loading state for this specific food detail counter
        var isFoodDetailLoading by remember { mutableStateOf(false) }
        ProductDetailCounter(
            quantity = cartItem?.quantity ?: 0,
            onAdd = {
                isFoodDetailLoading = true
                val addRequest = AddItemRequestBody(
                    itemId = foodItemId,
                    quantity = 1,
                    instructions = specialInstructions,
                    variationId = if (selectedVariationId != 0L) selectedVariationId else null,
                    choiceIds = selectedChoicesIds.values.flatten()
                )
                eventLogger.addFoodToCart(
                    id = foodItemId.getValueOrDefault(),
                    name = foodDetailArgs?.name.default(),
                    quantity = foodDetailArgs?.itemQuantity.getValueOrDefault(),
                    discountPrice = foodDetailArgs?.prices?.discountPrice.getValueOrDefault(),
                    source = "PDP",
                    categoryName = EMPTY_STRING,
                    restaurantName = foodDetailArgs?.restaurantName.default(),
                    variations = if (selectedVariationId != 0L) selectedVariationId else null
                )
                cartViewModel.addToCartFoodItem(addRequest, updateCounterLoadingState = {
                    isFoodDetailLoading = false
                })
            },
            onIncrement = {
                isFoodDetailLoading = true
                if (cartProductId != null) foodCartViewModel.incrementFoodCartItemQuantity(
                    cartProductId,
                    updateCounterLoadingState = {
                        isFoodDetailLoading = false
                    }
                )

            },
            onDecrement = {
                isFoodDetailLoading = true
                if (cartProductId != null) foodCartViewModel.decrementFoodCartItemQuantity(
                    cartProductId,
                    updateCounterLoadingState = {
                        isFoodDetailLoading = false
                    }
                )
            },
            onRemove = {
                isFoodDetailLoading = true
                if (cartProductId != null) foodCartViewModel.decrementFoodCartItemQuantity(
                    cartProductId,
                    updateCounterLoadingState = {
                        isFoodDetailLoading = false
                    }
                )
            },
            isAddToCartDisabled = isAddToCartDisabled,
            isUpdatingQuantity = isFoodDetailLoading
        )
    }
} 