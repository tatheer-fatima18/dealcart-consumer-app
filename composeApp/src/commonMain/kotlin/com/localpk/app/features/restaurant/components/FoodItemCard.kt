package com.localpk.app.features.restaurant.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.GreenBox
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import com.localpk.app.features.restaurant.data.AddItemRequestBody
import com.localpk.app.features.restaurant.data.CategoryItemUIData
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.fallback
import org.koin.compose.koinInject

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FoodItemCard(
    item: CategoryItemUIData,
    onAddClick: (CategoryItemUIData) -> Unit,
    onCardClick: (CategoryItemUIData) -> Unit,
    onIncrementClick: (CategoryItemUIData) -> Unit,
    onDecrementClick: (CategoryItemUIData) -> Unit,
) {
    val foodCartViewModel: FoodCartViewModel = koinInject()
    val cartViewModel: CartViewModel = koinInject()
    val localFoodCartItems by foodCartViewModel.localFoodCartItems.collectAsState()
    val localCartItem = localFoodCartItems[item.id]
    val cartItemId = localCartItem?.cartProductId
    val itemQuantity = localCartItem?.quantity ?: 0
    
    // Local loading state for this specific food item
    var isFoodItemLoading by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = BorderPrimaryDefault),
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { onCardClick(item) }
    ) {
        Row(
            modifier = Modifier.padding(9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 3.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    item.name.default(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = TextSubdued,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                if (item.description.isNullOrEmpty().not()) {
                    Text(
                        item.description.default(),
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = TextSubdued,
                            fontWeight = FontWeight.Normal
                        ),
                    )
                }
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (item.variations.isNullOrEmpty()
                                .not()
                        ) "From ${item.formattedDiscountedAmount()}"
                        else item.formattedDiscountedAmount(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = TextSubdued,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    if (item.prices?.listPrice != null && item.prices.listPrice > item.prices.discountPrice.getValueOrDefault()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            item.formattedTotalAmount(),
                            style = TextStyle(
                                textDecoration = TextDecoration.LineThrough,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = TextSubdued
                            ),
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .align(Alignment.Center)
                ) {
                    loadImagePainterWithValidation(
                        imageUrl = item.image?.medium,
                        fallbackRes = Res.drawable.fallback,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(8.dp)),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    if (item.prices?.listPrice != null && item.prices.listPrice > item.prices.discountPrice.getValueOrDefault()) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.TopEnd)
                        ) {
                            GreenBox(
                                modifier = Modifier
                                    .background(
                                        Color(0xFF179C48),
                                        shape = RoundedCornerShape(
                                            topEnd = 8.dp,
                                            bottomStart = 8.dp
                                        )
                                    )
                                    .padding(start = 6.dp, end = 6.dp)
                                    .wrapContentSize()
                            ) {
                                Text(
                                    text = item.percentageOff(),
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = PoppinsFontFamily(),
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
                AnimatedContent(
                    targetState = itemQuantity > 0,
                    transitionSpec = {
                        slideInHorizontally { fullWidth -> fullWidth } + fadeIn() with
                                slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                    },
                    label = EMPTY_STRING,
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) { hasQuantity ->
                    if (!hasQuantity) {
                        AddButton(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = 5.dp, y = 5.dp),
                            onClick = {
                                if (item.hasVariationsOrChoiceGroups()) {
                                     onCardClick(item)
                                } else {
                                    val addRequest = AddItemRequestBody(
                                        itemId = item.id,
                                        quantity = 1,
                                        instructions = null,
                                        variationId = null,
                                        choiceIds = null
                                    )
                                    cartViewModel.addToCartFoodItem(addRequest)
                                    onAddClick(item)
                                }
                            }
                        )
                    } else {
                        QuantitySelector(
                            quantity = itemQuantity,
                            onIncrement = {
                                isFoodItemLoading = true
                                if (cartItemId != null) {
                                    foodCartViewModel.incrementFoodCartItemQuantity(
                                        cartItemId,
                                        updateCounterLoadingState = {
                                            isFoodItemLoading = false
                                        }
                                    )
                                }
                                onIncrementClick(item)
                            },
                            onDecrement = {
                                isFoodItemLoading = true
                                if (cartItemId != null) {
                                    foodCartViewModel.decrementFoodCartItemQuantity(
                                        cartItemId,
                                        updateCounterLoadingState = {
                                            isFoodItemLoading = false
                                        }
                                    )
                                }
                                onDecrementClick(item)
                            },
                            isUpdatingQuantity = isFoodItemLoading,
                            modifier = Modifier.align(Alignment.BottomEnd)
                        )
                    }
                }
            }
        }
    }
}