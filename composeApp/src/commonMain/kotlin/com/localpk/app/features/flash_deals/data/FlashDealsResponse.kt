package com.localpk.app.features.flash_deals.data

import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import kotlinx.serialization.Serializable
import kotlin.math.ceil

@Serializable
data class FlashDealsResponse(val body: List<FlashDealData>?, val success: Boolean)

@Serializable
data class FlashDealData(
    val id: Long,
    val productName: String?,
    val productImage: String?,
    val productId: Long?,
    val originalPrice: Double?,
    val discountPercentage: Double? = ZERO,
    val dealPrice: Double?,
    val timeLeft: Long?,
    val totalPersonsRequired: Int?,
    val totalPersonsJoined: Int?,
    val isAvailed: Boolean? = false,
    val dealStatus: String,
    val categoryName: String? = EMPTY_STRING,
    val subCategoryName: String? = EMPTY_STRING,
    val quantityBought: Int? = ZERO_INT,
    val sloID: Long? = null,
    val expiryEndTime: Long,
    val paymentStatus: String? = EMPTY_STRING
) {
    fun map(): FlashDealUiData = FlashDealUiData(
        id = id,
        productName = productName.default(),
        productImage = productImage.default(),
        productId = productId.getValueOrDefault(),
        originalPrice = originalPrice.getValueOrDefault(),
        discountPercentage = discountPercentage.getValueOrDefault(),
        dealPrice = dealPrice.getValueOrDefault(),
        timeLeft = timeLeft.getValueOrDefault(),
        totalPersonsRequired = totalPersonsRequired.getValueOrDefault(),
        totalPersonsJoined = totalPersonsJoined.getValueOrDefault(),
        isAvailed = isAvailed.default(),
        dealStatus = dealStatus,
        categoryName = categoryName.orEmpty(),
        subCategoryName = subCategoryName.orEmpty(),
        expiryEndTime = expiryEndTime,
        isExpired = dealStatus == "expired",
        status = if (dealStatus == "expired") dealStatus else "pending",
//        dealStatusColor = if (dealStatus == "expired") ProfileTextGray else DcOrangeDark,
//        dealOutlineBackground = if (dealStatus == "expired") R.drawable.bg_gray_outline_corners else R.drawable.bg_flash_deal_item,
        sloID = sloID,
        quantityBought = quantityBought,
        paymentStatus = paymentStatus.default(),
        itemQuantity = 0
    )
}

@Serializable
data class FlashDealUiData(
    val id: Long,
    val productName: String,
    val productImage: String,
    val productId: Long,
    val originalPrice: Double,
    val discountPercentage: Double,
    val dealPrice: Double,
    val timeLeft: Long,
    val totalPersonsRequired: Int,
    val totalPersonsJoined: Int,
    val isAvailed: Boolean,
    val dealStatus: String,
    val categoryName: String,
    val subCategoryName: String,
    val expiryEndTime: Long,
    val isExpired: Boolean?,
    val status: String?,
    val dealStatusColor: Int? = ZERO_INT,
    val dealOutlineBackground: Int? = ZERO_INT,
    val isFlashDealAB: Boolean? = RemoteConfigs.isEnabledFlashDealTesting(),
    val sloID: Long?,
    val quantityBought: Int?,
    val paymentStatus: String,
    var itemQuantity: Int,
) {
    fun dealPercentageOff(totalQuantity: Int? = 1): String {
        return if (((((originalPrice.getValueOrDefault() * totalQuantity.getValueOrDefault(1)) - (dealPrice.getValueOrDefault() * totalQuantity.getValueOrDefault(
                1
            ))) / (originalPrice.getValueOrDefault() * totalQuantity.getValueOrDefault(1))) * 100) <= 0
        )
            "1" // this is by design, as recommended by product team.
        else if (RemoteConfigs.isDiscountTypeAbsolute()
        ) (
                ((originalPrice.getValueOrDefault() * totalQuantity.getValueOrDefault(
                    1
                )) - (dealPrice.getValueOrDefault() * totalQuantity.getValueOrDefault(1)))
                ).formatPKRCurrency()
        else ceil(
            ((((originalPrice.getValueOrDefault() * totalQuantity.getValueOrDefault(1)) - (dealPrice.getValueOrDefault() * totalQuantity.getValueOrDefault(
                1
            ))) / (originalPrice.getValueOrDefault() * totalQuantity.getValueOrDefault(1))) * 100)
        ).toInt()
            .toString() + "%"
    }

    fun formattedOriginalPrice(): String = originalPrice.getValueOrDefault().formatPKRCurrency()
    fun formattedDealPrice(): String = dealPrice.getValueOrDefault().formatPKRCurrency()
}

enum class FlashDealStatus(val status: String) {
    ACTIVE("active"),
    FAILURE("failure"),
    NO_ACTIVE_DEALS("noActiveDeals")
}

enum class PaymentStatus(val paymentStatus: String) {
    PAYMENT("payment"),
    PARTIAL_REFUND("partial_refund"),
    REFUNDED("refund")
}

enum class OrderItemStatus(val itemStatus: String) {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    DELIVERED("delivered"),
    RETURNED("returned"),
    CANCELLED("cancelled"),
    OUT_OF_STOCK("out of stock")
}