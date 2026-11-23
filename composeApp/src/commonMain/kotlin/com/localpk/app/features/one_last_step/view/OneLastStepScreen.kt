package com.localpk.app.features.one_last_step.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.cart.data.OneLastStepUiData
import com.localpk.app.features.checkout.view.CartCheckoutViewModel
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.one_last_step.components.CongratulationsSection
import com.localpk.app.features.one_last_step.components.FlashProduct
import com.localpk.app.features.one_last_step.components.TotalSavingsSection
import com.localpk.app.features.one_last_step.components.VideoCard
import com.localpk.app.features.one_last_step.viewModel.OneLastViewModel
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_spin_the_wheel_cross
import dealcart.composeapp.generated.resources.share_to_confirm
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class OneLastStepScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<CartCheckoutViewModel>()
        val oneLastStepData by viewModel.oneLastStepData.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        // Navigate to home if data is not available
        LaunchedEffect(oneLastStepData) {
            if (oneLastStepData == null) {
                // Clear navigation stack and go to home
                navigator.popUntil { false }
                navigator.push(HomeScreen())
            }
        }

        // Only render content when oneLastStepData is available
        if (oneLastStepData != null) {
            val data = oneLastStepData!!
            // Only create OneLastViewModel when oneLastStepData is not null
            val oneLastViewModel = koinViewModel<OneLastViewModel> {
                parametersOf(data)
            }

            val videoData by oneLastViewModel.videoStateFlow.collectAsState()
            StatusBarIconColor(backgroundColor = Color.White)
            Surface {
                OneLastStepScreen(
                    videoData,
                    viewModel.oneLastStepData.value?.formattedDiscountTotal().default()
                )
            }
        }
    }

    @Composable
    fun OneLastStepScreen(
        videoData: OneLastStepUiData?,
        formattedDiscountTotal: String
    ) {
        val navigator = LocalNavigator.currentOrThrow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            MainContent(
                onBack = { navigator.push(HomeScreen()) },
                videoData,
                formattedDiscountTotal
            )
        }
    }

    @Composable
    private fun MainContent(
        onBack: () -> Unit,
        videoData: OneLastStepUiData?,
        formattedDiscountTotal: String
    ) {

        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding).statusBarsPadding()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(7.dp))
                IconButton(
                    onClick = { onBack() },
                    modifier = Modifier
                        .align(Alignment.End)
                        .size(48.dp)
                        .padding(top = 8.dp)
                ) {
                    Image(
                        painterResource(Res.drawable.ic_spin_the_wheel_cross),
                        null,
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable {
                                onBack()
                            }
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxHeight()
                ) {
                    if (videoData.isNotNull())
                        VideoCard(
                            videoData = videoData,
                            onClick = {
//                                if (videoData.isNotNull()) {
//                                    val redirectType: String
//                                    val redirectTo: String
//                                    if (videoData?.isExternalLink == true) {
//                                        startActivity(
//                                            Intent(
//                                                Intent.ACTION_VIEW,
//                                                Uri.parse(videoData!!.externalLink)
//                                            )
//                                        )
//                                        redirectType = EXTERNAL_LINK
//                                        redirectTo = videoData!!.externalLink
//                                        eventLogger.olsClickAd(redirectType, redirectTo)
//                                    } else {
//                                        redirectType = INTERNAL_PRODUCT
//                                        redirectTo = videoData?.product?.name ?: EMPTY_STRING
//                                        val product = ProductUiData(
//                                            id = videoData?.productId.getValueOrDefault(),
//                                            name = EMPTY_STRING,
//                                            description = EMPTY_STRING,
//                                            imageUrl = EMPTY_STRING,
//                                            price = ZERO,
//                                            finalPrice = ZERO,
//                                            discountedPrice = ZERO,
//                                            inventoryQty = 0,
//                                            productCategory = null,
//                                            purchasePrice = ZERO,
//                                            isFavourite = false,
//                                            isBackInStockSubscribed = false,
//                                            isSponsored = false,
//                                            slot = 0,
//                                            showMrp = false,
//                                            purchaseLimit = 0,
//                                            itemQuantity = mutableIntStateOf(0)
//                                        )
//                                        eventLogger.olsClickAd(redirectType, redirectTo)
//                                        navigate(
//                                            OneLastStepFragmentDirections.actionOneLastStepToProductDetail(
//                                                product
//                                            )
//                                        )
//                                    }
//                                }
                            }
                        )
                    Spacer(modifier = Modifier.height(25.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxHeight()
                    ) {
                        CongratulationsSection()
                        Spacer(modifier = Modifier.height(20.dp))
                        TotalSavingsSection(
                            formattedDiscountTotal
                        )
//                        if (flashOrdersData.isEmpty() && milestoneData.isNotNull()) {
//                            Spacer(modifier = Modifier.height(10.dp))
//                            ShareAndEarnSection(
//                                milestoneData,
//                                onActionShareAndWin = {
//                                    //  oneLastStepViewModel.onActionShareAndWin()
//                                },
//                                onHowItWorksClick = {
//                                    //oneLastStepViewModel.onHowItWorksClick()
//                                }
//                            )
//                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun FlashDealBottomSheetContent(flashDeals: List<FlashDealUiData>) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(Res.string.share_to_confirm),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                color = ColorPrimaryText
            )

            if (flashDeals.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                flashDeals.forEachIndexed { _, it ->
                    FlashProduct(
                        flashDeal = it,
//                        onShareClick = {
////                        eventLogger.flashDealSharesDeal(
////                            it.id.getValueOrDefault(),
////                            it.productName.default(),
////                            FlashDealScreenName.OLS.key
////                        )
//                        viewModelFlashItem.fetchFlashShareLink(
//                            it.productId.getValueOrDefault(),
//                            it.id.getValueOrDefault(),
//                            it.productImage.default(),
//                            sharedPreferencesManager.getUserId()
//                        )
//                        dealId = it.id.getValueOrDefault()
//                    }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }


}