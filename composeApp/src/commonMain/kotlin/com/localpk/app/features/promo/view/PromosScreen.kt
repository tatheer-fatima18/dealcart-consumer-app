package com.localpk.app.features.promo.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.promo.components.PromoCodeItem
import com.localpk.app.features.promo.components.PromoCodeShimmer
import com.localpk.app.features.promo.domain.toPromoDetailScreen
import com.localpk.app.features.rewards.components.TopBar
import com.localpk.app.features.share_to_friend.presentation.ShareToFriendScreen
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.no_promo_available
import dealcart.composeapp.generated.resources.no_promos_icon
import dealcart.composeapp.generated.resources.promo_codes
import dealcart.composeapp.generated.resources.promo_codes_hasil
import dealcart.composeapp.generated.resources.promocode
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class PromosScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<PromoListingViewModel>()
        val promos by viewModel.promosListing.collectAsState()
        val isLoaded by viewModel.isPromosLoaded.collectAsState()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val navigator = LocalNavigator.current
        StatusBarIconColor(backgroundColor = Color.Blue)
        Surface {
            Crossfade(
                targetState = isLoaded,
                label = EMPTY_STRING
            ) { target ->
                when (target) {
                    true -> {
                        Column {
                            TopBar(
                                onBackClick = { navigator?.pop() },
                                title = stringResource(Res.string.promo_codes),
                                onShareClick = {
                                    if (sharedPreferencesManager.isLoggedIn()) {
                                        navigator?.push(ShareToFriendScreen())
                                    } else {
                                        navigator?.push(
                                            PhoneInputScreen(
                                                navSource = ScreenSource.SHARE_TO_FRIEND.key
                                            )
                                        )
                                    }
                                }
                            )
                            if (promos.isNotEmpty()) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .paint(
                                                painter = painterResource(Res.drawable.promocode),
                                                contentScale = ContentScale.FillBounds
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(vertical = 26.dp),
                                            text = stringResource(Res.string.promo_codes_hasil),
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                fontFamily = PoppinsFontFamily(),
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF354158),
                                                textAlign = TextAlign.Center,
                                            )
                                        )
                                    }
                                    LazyColumn(
                                        modifier = Modifier.padding(horizontal = 20.dp)
                                    ) {
                                        itemsIndexed(promos) { _, item ->
                                            Spacer(modifier = Modifier.height(10.dp))
                                            PromoCodeItem(
                                                item,
                                                onClick = {
                                                    navigator?.push(
                                                        item.toPromoDetailScreen()
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.no_promos_icon),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = stringResource(Res.string.no_promo_available),
                                        style = TextStyle(
                                            fontFamily = PoppinsFontFamily(),
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    )
                                }
                            }
                        }
                    }

                    false -> {
                        Column {
                            TopBar(
                                onBackClick = { navigator?.pop() },
                                title = stringResource(Res.string.promo_codes),
                                onShareClick = {
                                    if (sharedPreferencesManager.isLoggedIn()) {
                                        navigator?.push(ShareToFriendScreen())
                                    } else {
                                        navigator?.push(
                                            PhoneInputScreen(
                                                navSource = ScreenSource.SHARE_TO_FRIEND.key
                                            )
                                        )
                                    }
                                }
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .paint(
                                        painter = painterResource(Res.drawable.promocode),
                                        contentScale = ContentScale.FillBounds
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = 26.dp),
                                    text = stringResource(Res.string.promo_codes_hasil),
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsFontFamily(),
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF354158),
                                        textAlign = TextAlign.Center,
                                    )
                                )
                            }
                            LazyColumn(
                                modifier = Modifier.padding(horizontal = 20.dp)
                            ) {
                                items(4) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    PromoCodeShimmer()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 