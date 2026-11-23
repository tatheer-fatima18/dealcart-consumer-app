package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.restaurant.data.RestaurantDetailArgsData
import com.localpk.app.features.restaurant.data.RestaurantUI
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.capitalizeFirst
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.delivery_time
import dealcart.composeapp.generated.resources.fallback_cover
import dealcart.composeapp.generated.resources.fallback_logo
import dealcart.composeapp.generated.resources.free_delivery
import dealcart.composeapp.generated.resources.mov
import dealcart.composeapp.generated.resources.prep_time
import org.jetbrains.compose.resources.stringResource

@Composable
fun RestaurantCard(
    restaurant: RestaurantUI,
    onRestaurantCardClick: (RestaurantDetailArgsData) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, BorderPrimaryDefault),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRestaurantCardClick(
                    RestaurantDetailArgsData(
                        id = restaurant.id,
                        deliveryFee = restaurant.deliveryFee,
                        minimumOrderValue = restaurant.minimumOrderValue,
                        name = restaurant.name,
                        prepTime = restaurant.deliveryTime,
                        cuisines = restaurant.cuisines,
                    )
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column {
            loadImagePainterWithValidation(
                imageUrl = restaurant.coverImage?.medium,
                fallbackRes = Res.drawable.fallback_cover,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .height(127.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = restaurant.name,
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = TextSubdued,
                        )
                        Text(
                            text = restaurant.cuisines?.joinToString(", ").default()
                                .capitalizeFirst(),
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Normal,
                                color = TextSubduedHalf,
                            )
                        )
                    }

                    loadImagePainterWithValidation(
                        imageUrl = restaurant.logoImage?.medium,
                        fallbackRes = Res.drawable.fallback_logo,
                        contentDescription = null,
                        modifier = Modifier
                            .width(32.dp)
                            .height(32.dp)
                            .clip(shape = CircleShape)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    thickness = 1.dp,
                    color = BorderPrimaryDefault
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (restaurant.deliveryTime.isNotEmpty()) {
                        IconWithText(
                            icon = Res.drawable.prep_time,
                            text = restaurant.deliveryTime,
                            width = 24.dp,
                            fontSize = 14
                        )

                        VerticalSeparator(padding = 10.dp)
                    }

                    IconWithText(
                        icon = Res.drawable.delivery_time,
                        text = if (restaurant.deliveryFee.isNotZero()) "Rs ${restaurant.deliveryFee}" else stringResource(
                            Res.string.free_delivery
                        ),
                        width = 24.dp,
                        fontSize = 14
                    )

                    if (restaurant.minimumOrderValue.isNotZero()) {
                        VerticalSeparator(padding = 10.dp)
                        IconWithText(
                            icon = Res.drawable.mov,
                            text = "Min Rs ${restaurant.minimumOrderValue}",
                            width = 18.dp,
                            fontSize = 14
                        )
                    }
                }
            }
        }
    }
}
