package com.localpk.app.features.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.GreenBox
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.cart.data.CartUiModel
import com.localpk.app.features.cart.data.ProductInfo
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.hour
import com.localpk.app.utils.isNotZero
import com.localpk.app.utils.minutes
import com.localpk.app.utils.seconds
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.baqaya_waqt
import dealcart.composeapp.generated.resources.flash_deal
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.ic_persons_members
import dealcart.composeapp.generated.resources.ic_time_remaining
import dealcart.composeapp.generated.resources.members
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun SingleCartProductItem(
    singleProduct: CartUiModel
) {
    val cartViewModel: CartViewModel = koinInject()

    val localCartItems by cartViewModel.localCartItems.collectAsState()
    val cartProductId = localCartItems[singleProduct.productId]?.cartProductId.getValueOrDefault()

    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (singleProduct.isFlashDeal) {
            var tvHours by remember { mutableStateOf("00") }
            var tvMinutes by remember { mutableStateOf("00") }
            var tvSeconds by remember { mutableStateOf("00") }

            val currentTime by rememberUpdatedState(newValue = singleProduct.remainingTime)

            LaunchedEffect(currentTime) {
                var timeLeft = currentTime
                while (timeLeft > 0) {
                    timeLeft -= 1000
                    tvHours = timeLeft.hour()
                    tvMinutes = timeLeft.minutes()
                    tvSeconds = timeLeft.seconds()
                    delay(1000)
                }
                // Set to 00:00:00 on finish
                tvHours = "00"
                tvMinutes = "00"
                tvSeconds = "00"
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(Res.string.flash_deal),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = ButtonSurfaceBrand,
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.members),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = TextSubdued,
                            textAlign = TextAlign.Center,
                        )
                    )
                    Image(
                        painter = painterResource(Res.drawable.ic_persons_members),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = ButtonSurfaceBrand
                                )
                            ) {
                                append(
                                    "${singleProduct.totalJoinedPersons}"
                                )
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = TextSubdued
                                )
                            ) {
                                append(
                                    "/${
                                        "${singleProduct.totalPersonsRequired}"
                                    }"
                                )
                            }
                        },
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "|",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = SurfaceBackground,
                        )
                    )
                    Text(
                        text = stringResource(Res.string.baqaya_waqt),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            color = TextSubdued
                        )
                    )
                    Image(
                        painter = painterResource(Res.drawable.ic_time_remaining),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "$tvHours : $tvMinutes : $tvSeconds",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = IconDanger
                        )
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, RoundedCornerShape(10.dp)),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .border(
                        BorderStroke(
                            2.dp,
                            BorderPrimaryDefault
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ),
            ) {
                loadImagePainterWithValidation(
                    imageUrl = singleProduct.productImage,
                    fallbackRes = Res.drawable.ic_dealcart_placeholder,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = singleProduct.productName,
                    style = TextStyle(
                        textAlign = TextAlign.Left,
                        color = TextSubdued,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 12.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = singleProduct.formattedDiscountedPrice(),
                        style = TextStyle(
                            textAlign = TextAlign.Left,
                            color = TextSubdued,
                            fontSize = 13.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    if (singleProduct.isFlashDeal || singleProduct.showMrp == false) {
                        Text(
                            text = singleProduct.formattedActualPrice(),
                            style = TextStyle(
                                textDecoration = TextDecoration.LineThrough,
                                color = TextSubdued,
                                fontSize = 11.sp,
                                fontFamily = PoppinsFontFamily()
                            )
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Bottom),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.End,
            ) {
                if (singleProduct.isFlashDeal || singleProduct.showMrp == false) {
                    Box {
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
                                modifier = Modifier.align(Alignment.Center),
                                text = singleProduct.percentageOff(),
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold,
                                )
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    QuantityCounter(
                        quantity = singleProduct.quantity,
                        increaseCounter = {
                            isLoading = true
                            if (cartProductId.isNotZero())
                                cartViewModel.incrementItemQuantity(
                                    productId = singleProduct.id,
                                    cartProductId = cartProductId,
                                    isCart = true,
                                    successCallback = {
                                        cartViewModel.fetchUserCart(updateCounterLoadingState = {
                                            isLoading = false
                                        })
                                    },
                                    failureCallback = {
                                        isLoading = false
                                    }
                                )
                        },
                        decreaseCounter = {
                            isLoading = true
                            if (singleProduct.quantity == 1) {
                                if (cartProductId.isNotZero())
                                    cartViewModel.removeCartItem(
                                        isCart = true,
                                        productData = ProductInfo(
                                            if (singleProduct.isFlashDeal) singleProduct.flashDealId.getValueOrDefault() else singleProduct.id,
                                            cartProductId,
                                            singleProduct.quantity,
                                            singleProduct.discountedPrice,
                                            singleProduct.productName,
                                        ),
                                        successCallback = {
                                            cartViewModel.fetchUserCart(updateCounterLoadingState = {
                                                isLoading = false
                                            })
                                        },
                                        failureCallback = {
                                            isLoading = false
                                        }
                                    )
                            } else {
                                if (cartProductId.isNotZero())
                                    cartViewModel.decreaseItemQuantity(
                                        singleProduct.id,
                                        cartProductId,
                                        isCart = true,
                                        successCallback = {
                                            cartViewModel.fetchUserCart(updateCounterLoadingState = {
                                                isLoading = false
                                            })
                                        },
                                        failureCallback = {
                                            isLoading = false
                                        }
                                    )
                            }
                        },
                        isUpdatingQuantity = isLoading
                    )
                }
            }
        }
    }
}