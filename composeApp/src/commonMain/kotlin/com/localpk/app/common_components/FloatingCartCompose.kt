package com.localpk.app.common_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.SaveGreen
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import kotlinx.coroutines.delay
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.IconDangerHalf
import com.localpk.app.theming.MovGreen
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.RobotoCondensedFamily
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.formatPKRCurrency
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_close_normal
import dealcart.composeapp.generated.resources.truck_fast
import org.jetbrains.compose.resources.painterResource

@Composable
fun FloatingCartCompose(
    totalAmount: Double,
    isLessMOV: Boolean,
    dcCityCartTotal: Int,
    dcCartMinOrderVal: Float,
    overAllDiscount: Double,
    isProductDetail: Boolean,
    isFood: Boolean? = false,
    itemCount: Int = 0,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var isVisible by remember { mutableStateOf(false) }
        if (dcCityCartTotal > 0 && isLessMOV) {
            isVisible = true
        } else if (dcCityCartTotal <= 0) {
            isVisible = false
        }

        FloatingCartMessage(
            isLessMOV,
            dcCityCartTotal,
            dcCartMinOrderVal,
            isVisible
        )

        LaunchedEffect(isLessMOV) {
            if (isLessMOV.not()) {
                delay(3000)
                isVisible = false
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                FloatingCartAmount(
                    totalAmount,
                    overAllDiscount,
                    itemCount
                )
            }

            Text(
                text = "VIEW CART",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    color = if (isProductDetail) ButtonSurfaceBrand else Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .defaultMinSize(minWidth = 120.dp)
                    .border(
                        width = if (isProductDetail) 1.dp else 0.dp,
                        color = ButtonSurfaceBrand,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = if (isProductDetail) Color.White else ButtonSurfaceBrand,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick() }
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun FloatingCartMessage(
    isLessMOV: Boolean,
    dcCartTotal: Int,
    dcCartMinOrderVal: Float,
    isVisible: Boolean
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (dcCartTotal.toFloat() / dcCartMinOrderVal).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = EMPTY_STRING
    )
    // Always reserve the space to prevent layout shifts
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            AnimatedVisibility(
                visible = isVisible && dcCartTotal > 0 && dcCartMinOrderVal > 0,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
//                val progress = (dcCartTotal / dcCartMinOrderVal)
                val remainingOrderAmount = dcCartMinOrderVal - dcCartTotal

                Text(
                    text = if (isLessMOV) "Spend ${
                        remainingOrderAmount.toDouble().formatPKRCurrency()
                    } to place your order!" else "Yay! You can now place your order",
                    style = TextStyle(
                        color = TextSubdued,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFontFamily()
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 9.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(IconDangerHalf)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = animatedProgress)
                                .height(4.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (isLessMOV) IconDanger else MovGreen)
                        )
                    }
                    Icon(
                        painter = painterResource(Res.drawable.truck_fast),
                        tint = if (isLessMOV) TextSubduedHalf else ButtonSurfaceBrand,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingCartAmount(
    totalAmount: Double,
    overAllDiscount: Double,
    itemCount: Int = 0
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = totalAmount.formatPKRCurrency(),
                style = TextStyle(
                    color = TextSubdued,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamily()
                )
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .width(2.dp)
                    .height(12.dp)
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
            text = "You save ${overAllDiscount.formatPKRCurrency()}",
            style = TextStyle(
                color = SaveGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = RobotoCondensedFamily()
            )
        )
    }
} 