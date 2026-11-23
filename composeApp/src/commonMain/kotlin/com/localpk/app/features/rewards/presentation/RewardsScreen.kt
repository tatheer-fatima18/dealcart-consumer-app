package com.localpk.app.features.rewards.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.localpk.app.features.banner.presentation.BannerViewModel
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.profile.data.CustomerSavingDetailData
import com.localpk.app.features.rewards.components.AnimatedImage
import com.localpk.app.features.rewards.components.DisableButton
import com.localpk.app.features.rewards.components.GamesSection
import com.localpk.app.features.rewards.components.HistoryButton
import com.localpk.app.features.rewards.components.LoginButton
import com.localpk.app.features.rewards.components.ToggleButtonDemo
import com.localpk.app.features.rewards.components.TopBar
import com.localpk.app.features.rewards.components.UpperSection
import com.localpk.app.features.rewards.data.Milestones
import com.localpk.app.features.rewards.viewModel.RewardsViewModel
import com.localpk.app.features.share_to_friend.presentation.ShareToFriendScreen
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.Yellow
import com.localpk.app.utils.COINS_CARD
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SAVINGS_CARD
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.capitalizeFirstLetter
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.avoid_traffic
import dealcart.composeapp.generated.resources.earn_dc_coins
import dealcart.composeapp.generated.resources.expiry
import dealcart.composeapp.generated.resources.ic_profile_avatar_female
import dealcart.composeapp.generated.resources.ic_profile_avatar_profile
import dealcart.composeapp.generated.resources.save_on_petrol
import dealcart.composeapp.generated.resources.title_reward_nav
import dealcart.composeapp.generated.resources.your_coins
import dealcart.composeapp.generated.resources.your_savings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class RewardsScreen : Screen {
    @Composable
    override fun Content() {
        val rewardsViewModel = koinViewModel<RewardsViewModel>()
        val bannerViewModel = koinViewModel<BannerViewModel>()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()

        LaunchedEffect(Unit) {
            bannerViewModel.getBanners(EMPTY_STRING, SAVINGS_CARD)
            bannerViewModel.getBanners(EMPTY_STRING, COINS_CARD)
        }

        val visibility by rewardsViewModel.layoutVisibility.collectAsState()
        val isLoggedIn = sharedPreferencesManager.isLoggedIn()
        val milestonesData by rewardsViewModel.milestonesData.collectAsState()
        val coinsCardImage by bannerViewModel.coinsCardBanners.collectAsState()
        val savingsCardImage by bannerViewModel.savingsCardBanners.collectAsState()
        val gender by rewardsViewModel.gender.collectAsState()
        val customerSavingDetail by rewardsViewModel.customerSavingDetail.collectAsState()
        val username = sharedPreferencesManager.getUserName()
        val authToken = sharedPreferencesManager.getToken()

        Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
            if (visibility) {
                Crossfade(
                    targetState = milestonesData != null && customerSavingDetail != null &&
                            savingsCardImage.isNotEmpty() && coinsCardImage.isNotEmpty(),
                    label = EMPTY_STRING
                ) { target ->
                    if (target) {
                        RewardsContent(
                            milestones = milestonesData,
                            gender = gender,
                            customerSavingDetail = customerSavingDetail,
                            username = username,
                            savingsImageUrl = savingsCardImage.firstOrNull()?.image?.path.default(),
                            coinsImageUrl = coinsCardImage.firstOrNull()?.image?.path.default(),
                            isLoggedIn = isLoggedIn,
                            authToken = authToken
                        )
                    } else {
                        RewardsContent(
                            milestones = null,
                            gender = gender,
                            customerSavingDetail = null,
                            username = username,
                            savingsImageUrl = savingsCardImage.firstOrNull()?.image?.path.default(),
                            coinsImageUrl = coinsCardImage.firstOrNull()?.image?.path.default(),
                            isLoggedIn = isLoggedIn,
                            authToken = authToken
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RewardsContent(
    milestones: Milestones?,
    gender: String,
    customerSavingDetail: CustomerSavingDetailData?,
    username: String,
    savingsImageUrl: String?,
    coinsImageUrl: String?,
    authToken: String,
    isLoggedIn: Boolean
) {
    val displayedUsername = if (username.isNotEmpty()) {
        if (username.length > 9) {
            ", ${username.take(9).capitalizeFirstLetter()}..!"
        } else {
            ", ${username.capitalizeFirstLetter()}!"
        }
    } else {
        EMPTY_STRING
    }

    val coins = milestones?.toMilestonesUIData()?.formattedTotal
    val savings = customerSavingDetail?.formattedTotalSavings()
    var isSelected by remember { mutableStateOf(true) }
    val greetingsUser = "Welcome $displayedUsername"
    val navigator = LocalNavigator.current


    val bgGender = when (gender) {
        "male" -> Res.drawable.ic_profile_avatar_profile
        else -> Res.drawable.ic_profile_avatar_female
    }
    StatusBarIconColor(backgroundColor = Color.Blue)

    Column(modifier = Modifier.fillMaxSize()) {

        TopBar(
            onBackClick = { navigator?.pop() },
            title = stringResource(Res.string.title_reward_nav),
            onShareClick = {
                if (isLoggedIn) {
                    navigator?.push(ShareToFriendScreen())
                } else {
                    navigator?.push(
                        PhoneInputScreen(
                            navSource = ScreenSource.SHARE_TO_FRIEND.key
                        )
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        6.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = greetingsUser,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 23.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    // Gender image
                    Image(
                        painter = painterResource(bgGender),
                        contentDescription = null,
                        modifier = Modifier
                            .size(38.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (!isLoggedIn) {
                    LoginButton()
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AnimatedImage(isSelected = isSelected, coinsImageUrl, savingsImageUrl)
                    if (isLoggedIn) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, bottom = 64.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (isSelected) stringResource(Res.string.your_coins) else stringResource(
                                    Res.string.your_savings
                                ),
                                style = TextStyle(
                                    textAlign = TextAlign.Left,
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = if (isSelected) coins.default() else savings.default(),
                                style = TextStyle(
                                    textAlign = TextAlign.Left,
                                    color = if (isSelected) Yellow else ColorPrimary,
                                    fontFamily = PoppinsFontFamily(),
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = if (isSelected) stringResource(Res.string.expiry) else stringResource(
                                    Res.string.save_on_petrol
                                ),
                                modifier = Modifier,
                                style = TextStyle(
                                    textAlign = TextAlign.Left,
                                    color = if (isSelected) Color(0xFFFFA000) else Color.Black,
                                    fontSize = 10.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Normal
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            if (isSelected) {
                                HistoryButton()
                            } else {
                                DisableButton()
                            }
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, bottom = 90.dp),
                            text = if (isSelected) stringResource(Res.string.earn_dc_coins) else stringResource(
                                Res.string.avoid_traffic
                            ),
                            style = TextStyle(
                                textAlign = TextAlign.Left,
                                color = if (isSelected) Yellow else ColorPrimary,
                                fontSize = 18.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ToggleButtonDemo(isSelected) {
                            isSelected = it
                        }
                    }
                }
                Spacer(modifier = Modifier.height(19.dp))
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            UpperSection(isLoggedIn)
            GamesSection(isLoggedIn, username, userToken = authToken)
//                StoriesSection()
        }
    }
}