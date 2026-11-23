package com.localpk.app.features.home.components.swimlanes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.common_components.GreenBox
import com.localpk.app.common_components.NewCounter
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.cart.data.ProductInfo
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.product_detail.data.CartProductType
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.productlist.data.toProductDetailScreen
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.GradientRed
import com.localpk.app.theming.GradientYellow
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.subTotal
import com.localpk.app.theming.transparent
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.members
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun FlashDealItem(
    totalPersonsRequired: Int,
    totalPersonsJoined: Int,
    data: ProductUiData,
    cardClick: () -> Unit,
    onAddClick: () -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    val cartViewModel: CartViewModel = koinInject()
    val localCartItems by cartViewModel.localCartItems.collectAsState()
    val itemQuantity = localCartItems[data.id]?.quantity ?: 0
    val cartProductId = localCartItems[data.id]?.cartProductId

    Box(
        modifier = Modifier
            .width(155.dp)
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
            .height(289.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        BorderStroke(1.dp, transparent),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                loadImagePainterWithValidation(
                    imageUrl = data.imageUrl,
                    fallbackRes = Res.drawable.ic_dealcart_placeholder,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(155.dp)
                        .height(155.dp)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, transparent),
                            shape = RoundedCornerShape(8.dp)
                        )
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
                                text = data.percentageOff(),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = PoppinsFontFamily(),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(bottom = 2.5.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = data.name,
                        style = TextStyle(
                            color = TextPrimary,
                            fontFamily = PoppinsFontFamily(),
                            fontSize = 12.sp,
                            lineHeight = 13.2.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(vertical = 1.dp),
                        color = BorderPrimaryDefault
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .height(30.dp)
                            .border(
                                1.dp,
                                transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        GradientRed,
                                        GradientYellow
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(100f, 100f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(
                                    Res.drawable.members
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(12.dp)
                                    .height(12.dp)
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                text = "$totalPersonsJoined/$totalPersonsRequired members",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp)
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (data.itemQuantity <= 0) {
                            Text(
                                text = data.formattedDiscountedPrice(),
                                style = TextStyle(
                                    textAlign = TextAlign.Left,
                                    color = TextSubdued,
                                    fontSize = 14.sp,
                                    lineHeight = 14.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            if (data.showMrp?.not().default()) {
                                Text(
                                    text = data.formattedPrice(),
                                    modifier = Modifier.padding(top = 1.dp),
                                    style = TextStyle(
                                        textDecoration = TextDecoration.LineThrough,
                                        color = subTotal,
                                        fontSize = 12.sp,
                                        lineHeight = 12.sp,
                                        fontFamily = PoppinsFontFamily(),
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.weight(.1f))
                        }

                        NewCounter(
                            itemQuantity = itemQuantity,
                            onAddClick = {
                                cartViewModel.addToCart(
                                    cartProductType = CartProductType.DEAL,
                                    productData = data
                                )
                                onAddClick()
                            },
                            onDelete = {
                                if (cartProductId != null && cartProductId != 0L) {
                                    cartViewModel.removeCartItem(
                                        productData = ProductInfo(
                                            productId = data.flashDealId,
                                            cartProductId = cartProductId,
                                            quantity = localCartItems[data.id]?.quantity.getValueOrDefault(),
                                            price = data.discountedPrice,
                                            productName = data.name
                                        )
                                    )
                                }
                            },
                            onDecrement = {
                                if (cartProductId != null && cartProductId != 0L) {
                                    cartViewModel.decreaseItemQuantity(data.id, cartProductId)
                                }
                            },
                            onIncrement = {
                                if (cartProductId != null && cartProductId != 0L) {
                                    cartViewModel.incrementItemQuantity(data.id, cartProductId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}