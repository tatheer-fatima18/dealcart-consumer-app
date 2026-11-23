package com.localpk.app.features.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.cart.data.CartUiModel
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.five_add_on
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun CartCheckoutSegregation(
    storeName: String,
    isPostexDelivery: Boolean,
    formattedDeliveryDate: String,
    cartDetails: List<CartUiModel>,
    onClickDeliveryFee: () -> Unit = {},
    deliveryType: DeliveryType = DeliveryType.STANDARD,
    isDeliverySelectionAvailable: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CartCheckoutDeliveryInfo(
            storeName = storeName,
            isPostexDelivery = isPostexDelivery,
            formattedDeliveryDate = formattedDeliveryDate,
            onClickDeliveryFee = onClickDeliveryFee,
            deliveryType = deliveryType,
            isDeliverySelectionAvailable = isDeliverySelectionAvailable
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            items(cartDetails.take(4)) {
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    loadImagePainterWithValidation(
                        imageUrl = it.productImage,
                        fallbackRes = Res.drawable.ic_dealcart_placeholder,
                        contentDescription = null,
                        modifier = Modifier
                            .border(
                                BorderStroke(
                                    1.dp,
                                    BorderPrimaryDefault
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                            .size(64.dp)
                    )

                    Text(
                        modifier = Modifier
                            .background(
                                color = BorderPrimaryDefault,
                                shape = RoundedCornerShape(
                                    topStart = 6.4.dp,
                                    bottomEnd = 3.2.dp
                                )
                            )
                            .padding(vertical = 3.2.dp, horizontal = 4.9.dp),
                        text = "x${it.quantity}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            color = TextSubduedHalf,
                        )
                    )
                }
            }
            item {
                if (cartDetails.size > 4) {
                    Box(
                        modifier = Modifier
                            .border(
                                BorderStroke(
                                    1.dp,
                                    BorderPrimaryDefault
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                            .size(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.five_add_on),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily(),
                                color = TextSubduedHalf,
                            )
                        )
                    }
                }
            }
        }
    }
}