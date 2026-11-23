package com.localpk.app.features.cart.data

import com.localpk.app.features.productlist.data.ProductImage
import com.localpk.app.features.restaurant.data.FoodCartUIData
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.ZERO_STRING
import com.localpk.app.utils.default
import com.localpk.app.utils.formatCartDeliveryDate
import com.localpk.app.utils.formatDate
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.ceil
import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlin.math.floor

@Serializable
data class CartResponse(
    val body: CartBody,
    val success: Boolean,
) : JavaSerializable

@Serializable
data class CartBody(
    val id: Int? = null,
    val items: List<Cart>? = emptyList(),
    val cartTotal: Double? = null,
    val deliveryFee: Double? = null,
    val dcDeliveryFee: Double? = null,
    val thirdPartyDeliveryFee: Double? = null,
    val discountTotal: Double? = null,
    val amountInCurrency: Double? = null,
    val isDCCoinEnabled: Boolean? = false,
    val totalDCCoins: Int? = null,
    val isSameDayDeliveryEligible: Boolean? = false,
    val sameDayDeliveryFee: Double? = null
) : JavaSerializable {
    private fun mapSegregationCart(cart: Cart?) = SegregationCartUiData(
        cartDetails = cart?.cartDetails?.map { it.map() }
            ?.filter { it.isFlashDeal.not() }.orEmptyArrayList(),
        flashDeals = cart?.cartDetails?.map { detail -> detail.map() }?.filter { it.isFlashDeal }
            .orEmptyArrayList(),
        PromoDetails = cart?.PromoDetails,
        deliveryFee = cart?.deliveryFee.getValueOrDefault(),
        discountTotal = cart?.discountTotal.getValueOrDefault(),
        type = cart?.type.default(),
        cartTotal = cart?.cartTotal.getValueOrDefault(),
        amountNeededForFreeDelivery = cart?.amountNeededForFreeDelivery.getValueOrDefault(),
        minimumOrderValue = cart?.minimumOrderValue.getValueOrDefault(399),
        hasNicotineTobaccoItems = cart?.hasNicotineTobaccoItems == true,
        hasRestrictedPromotionalDiscountProducts = cart?.hasRestrictedPromotionalDiscountProducts == true,
        bundle = cart?.bundle,
        deliveryDate = cart?.deliveryDate.default(),
        deliveryStartRange = cart?.deliveryStartRange.default(),
        deliveryEndRange = cart?.deliveryEndRange.default(),
        deliverBy = cart?.deliverBy.default(),
        sellerId = cart?.sellerId.getValueOrDefault(),
        sellerName = cart?.sellerName.default(),
        isEnabled = cart?.isEnabled == true
    )

    fun dcCityCart() = items?.firstOrNull { it.type == CartOrderType.DC_CITY.key }

    fun multiCityCart() =
        items?.firstOrNull {
            (it.type == CartOrderType.MULTI_CITY.key || it.type == CartOrderType.DC_CITY_NON_SERVICEABLE.key) && it.sellerId == 1L && it.sellerName.default()
                .trim().lowercase() == CartOrderType.DEAL_CART.key
        }

    private fun sellerCartUiData() = items?.filter {
        (it.type == CartOrderType.MULTI_CITY.key || it.type == CartOrderType.DC_CITY_NON_SERVICEABLE.key) && it.sellerId != 1L && it.sellerName.default() != CartOrderType.DEAL_CART.key
    }

    fun mapCartUiData() = CartUiData(
        id = id.getValueOrDefault(),
        dcCityCart = mapSegregationCart(dcCityCart()),
        multiCityCart = mapSegregationCart(multiCityCart()),
        sellerCarts = sellerCartUiData()?.map { mapSegregationCart(it) },
        cartTotal = cartTotal.getValueOrDefault(),
        dcDeliveryFee = dcDeliveryFee.getValueOrDefault(),
        multiCityDeliveryFee = deliveryFee.getValueOrDefault(),
        discountTotal = discountTotal.getValueOrDefault(),
        amountInCurrency = amountInCurrency.getValueOrDefault(),
        isDCCoinEnabled = isDCCoinEnabled.default(),
        totalDCCoins = totalDCCoins.getValueOrDefault(),
        formattedCartTotal = cartTotal.getValueOrDefault().formatPKRCurrency(),
        formattedAmountInCurrency = floor(amountInCurrency.getValueOrDefault()).formatPKRCurrency(),
        formattedTotalDCCoins = totalDCCoins.getValueOrDefault().toDouble().formatPKRCurrency(),
        formattedTotalPromoDiscount = (
                dcCityCart()?.PromoDetails?.discountValue?.toDouble()
                    .getValueOrDefault() + multiCityCart()?.PromoDetails?.discountValue?.toDouble()
                    .getValueOrDefault()
                ).formatPKRCurrency(),
        formattedTotalDeliveryFee = EMPTY_STRING,
        isSameDeliveryEligible = isSameDayDeliveryEligible.default(),
        sameDayDeliveryFee = sameDayDeliveryFee.getValueOrDefault()
    )
}

fun cartUiData() = CartUiData(
    id = ZERO_INT,
    dcCityCart = null,
    multiCityCart = null,
    sellerCarts = emptyList(),
    cartTotal = ZERO,
    dcDeliveryFee = ZERO,
    multiCityDeliveryFee = ZERO,
    discountTotal = ZERO,
    amountInCurrency = ZERO,
    isDCCoinEnabled = false,
    totalDCCoins = ZERO_INT,
    formattedCartTotal = ZERO_STRING,
    formattedAmountInCurrency = ZERO_STRING,
    formattedTotalDCCoins = ZERO_STRING,
    formattedTotalPromoDiscount = ZERO_STRING,
    formattedTotalDeliveryFee = EMPTY_STRING
)


@Serializable
data class CartUiData(
    val id: Int,
    val dcCityCart: SegregationCartUiData?,
    val multiCityCart: SegregationCartUiData?,
    val sellerCarts: List<SegregationCartUiData>?,
    val amountInCurrency: Double = ZERO,
    val cartTotal: Double,
    val dcDeliveryFee: Double,
    val multiCityDeliveryFee: Double,
    val totalDeliveryFee: Double = ZERO,
    val discountTotal: Double,
    val totalDCCoins: Int = ZERO_INT,
    val isDCCoinEnabled: Boolean = false,
    val formattedTotalDeliveryFee: String,
    val formattedCartTotal: String,
    val formattedAmountInCurrency: String,
    val formattedTotalDCCoins: String,
    val formattedTotalPromoDiscount: String,
    val isSameDeliveryEligible: Boolean = false,
    val sameDayDeliveryFee: Double = ZERO
) : JavaSerializable {
    fun formattedTotalPromoDiscount(
        isDCCityPromoEnable: Boolean,
        isMultiCityPromoEnable: Boolean
    ): String {
        val dcDiscount = dcCityCart?.PromoDetails?.discountValue?.toDouble().getValueOrDefault()
        val multiDiscount =
            multiCityCart?.PromoDetails?.discountValue?.toDouble().getValueOrDefault()

        val totalDiscount = when {
            isDCCityPromoEnable && isMultiCityPromoEnable -> dcDiscount + multiDiscount
            isDCCityPromoEnable -> dcDiscount
            isMultiCityPromoEnable -> multiDiscount
            else -> 0.0
        }

        return totalDiscount.formatPKRCurrency()
    }

    fun totalPromoDiscount(
        isDCCityPromoEnable: Boolean,
        isMultiCityPromoEnable: Boolean
    ): Double {
        var totalPromoDiscount = ZERO

        totalPromoDiscount += if (isDCCityPromoEnable) dcCityCart?.PromoDetails?.discountValue?.toDouble()
            .getValueOrDefault() else ZERO
        totalPromoDiscount += if (isMultiCityPromoEnable) multiCityCart?.PromoDetails?.discountValue?.toDouble()
            .getValueOrDefault() else ZERO

        totalPromoDiscount += sellerCarts?.sumOf {
            if (it.isEnabled) it.PromoDetails?.discountValue?.toDouble()
                .getValueOrDefault() else ZERO
        }.getValueOrDefault()

        return totalPromoDiscount
    }

    fun formattedSameDayDeliveryFee(): String =
        sameDayDeliveryFee.formatPKRCurrency()
}

@Serializable
data class SegregationCartUiData(
    val cartDetails: List<CartUiModel>,
    val flashDeals: List<CartUiModel>,
    val PromoDetails: CartPromo?,
    val deliveryFee: Double = ZERO,
    val discountTotal: Double,
    val type: String,
    val cartTotal: Double,
    val amountNeededForFreeDelivery: Int = ZERO_INT,
    val minimumOrderValue: Int,
    val hasNicotineTobaccoItems: Boolean,
    val hasRestrictedPromotionalDiscountProducts: Boolean,
    val bundle: BundleDetails?,
    val deliveryDate: String,
    val deliveryStartRange: String,
    val deliveryEndRange: String,
    val deliverBy: String,
    val sellerId: Long,
    val sellerName: String,
    var isEnabled: Boolean
) : JavaSerializable {
    fun getMinOrderVal(): Int =
        if (type == CartOrderType.DC_CITY.key) minimumOrderValue else ZERO_INT

    fun isBundle(): Boolean =
        if (type == CartOrderType.DC_CITY.key) bundle.isNotNull() else false

    fun isPostexDelivery(): Boolean =
        (type == CartOrderType.MULTI_CITY.key || type == CartOrderType.DC_CITY_NON_SERVICEABLE.key)

    fun formattedDeliveryDate(): String =
        if (isPostexDelivery()) "${deliveryStartRange.formatCartDeliveryDate()} - ${deliveryEndRange.formatCartDeliveryDate()}" else deliveryDate.formatCartDeliveryDate(
            showFullMonth = true
        )

    fun formattedDeliveryFee(): String =
        if (isPostexDelivery()) deliveryFee.formatPKRCurrency() else EMPTY_STRING
}

@Serializable
data class CartUiModel(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val isFlashDeal: Boolean = false,
    val productName: String,
    val productImage: String,
    val actualPrice: Double,
    val discountedPrice: Double,
    val remainingTime: Long,
    val totalJoinedPersons: Int = ZERO_INT,
    val totalPersonsRequired: Int = ZERO_INT,
    val fdDiscountPercentage: Double = ZERO,
    val expiryEndTime: Long,
    val showMrp: Boolean?,
    val flashDealLabel: String = if (RemoteConfigs.isEnabledFlashDealTesting()) "Group Deal" else "Flash Deal",
    val isQuantityButtonHidden: Boolean? = false,
    val parentCategories: List<String>,
    val flashDealId: Long? = null
) : JavaSerializable {
    fun formattedActualPrice(): String = actualPrice.formatPKRCurrency()
    fun formattedDiscountedPrice(): String = discountedPrice.formatPKRCurrency()

    fun percentageOff(): String {

        return if ((((actualPrice - discountedPrice) / actualPrice) * 100) <= 0)
            "1" // this is by design, as recommended by product team.
        else ceil((((actualPrice - discountedPrice) / actualPrice) * 100)).toInt()
            .toString() + "% OFF"
    }
}

@Serializable
data class CartPromo(
    val isValidated: Boolean,
    val promoName: String,
    val promoCode: String,
    val discountValue: Long = ZERO_LONG,
    val discountType: String? = EMPTY_STRING,
    val maxDiscountAllowed: Long = ZERO_LONG,
    val promoErrorMessage: String? = EMPTY_STRING,
    val expiryDate: String?,
    val id: Long = ZERO_LONG
) : JavaSerializable {
    fun formattedExpiryDate(): String = expiryDate?.formatDate().default()
}

@Serializable
data class CartCheckoutArgs(
    val dcCoins: Int,
    val isDcCoinEnabled: Boolean,
    val groups: List<CartUiModel>? = null,
    val dcCityFlashDeals: List<CartUiModel>? = null,
    val multiCityFlashDeals: List<CartUiModel>? = null,
    val cart: CartUiData? = null,
    val isDCCityOrder: Boolean? = false,
    val isMultiCityOrder: Boolean? = false,
    val discountedCartTotal: Int?,
    val overallCartAndCoinDiscount: Double?,
    val showMrp: Boolean? = true,
    val formattedPromoDiscount: String?,
    val packagingFee: Int? = ZERO_INT,
    val platformFee: Int? = ZERO_INT,
    val parentCategories: String? = null,
    val totalPromoDiscount: Double? = ZERO,
    val dcCoinDiscount: Double? = ZERO,
    val foodCart: FoodCartUIData? = null
) : JavaSerializable {
    fun formattedOverallCartAndCoinDiscount(): String =
        overallCartAndCoinDiscount.getValueOrDefault().formatPKRCurrency()

    fun formattedDcCoinDiscount(): String =
        cart?.amountInCurrency.getValueOrDefault().formatPKRCurrency()
}

fun cartTotalQuantity(list: List<CartUiModel>): Int {
    var quantity: Int = ZERO_INT
    list.forEach { quantity += it.quantity }
    return quantity
}

@Serializable
data class CartFlashDeal(
    val id: Long,
    val joinedCount: Int,
    val expiryEndTime: Long,
    val remainingTime: Long,
    val totalPersonsRequired: Int,
    val discountedPercentage: Double?
) : JavaSerializable

@Serializable
data class CartBundleResponse(
    val success: Boolean,
    val body: CartBundle?,
) : JavaSerializable

@Serializable
data class CartBundle(
    val maxBundles: Int?,
    val productOOSDetails: List<ProductOOSDetails>?
) : JavaSerializable

@Serializable
data class ProductOOSDetails(
    val availableQuantity: Int?,
    val product: CartProduct?,
    val requestedQuantity: Int?
) : JavaSerializable

@Serializable
data class BundleQuantity(
    val quantity: Int?
) : JavaSerializable

@Serializable
data class BundleDetails(
    val discountedPrice: Double?,
    val finalPrice: Double?,
    val name: String?,
    val discountPercentage: Double?,
    val quantity: Int?
) : JavaSerializable {
    fun formattedDiscountPrice(): String =
        discountedPrice.getValueOrDefault().formatPKRCurrency()

    fun formattedFinalPrice(): String = finalPrice.getValueOrDefault().formatPKRCurrency()
}

@Serializable
data class CartRequest(
    val detailed: Boolean
) : JavaSerializable

fun cartUiData(id: Int = ZERO_INT) = CartUiData(
    id = id,
    dcCityCart = null,
    multiCityCart = null,
    sellerCarts = null,
    cartTotal = ZERO,
    totalDCCoins = ZERO_INT,
    amountInCurrency = ZERO,
    isDCCoinEnabled = false,
    discountTotal = ZERO,
    formattedAmountInCurrency = EMPTY_STRING,
    formattedTotalDCCoins = EMPTY_STRING,
    formattedCartTotal = EMPTY_STRING,
    formattedTotalPromoDiscount = EMPTY_STRING,
    formattedTotalDeliveryFee = EMPTY_STRING,
    dcDeliveryFee = ZERO,
    multiCityDeliveryFee = ZERO
)

@Serializable
data class CartProduct(
    val id: Long? = ZERO_LONG,
    val name: String? = EMPTY_STRING,
    val images: List<ProductImage>? = emptyList(),
    val isMRPRestricted: Boolean? = false
) : JavaSerializable

@Serializable
data class CartPrices(
    val discounted: Boolean?,
    val discountedPrice: Double?,
    val actualPrice: Double?,
) : JavaSerializable

@Serializable
data class CartDetail(
    val id: Long,
    val product: CartProduct?,
    val prices: CartPrices?,
    val deal: CartFlashDeal? = null,
    val quantity: Int,
    val parentCategories: List<String>? = null,
) : JavaSerializable {
    fun map(userId: Long = 0) = CartUiModel(
        id = id,
        productId = product?.id ?: 0,
        quantity = quantity,
        productName = product?.name ?: EMPTY_STRING,
        actualPrice = prices?.actualPrice ?: 0.0,
        discountedPrice = prices?.discountedPrice ?: 0.0,
        productImage = product?.images?.firstOrNull()?.thumbnail
            ?: product?.images?.firstOrNull()?.path ?: EMPTY_STRING,
        isFlashDeal = deal.isNotNull(),
        totalJoinedPersons = deal?.joinedCount.getValueOrDefault(),
        totalPersonsRequired = deal?.totalPersonsRequired.getValueOrDefault(),
        fdDiscountPercentage = deal?.discountedPercentage.getValueOrDefault(),
        expiryEndTime = deal?.expiryEndTime.getValueOrDefault(),
        showMrp = product?.isMRPRestricted == true || (prices?.actualPrice
            ?: 0.0) <= (prices?.discountedPrice ?: 0.0),
        parentCategories = parentCategories.orEmptyArrayList(),
        remainingTime = if (deal?.remainingTime.isNotNull()) deal?.remainingTime.getValueOrDefault() else
            ZERO_LONG,
        flashDealId = deal?.id
    )
}

enum class CartOrderType(val key: String) {
    GROCERY("grocery"),
    POSTEX("postex"),
    DEAL_CART("dealcart"),
    DC_CITY("dc_service_area"),
    MULTI_CITY("global"),
    DC_CITY_NON_SERVICEABLE("dc_non_serviceable_area")
}

@Serializable
data class OneLastStepData(
    val discountTotal: Double?,
    val flashDeals: List<CartUiModel>
) : JavaSerializable {
    fun formattedDiscountTotal(): String =
        discountTotal.getValueOrDefault().formatPKRCurrency()
}

@Serializable
data class Cart(
    val type: String,
    val cartTotal: Double?,
    val discountTotal: Double?,
    val cartDetails: List<CartDetail>?,
    val PromoDetails: CartPromo? = null,
    val deliveryFee: Double = ZERO,
    val amountNeededForFreeDelivery: Int? = ZERO_INT,
    val minimumOrderValue: Int?,
    val hasNicotineTobaccoItems: Boolean?,
    val hasRestrictedPromotionalDiscountProducts: Boolean?,
    val bundle: BundleDetails?,
    val deliveryDate: String? = EMPTY_STRING,
    val deliveryStartRange: String? = EMPTY_STRING,
    val deliveryEndRange: String? = EMPTY_STRING,
    val deliverBy: String,
    val sellerId: Long,
    val sellerName: String,
    val isEnabled: Boolean? = true,
) : JavaSerializable


@Serializable
data class OOSBottomSheetArgs(
    val oosItems: List<ProductOOSDetails>
) : JavaSerializable {
    @Transient
    var onDismiss: () -> Unit = { }
}

