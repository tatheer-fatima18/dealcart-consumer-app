package com.localpk.app.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.CART_BADGE_GREEN
import com.localpk.app.utils.EventLogger
import org.jetbrains.compose.resources.painterResource
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bottom_menu_cart_icon
import dealcart.composeapp.generated.resources.bottom_menu_home_icon
import dealcart.composeapp.generated.resources.bottom_menu_orders_icon
import dealcart.composeapp.generated.resources.bottom_menu_rewards_icon
import dealcart.composeapp.generated.resources.bottom_menu_wallet_icon
import org.koin.compose.koinInject

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    cartItemCount: Int = 0
) {
    val eventLogger: EventLogger = koinInject()

    val items = listOf(
        BottomNavItem("Home", painterResource(Res.drawable.bottom_menu_home_icon)),
        BottomNavItem("Orders", painterResource(Res.drawable.bottom_menu_orders_icon)),
        BottomNavItem("Wallet", painterResource(Res.drawable.bottom_menu_wallet_icon)),
        BottomNavItem("Rewards", painterResource(Res.drawable.bottom_menu_rewards_icon)),
        BottomNavItem("Cart", painterResource(Res.drawable.bottom_menu_cart_icon))
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(Color.White)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = BorderPrimaryDefault
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                val showBadge = index == 4 && cartItemCount > 0 // Cart icon (index 4) and has items

                Column(
                    modifier = Modifier
                        .clickable {
                            if (index == 4) {
                                eventLogger.orderConfirmScreen(source = "cart click from bottom nav")
                            }
                            onTabSelected(index)
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Image(
                            painter = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        )

                        // Badge for cart icon
                        if (showBadge) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 8.dp, y = (-4).dp)
                                    .size(20.dp)
                                    .background(
                                        color = CART_BADGE_GREEN,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (cartItemCount > 99) "99+" else cartItemCount.toString(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .offset(x = 0.dp, y = (-2).dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        }
    }
}

data class BottomNavItem(val label: String, val icon: Painter)