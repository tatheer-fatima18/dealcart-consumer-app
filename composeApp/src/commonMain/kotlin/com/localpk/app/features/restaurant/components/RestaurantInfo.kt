package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.restaurant.data.RestaurantDetailArgsData
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.capitalizeFirst
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.delivery_time
import dealcart.composeapp.generated.resources.fallback_logo
import dealcart.composeapp.generated.resources.free_delivery
import dealcart.composeapp.generated.resources.info
import dealcart.composeapp.generated.resources.mov
import dealcart.composeapp.generated.resources.prep_time
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RestaurantInfo(
    restaurantsDetails: RestaurantDetailArgsData,
    restaurantLogoImage: String,
    onInfoClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .border(
                BorderStroke(width = 1.dp, color = BorderPrimaryDefault),
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 13.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            loadImagePainterWithValidation(
                imageUrl = restaurantsDetails.logoImage,
                fallbackRes = Res.drawable.fallback_logo,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(shape = CircleShape)
            )
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = restaurantsDetails.name,
                    style = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = TextSubdued
                    ),
                )
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onInfoClick() },
                    painter = painterResource(Res.drawable.info),
                    tint = TextSubduedHalf,
                    contentDescription = null
                )
            }

            Text(
                text = restaurantsDetails.cuisines?.joinToString(", ").default()
                    .capitalizeFirst(),
                style = TextStyle(
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = TextSubduedHalf
                ),
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                thickness = 1.dp,
                color = BorderPrimaryDefault
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                if (restaurantsDetails.prepTime.isNotEmpty()) {
                    IconWithText(
                        icon = Res.drawable.prep_time,
                        text = restaurantsDetails.prepTime,
                        width = 24.dp,
                        fontSize = 14
                    )

                    VerticalSeparator(padding = 10.dp)
                }

                IconWithText(
                    icon = Res.drawable.delivery_time,
                    text = if (restaurantsDetails.deliveryFee.isNotZero()) "Rs ${restaurantsDetails.deliveryFee}" else stringResource(
                        Res.string.free_delivery
                    ),
                    width = 24.dp,
                    fontSize = 14
                )

                if (restaurantsDetails.minimumOrderValue.isNotZero()) {
                    VerticalSeparator(5.dp)
                    IconWithText(
                        icon = Res.drawable.mov,
                        text = "Min Rs ${restaurantsDetails.minimumOrderValue}",
                        width = 18.dp,
                        fontSize = 14
                    )
                }
            }
        }
    }
}