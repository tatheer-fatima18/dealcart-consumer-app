package com.localpk.app.features.one_last_step.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.cart.data.OneLastStepData
import com.localpk.app.features.cart.data.OneLastStepUiData
import com.localpk.app.features.cart.domain.GetOneLastStepAdUseCase
import com.localpk.app.features.flash_deals.data.FlashDealUiData
import com.localpk.app.features.flash_deals.data.PaymentStatus
import com.localpk.app.features.rewards.data.MilestoneRewardsInfo
import com.localpk.app.features.rewards.data.MilestoneRewardsInfoResponse
import com.localpk.app.features.rewards.domain.MilestoneRewardsInfoUseCase
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.getDeliveryDate
import com.localpk.app.utils.getValueOrDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OneLastViewModel(
    val args: OneLastStepData,
    private val oneLastStepAdUseCase: GetOneLastStepAdUseCase,
    private val milestoneRewardsInfoUseCase: MilestoneRewardsInfoUseCase,
//    private val eventLogger: EventLogger,
) : BaseViewModel<Any>() {

    private val _discountedTotal = MutableStateFlow(0.0)
    val discountedTotal: StateFlow<Double> = _discountedTotal

    private val _deliveryDate = MutableStateFlow("")
    val deliveryDate: StateFlow<String> = _deliveryDate

    private val _videoStateFlow = MutableStateFlow<OneLastStepUiData?>(null)
    val videoStateFlow: StateFlow<OneLastStepUiData?> = _videoStateFlow

    private val _flashOrdersStateFlow = MutableStateFlow<List<FlashDealUiData>>(emptyList())
    val flashOrdersStateFlow: StateFlow<List<FlashDealUiData>> = _flashOrdersStateFlow

    val isShareButtonOnTop = MutableStateFlow(false)

    private val _milestoneInfoStateFlow = MutableStateFlow<MilestoneRewardsInfo?>(null)
    val milestoneInfoStateFlow: StateFlow<MilestoneRewardsInfo?> = _milestoneInfoStateFlow

    init {
        _discountedTotal.value = args.discountTotal.getValueOrDefault()
        _deliveryDate.value = getDeliveryDate()
        isShareButtonOnTop.value = RemoteConfigs.getOneLastStepShareButtonOnTopValue()
        getOneLastStepAd()
        getMileStoneInfo()

        if (args.flashDeals.isNotEmpty()) {
            val flashOrders = args.flashDeals.map { dealMapData ->
                FlashDealUiData(
                    productName = dealMapData.productName,
                    productImage = dealMapData.productImage,
                    productId = dealMapData.id,
                    originalPrice = dealMapData.actualPrice,
                    dealPrice = dealMapData.discountedPrice,
                    timeLeft = dealMapData.remainingTime,
                    totalPersonsJoined = dealMapData.totalJoinedPersons + 1,
                    totalPersonsRequired = dealMapData.totalPersonsRequired,
                    dealStatus = if (dealMapData.remainingTime > 0) "active" else "expired",
                    id = dealMapData.id,
                    quantityBought = dealMapData.quantity,
                    sloID = dealMapData.id,
                    discountPercentage = ((dealMapData.actualPrice - dealMapData.discountedPrice) / dealMapData.actualPrice) * 100,
                    expiryEndTime = dealMapData.expiryEndTime,
                    categoryName = EMPTY_STRING,
                    subCategoryName = EMPTY_STRING,
                    isAvailed = false,
                    isExpired = dealMapData.remainingTime <= 0,
                    status = if (dealMapData.remainingTime > 0) "active" else "expired",
                    paymentStatus = PaymentStatus.PAYMENT.paymentStatus,
                    itemQuantity = 0
                )
            }
            _flashOrdersStateFlow.value = flashOrders
        }

    }

    private fun getOneLastStepAd() {
        unZipViewStateFlow(
            oneLastStepAdUseCase(Unit),
            onSuccess = { items ->
                val data = items as List<OneLastStepUiData>
                if (data.isNotEmpty()) _videoStateFlow.value = data.first()
            }
        )
    }

    fun getMileStoneInfo() {
        unZipViewStateFlow(
            milestoneRewardsInfoUseCase(Unit),
            onSuccess = {
                val response = it as MilestoneRewardsInfoResponse
                _milestoneInfoStateFlow.value = response.body
            }
        )
    }

//    fun onActionShareAndWin() {
//        val linkRequest = CreateProductLinkRequest(
//            userId = ZERO.toLong(),
//            productId = ZERO.toLong(),
//            platform = EMPTY_STRING,
//            originalPrice = EMPTY_STRING,
//            socialTitle = EMPTY_STRING,
//            groupPrice = EMPTY_STRING,
//            socialImageLink = EMPTY_STRING,
//            isHomeScreen = false
//        )
//        navigate(
//            navigationProvider.getPushNavigator(
//                OneLastStepFragmentDirections.actionOlsToShareAndEarnBottomSheet(linkRequest)
//            )
//        )
//    }
//
//    fun onHowItWorksClick() {
//        eventLogger.clickShareEarnTutorialOLS()
//        InAppStoryManager.getInstance().showStory(
//            "1mzds6a",
//            App.application?.applicationContext,
//            AppearanceManager()
//        )
//    }


}