package com.localpk.app.features.restaurant.data

import com.localpk.app.features.cart.data.CartPromo
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import kotlinx.serialization.Serializable
import kotlin.math.ceil
import kotlin.math.floor

@Serializable
data class FoodCart(
    val estimatedDeliveryTime: String? = EMPTY_STRING,
    val totalItemsAmount: Int?,
    val minimumOrderValue: Int? = null,
    val additionalFees: AdditionalFees?,
    val discounts: Discounts?,
    val items: List<Item>? = null,
    val id: Long? = null,
    val availableCoins: AvailableCoinsData?,
    val promoDetail: FoodPromo? = null
) {
    fun map(): FoodCartUIData {
        return FoodCartUIData(
            estimatedDeliveryTime = estimatedDeliveryTime.default(),
            totalItemsAmount = totalItemsAmount.getValueOrDefault(),
            minimumOrderValue = minimumOrderValue.getValueOrDefault(),
            additionalFees = additionalFees,
            discounts = discounts,
            items = items,
            id = id.getValueOrDefault(),
            availableCoins = availableCoins,
            promoDetail = promoDetail
        )
    }
}

@Serializable
data class AdditionalFees(
    val deliveryFee: Int? = null,
    val platformFee: Int?= null,
    val valueAddedTax: Double? = null
)

@Serializable
data class Discounts(
    val coinDiscount: Int?,
    val promoDiscount: Double?
)

@Serializable
data class Item(
    val cartItemId: Long?,
    val itemId: Long?,
    val name: String?,
    val image: String? = null,
    val description: String?,
    val quantity: Int?,
    val listPrice: Double?,
    val discountPrice: Double?,
    val variationName: String? = null,
    val choiceNames: List<String>?
) {
    fun formattedListPrice(): String =
        (listPrice.getValueOrDefault() * quantity.getValueOrDefault()).formatPKRCurrency()

    fun formattedDiscountedPrice(): String =
        (discountPrice.getValueOrDefault() * quantity.getValueOrDefault()).formatPKRCurrency()

    fun percentageOff(): String {
        return if ((((listPrice.getValueOrDefault() - discountPrice.getValueOrDefault()) / listPrice.getValueOrDefault()) * 100) <= 0)
            "1"
        else ceil(((listPrice.getValueOrDefault() - discountPrice.getValueOrDefault()) / listPrice.getValueOrDefault()) * 100).toInt()
            .toString() + "% OFF"
    }

    fun itemDiscount(): Double =
        (listPrice.getValueOrDefault() - discountPrice.getValueOrDefault()) * quantity.getValueOrDefault()
}

@Serializable
data class FoodCartUIData(
    val estimatedDeliveryTime: String,
    val totalItemsAmount: Int,
    val minimumOrderValue: Int,
    val additionalFees: AdditionalFees?,
    val discounts: Discounts?,
    val items: List<Item>? = null,
    val id: Long,
    val availableCoins: AvailableCoinsData?,
    val promoDetail: FoodPromo?
) {
    fun formattedAmountInCurrency(): String =
        (floor(availableCoins?.amountInCurrency.getValueOrDefault())).formatPKRCurrency()

    fun formattedTotalDCCoins(): String =
        (availableCoins?.totalDCCoins.getValueOrDefault().toDouble()).formatPKRCurrency()

    fun formattedOrderTotal(): String = (totalItemsAmount.toDouble()).formatPKRCurrency()

    fun formattedPromoCodeDiscount(): String =
        (discounts?.promoDiscount.getValueOrDefault()).formatPKRCurrency()

    fun formattedTax(): String =
        (additionalFees?.valueAddedTax.getValueOrDefault()).formatPKRCurrency()
}

@Serializable
data class AvailableCoinsData(
    val isDCCoinEnabled: Boolean? = false,
    val totalDCCoins: Int? = ZERO_INT,
    val amountInCurrency: Double? = ZERO
)

@Serializable
data class FoodPromoRequest(
    val promoId: Long,
    val coinsApplied: Int,
    val totalAmount: Int
)

@Serializable
data class FoodPromo(
    val promoCode: String? = EMPTY_STRING,
    val discountValue: Long = ZERO_LONG,
    val expiryDate: String? = EMPTY_STRING,
    val promoId: Long? = ZERO_LONG
) {
    fun map(): CartPromo {
        return CartPromo(
            expiryDate = expiryDate,
            promoCode = promoCode.default(),
            promoName = EMPTY_STRING,
            id = promoId.getValueOrDefault(),
            isValidated = true
        )
    }
}

@Serializable
data class AddFoodToCartResponse(
    val message: String?  = EMPTY_STRING,
    val statusCode: Int? = 0,
)
