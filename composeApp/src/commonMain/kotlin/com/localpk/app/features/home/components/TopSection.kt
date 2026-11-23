package com.localpk.app.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.AnimatedLottie
import com.localpk.app.features.restaurant.data.RestaurantOrderId
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.DarkTextColor
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.deliver_to
import dealcart.composeapp.generated.resources.ic_dealcart_orange
import dealcart.composeapp.generated.resources.is_order_in_progress
import dealcart.composeapp.generated.resources.menu_bar_hollow
import dealcart.composeapp.generated.resources.menu_drop_down
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopSection(
    address: String,
    onProfileAction: () -> Unit,
    onAddressClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    isLoading: Boolean,
    inboxCount: Int,
    searchText: String,
    isInboxEnabled: Boolean,
    onSearchClick: () -> Unit,
    topColor: Color,
    restaurantOrders: List<RestaurantOrderId>?,
    onOrderAction: () -> Unit,
    onOrderTrackingAction: () -> Unit,
    isFood: Boolean
) {
    var jsonString by remember { mutableStateOf<String?>(null) }

    // Launch coroutine to load Lottie JSON only once
    LaunchedEffect(Unit) {
        jsonString = Res.readBytes("files/chat.json").decodeToString()
    }

    Column(modifier = Modifier.background(topColor)) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp).statusBarsPadding()
        ) {
            if (isLoading && isFood.not()) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = DarkTextColor
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .padding(end = 10.dp)
            ) {
                Box(
                    modifier = Modifier

                ) {
                    IconButton(onClick = onProfileAction) {
                        Icon(
                            modifier = Modifier
                                .padding(5.dp)
                                .width(35.dp)
                                .height(35.dp),
                            painter = painterResource(Res.drawable.menu_bar_hollow),
                            contentDescription = "Menu",
                            tint = TextSubdued
                        )
                    }


                    if (isInboxEnabled && inboxCount > 0) {
                        Image(
                            modifier = Modifier
                                .width(16.dp)
                                .height(16.dp)
                                .align(Alignment.TopEnd),
                            painter = painterResource(Res.drawable.ic_dealcart_orange),
                            contentDescription = "Notification Badge"
                        )
                    }
                }


                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(Res.string.deliver_to),
                            maxLines = 1,
                            style = TextStyle(
                                color = TextSubdued,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,

                                ),
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.width(3.dp))

                        Image(
                            modifier = Modifier.clickable { onAddressClick() },
                            painter = painterResource(Res.drawable.menu_drop_down),
                            contentDescription = null,
                        )
                    }

                    Text(
                        text = address,
                        modifier = Modifier
                            .width(250.dp),
                        maxLines = 1,
                        style = TextStyle(
                            color = TextSubdued,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,

                            ),
                        overflow = TextOverflow.Ellipsis
                    )

                }


                if (restaurantOrders.isNullOrEmpty().not()) {
                    Image(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { if (restaurantOrders?.size.getValueOrDefault() > 1) onOrderAction() else onOrderTrackingAction() },
                        painter = painterResource(Res.drawable.is_order_in_progress),
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }


                jsonString?.let {
                    AnimatedLottie(
                        lottieSpec = LottieCompositionSpec.JsonString(it),
                        modifier = Modifier
                            .width(35.dp)
                            .height(30.dp),
                        useImage = true,
                        onClick = {
                            onWhatsAppClick()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(1.dp, BorderPrimaryDefault, shape = RoundedCornerShape(8.dp))
                    .clickable { onSearchClick() }
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                placeholder = searchText
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}