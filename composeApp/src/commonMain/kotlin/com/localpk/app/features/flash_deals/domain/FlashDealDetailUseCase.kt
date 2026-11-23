package com.localpk.app.features.flash_deals.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.flash_deals.data.DealPresenceConstants
import com.localpk.app.features.flash_deals.data.FlashDealDetail
import com.localpk.app.features.productlist.data.ProductDetailUiData
import com.localpk.app.features.productlist.data.ProductImage
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FlashDealDetailUseCase(
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    private val appRepository: AppRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FlowUseCase<Long, ViewState<ProductDetailUiData>>(dispatcher) {

    override fun execute(parameters: Long): Flow<ViewState<ProductDetailUiData>> =
        flow {
            emit(ViewState.Loading)
            appRepository.getFlashDealDetail(parameters).run {
                when (this) {
                    is ResponseResult.Success ->
                        data.body?.let { emit(ViewState.Success(getProductDetail(it))) }

                    else -> emit(errorResponseViewStateMapper.map(this))
                }
            }
        }

    private fun getProductDetail(flashData: FlashDealDetail): ProductDetailUiData {

        return ProductDetailUiData(
            id = flashData.productId,
            name = flashData.productName,
            description = flashData.description.default(),
            shortDescription = if (flashData.description.default().length > 82) flashData.description.default()
                .substring(0, 82) else flashData.description.default(),
            imageUrl = flashData.productImage.full,
            price = flashData.originalPrice.getValueOrDefault(),
            finalPrice = flashData.dealPrice,
            totalPersonsJoined = flashData.totalPersonsJoined,
            totalPersonsRequired = flashData.totalPersonsRequired,
            isFlashDeal = true,
            discountedPercentage = flashData.discountedPercentage,
            timeLeft = flashData.timeLeft,
            dealPresence = flashData.dealPresence,
            remainingPersons = flashData.totalPersonsRequired - flashData.totalPersonsJoined,
            groupEnabled = false,
            groupId = ZERO_LONG,
            groupPrice = flashData.dealPrice,
            groups = arrayListOf(),
            groupSize = ZERO_INT,
            purchasePrice = ZERO,
            images = arrayListOf(
                ProductImage(
                    EMPTY_STRING,
                    flashData.productImage.full ?: flashData.productImage.thumbnail
                    ?: flashData.productImage.medium,
                    EMPTY_STRING,
                    EMPTY_STRING
                )
            ),
            isFavourite = false,
            isBackInStockSubscribed = false,
            inventoryQty = ZERO_INT,
            isDealNone = flashData.dealPresence == DealPresenceConstants.NONE.key,
            maxPurchasableQuantity = flashData.maxPurchasableQuantity,
            expiryEndTime = flashData.expiryEndTime,
            showMrp = false,
            storeName = EMPTY_STRING,
            storeImage = EMPTY_STRING
        )
    }
}