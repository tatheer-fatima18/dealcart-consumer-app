package com.localpk.app.features.share_and_earn.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.localpk.app.base.domain.SocialSharingPlatform
import com.localpk.app.common_components.CustomSnackbar
import com.localpk.app.common_components.LoaderScreen
import com.localpk.app.features.rewards.components.TopBar
import com.localpk.app.features.rewards.data.MilestoneRewardsInfo
import com.localpk.app.features.share_and_earn.components.ShareAndEarnInvitation
import com.localpk.app.features.share_and_earn.components.ShareAndEarnSteps
import com.localpk.app.features.share_and_earn.data.ShareAndEarnUiData
import com.localpk.app.features.share_and_earn.data.ShareIcon
import com.localpk.app.features.share_and_earn.data.ShareStepsAndCoins
import com.localpk.app.features.share_and_earn.viewModel.ShareAndEarnViewModel
import com.localpk.app.features.share_to_friend.data.LinkBody
import com.localpk.app.features.share_to_friend.presentation.ShareToFriendScreen
import com.localpk.app.features.share_to_friend.viewModel.ShareToFriendViewModel
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.add
import dealcart.composeapp.generated.resources.bg_sharing
import dealcart.composeapp.generated.resources.dost_ke_pehlay
import dealcart.composeapp.generated.resources.dost_ke_signup
import dealcart.composeapp.generated.resources.doston_k_sath
import dealcart.composeapp.generated.resources.ic_facebook_share
import dealcart.composeapp.generated.resources.ic_message_share
import dealcart.composeapp.generated.resources.ic_messenger_share
import dealcart.composeapp.generated.resources.ic_twitter_share
import dealcart.composeapp.generated.resources.ic_whatsapp_share
import dealcart.composeapp.generated.resources.img_sharing
import dealcart.composeapp.generated.resources.share_and_earn
import dealcart.composeapp.generated.resources.signup_pe_apkay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class ShareAndEarnScreen : Screen {
    @Composable
    override fun Content() {
        val shareToFriendViewModel = koinViewModel<ShareToFriendViewModel>()
        val shareAndEarnViewModel = koinViewModel<ShareAndEarnViewModel>()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val navigator = LocalNavigator.current

        val appLinkData by shareToFriendViewModel.appLinkData.collectAsState()
        val milestoneInfo by shareAndEarnViewModel.milestoneInfoStateFlow.collectAsState()

        StatusBarIconColor(backgroundColor = Color.White)
        Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
            Crossfade(
                targetState = appLinkData != null && milestoneInfo != null,
                label = "share_and_earn_crossfade"
            ) { target ->
                if (target) {
                    ShareAndEarnContent(
                        appLinkData = appLinkData!!,
                        milestoneInfo = milestoneInfo!!,
                        sharedPreferencesManager = sharedPreferencesManager,
                        onBack = { navigator?.pop() },
                        onShareClick = {
                            navigator?.push(ShareToFriendScreen())
                        }
                    )
                } else {
                    LoaderScreen()
                }
            }
        }
    }
}

@Composable
fun ShareAndEarnContent(
    appLinkData: LinkBody,
    milestoneInfo: MilestoneRewardsInfo,
    sharedPreferencesManager: SharedPreferencesManager,
    onBack: () -> Unit,
    onShareClick: () -> Unit
) {
    val socialSharingPlatform = koinInject<SocialSharingPlatform>()
    val updatedReward = appLinkData.newUserReward.getValueOrDefault() / 10
    val message = "Earn ${
        updatedReward.toString().substringBeforeLast(".")
    } DC Coins when your friend signs up!"
    val link = appLinkData.shortLink.default()
    val milestonesUiData = milestoneInfo.toMilestoneRewardsInfoUIData()
    var showCopiedSnackbar by remember { mutableStateOf(false) }

    val uiData = ShareAndEarnUiData(
        shareSteps = listOf(
            ShareStepsAndCoins(
                step = stringResource(Res.string.dost_ke_signup),
                coins = milestonesUiData.formattedCCoinsForAppInstall.default()
            ),
            ShareStepsAndCoins(
                step = stringResource(Res.string.signup_pe_apkay),
                coins = milestonesUiData.formattedCoinsForAppSignup.default()
            ),
            ShareStepsAndCoins(
                step = stringResource(Res.string.dost_ke_pehlay),
                coins = milestonesUiData.formattedCoinsForFirstOrder.default()
            )
        ),
        icons = listOf(
            ShareIcon(Res.drawable.ic_whatsapp_share) {
                socialSharingPlatform.shareToWhatsApp(message, link)
            },
            ShareIcon(Res.drawable.ic_facebook_share) {
                socialSharingPlatform.shareToFacebook(message, link)
            },
            ShareIcon(Res.drawable.ic_messenger_share) {
                socialSharingPlatform.shareToMessenger(message, link)
            },
            ShareIcon(Res.drawable.ic_message_share) {
                socialSharingPlatform.shareToSMS(message, link)
            },
            ShareIcon(Res.drawable.ic_twitter_share) {
                socialSharingPlatform.shareToTwitter(message, link)
            },
            ShareIcon(Res.drawable.add) {
                socialSharingPlatform.shareToGeneral(message, link)
            }
        )
    )

    Box(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
        Image(
            modifier = Modifier
                .width(443.dp)
                .height(572.dp)
                .padding(top = 40.dp),
            painter = painterResource(Res.drawable.bg_sharing),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TopBar(
                onBackClick = onBack,
                title = stringResource(Res.string.share_and_earn),
                onShareClick = onShareClick
            )
            ShareAndEarnPage(uiData, link) { showCopiedSnackbar = true }

        }
    }

    CustomSnackbar(
        message = "Copied",
        showSnackbar = showCopiedSnackbar,
        onSnackbarDismissed = { showCopiedSnackbar = false }
    )
}

@Composable
fun ShareAndEarnPage(uiData: ShareAndEarnUiData, shareText: String, showSnackbar: () -> Unit = {}) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            Text(
                text = stringResource(Res.string.doston_k_sath),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.48.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    color = ColorPrimaryText,
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(modifier = Modifier.height(17.dp))
            Image(
                modifier = Modifier
                    .width(269.dp)
                    .height(73.3.dp),
                painter = painterResource(Res.drawable.img_sharing),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(24.dp))
            ShareAndEarnSteps(uiData.shareSteps)
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ShareAndEarnInvitation(uiData.icons, shareText, showSnackbar)


        }
    }
} 