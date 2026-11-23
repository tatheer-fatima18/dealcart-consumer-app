package com.localpk.app.features.share_to_friend.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.localpk.app.common_components.LoaderScreen
import com.localpk.app.features.profile.data.CustomerSavingDetailData
import com.localpk.app.features.rewards.data.Milestones
import com.localpk.app.features.rewards.viewModel.RewardsViewModel
import com.localpk.app.features.share_to_friend.components.GrayBg
import com.localpk.app.features.share_to_friend.components.ShareDetailsBox
import com.localpk.app.features.share_to_friend.data.LinkBody
import com.localpk.app.features.share_to_friend.viewModel.ShareToFriendViewModel
import com.localpk.app.theming.DarkBlueBg
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.Yellow
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_back_arrow_white
import dealcart.composeapp.generated.resources.ic_profile_avatar_female
import dealcart.composeapp.generated.resources.profile_icon
import dealcart.composeapp.generated.resources.share_your_achievements
import dealcart.composeapp.generated.resources.your_friends
import dealcart.composeapp.generated.resources.your_profile_snapshot
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class ShareToFriendScreen : Screen {
    @Composable
    override fun Content() {
        val shareToFriendViewModel = koinViewModel<ShareToFriendViewModel>()
        val rewardsViewModel = koinViewModel<RewardsViewModel>()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()

        val milestonesData by rewardsViewModel.milestonesData.collectAsState()
        val gender by rewardsViewModel.gender.collectAsState()
        val customerSavingDetail by rewardsViewModel.customerSavingDetail.collectAsState()
        val appLinkData by shareToFriendViewModel.appLinkData.collectAsState()

        StatusBarIconColor(backgroundColor = Color.White)
        Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
            Crossfade(
                targetState = milestonesData != null && customerSavingDetail != null,
                label = "share_to_friend_crossfade"
            ) { target ->
                if (target) {
                    ShareScreen(
                        milestones = milestonesData!!,
                        gender = gender,
                        customerSavingDetail = customerSavingDetail!!,
                        appLinkData = appLinkData,
                        sharedPreferencesManager = sharedPreferencesManager
                    )
                } else {
                    LoaderScreen()
                }
            }
        }
    }
}

@Composable
fun ShareScreen(
    milestones: Milestones,
    gender: String,
    customerSavingDetail: CustomerSavingDetailData,
    appLinkData: LinkBody?,
    sharedPreferencesManager: SharedPreferencesManager
) {
    // You can add username and other logic as needed
    val navigator = LocalNavigator.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlueBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()).statusBarsPadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_back_arrow_white),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navigator?.pop() }
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                )
                Text(
                    text = "Share",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.your_profile_snapshot),
                        modifier = Modifier
                            .padding(top = 6.dp),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = Yellow,
                            fontSize = 22.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = stringResource(Res.string.share_your_achievements) +
                                stringResource(Res.string.your_friends),
                        modifier = Modifier,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }


            ShareDetailsBox(
                username = sharedPreferencesManager.getUserName(),
                displayedUsername = sharedPreferencesManager.getUserName(),
                coins = milestones.toMilestonesUIData().formattedTotal,
                bgGender = if (gender == "male") Res.drawable.profile_icon else Res.drawable.ic_profile_avatar_female,
                savings = customerSavingDetail.totalSavings.toString()
            )

            Spacer(modifier = Modifier.height(200.dp)) // Add space for GrayBg at bottom
        }

        // GrayBg positioned at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            GrayBg(
                snapShot = null,
                message = appLinkData?.newUserReward?.toString() ?: EMPTY_STRING,
                link = appLinkData?.shortLink ?: EMPTY_STRING
            )
        }
    }
} 