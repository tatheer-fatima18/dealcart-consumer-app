package com.localpk.app.features.cart.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.CartContainer
import com.localpk.app.common_components.FloatingCartMessage
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.RobotoCondensedFamily
import com.localpk.app.theming.SaveGreen
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.login_to_proceed
import dealcart.composeapp.generated.resources.next
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun CartCheckOutView(
    isLoggedIn: Boolean,
    onCheckOutClick: () -> Unit,
    overAllDiscount: String,
    discountedCartTotal: String,
    isAnyBucketChecked: Boolean? = true,
    totalAmount: Int? = null,
    mov: Int? = null,
    isFood: Boolean = false,
    itemCount: Int
) {
    val checkoutButtonText = if (isLoggedIn) {
        Res.string.next
    } else {
        Res.string.login_to_proceed
    }
    val isLessMOV =
        mov.getValueOrDefault() > totalAmount.getValueOrDefault()

    var isVisible by remember { mutableStateOf(false) }
    if (totalAmount.getValueOrDefault() > 0 && isLessMOV) {
        isVisible = true
    } else if (totalAmount.getValueOrDefault() <= 0) {
        isVisible = false
    }
    CartContainer {
            AnimatedVisibility(
                isVisible && isFood,
                exit = ExitTransition.None
            ) {
                FloatingCartMessage(
                    isLessMOV = isLessMOV,
                    dcCartTotal = totalAmount.getValueOrDefault(),
                    dcCartMinOrderVal = mov.getValueOrDefault().toFloat(),
                    isVisible
                )
            }
            LaunchedEffect(isLessMOV) {
                if (isLessMOV.not()) {
                    delay(300)
                    isVisible = false
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = discountedCartTotal,
                            style = TextStyle(
                                color = TextSubdued,
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .width(2.dp)
                                .height(15.dp)
                                .background(color = SurfaceBackground)
                        )
                        Text(
                            text = "$itemCount Items",
                            style = TextStyle(
                                color = TextSubdued,
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily()
                            )
                        )
                    }
                    Text(
                        text = "You've saved $overAllDiscount",
                        style = TextStyle(
                            color = SaveGreen,
                            fontSize = 14.sp,
                            fontFamily = RobotoCondensedFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Text(
                    text = stringResource(checkoutButtonText).uppercase(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = if (isAnyBucketChecked.default()) Color.White else TextSubduedHalf,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 140.dp)
                        .background(
                            color = if (isAnyBucketChecked.default()) ButtonSurfaceBrand else SurfaceBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(enabled = isAnyBucketChecked.default()) {
                            onCheckOutClick()
                        }
                        .padding(
                            vertical = 8.dp,
                            horizontal = if (isLoggedIn) 30.dp else 16.dp
                        )
                )

            }
    }
}