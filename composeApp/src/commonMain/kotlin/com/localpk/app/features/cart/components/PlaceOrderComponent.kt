package com.localpk.app.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.RobotoCondensedFamily
import com.localpk.app.theming.SaveGreen
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.place_order
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaceOrderComponent(
    onCheckOutClick: () -> Unit,
    overAllDiscount: String,
    discountedCartTotal: Int,
    isOrderInProgress: Boolean?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(SurfaceBackground),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = discountedCartTotal.toDouble().formatPKRCurrency(),
                        style = TextStyle(
                            color = TextSubdued,
                            fontSize = 16.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .width(2.dp)
                            .height(15.dp)
                            .background(color = SurfaceBackground)
                    )
//                    Text(
//                        text = stringResource(
//                            R.string.items,
//                            MainActivity.bagdeCount.value.getValueOrDefault()
//                        ),
//                        style = TextStyle(
//                            color = TextSubdued,
//                            fontSize = 12.sp,
//                            fontFamily = PoppinsFontFamily()
//                        )
//                    )
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
                text = stringResource(Res.string.place_order).uppercase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    color = if (isOrderInProgress.default()) TextSubduedHalf else Color.White,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .defaultMinSize(minWidth = 140.dp)
                    .background(
                        color = if (isOrderInProgress.default()) SurfaceBackground else ButtonSurfaceBrand,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = isOrderInProgress.default().not()){
                        onCheckOutClick()
                    }
                    .padding(horizontal = 30.dp, vertical = 8.dp)
            )
        }
    }
}