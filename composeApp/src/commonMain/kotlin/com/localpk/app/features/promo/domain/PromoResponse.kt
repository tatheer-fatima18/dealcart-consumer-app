package com.localpk.app.features.promo.domain

import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.bg_promo_green
import dealcart.composeapp.generated.resources.bg_promo_orange
import dealcart.composeapp.generated.resources.bg_promo_purple
import dealcart.composeapp.generated.resources.bg_promo_yellow
import dealcart.composeapp.generated.resources.get
import dealcart.composeapp.generated.resources.ic_earning_coins
import dealcart.composeapp.generated.resources.ic_ticket_discount
import dealcart.composeapp.generated.resources.save
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.features.promo.view.PromoDetailsScreen
import com.localpk.app.features.promo.view.PromoTermsBottomSheetScreen


@Serializable
data class PromoResponse(
    val body: List<Promo>?,
    val success: Boolean?,
) : JavaSerializable


@Serializable
data class Promo(
    val id: Long,
    val promoName: String,
    val promoCode: String,
    val startDate: String,
    val expiryDate: String,
    val minOrderValue: Long,
    val discountType: String,
    val discountValue: Long,
    val isActive: Boolean,
    val promoDescription: String,
    val termsAndConditions: String,
    val promoFor: String? = PromoType.GROCERY.key
) : JavaSerializable {
    fun toMapPromoUiData(): PromoUiData {
        var newDiscountLabel = Res.string.save
        var newDiscountText = EMPTY_STRING
        var newBgPromo = Res.drawable.bg_promo_yellow
        var newTicketDiscountIcon = Res.drawable.ic_ticket_discount
        when (discountType) {
            PromoItemConstants.FLAT.key -> {
                newDiscountLabel = Res.string.save
                newDiscountText = "$discountValue"
                newBgPromo = Res.drawable.bg_promo_yellow
                newTicketDiscountIcon = Res.drawable.ic_ticket_discount
            }

            PromoItemConstants.PERCENTAGE.key -> {
                newDiscountLabel = Res.string.save
                newDiscountText = "${discountValue}%"
                newBgPromo = Res.drawable.bg_promo_purple
                newTicketDiscountIcon = Res.drawable.ic_ticket_discount
            }

            PromoItemConstants.PERCENTAGE_OF_ORDER_COINS.key -> {
                newDiscountLabel = Res.string.get
                newDiscountText = "${discountValue}%"
                newBgPromo = Res.drawable.bg_promo_orange
                newTicketDiscountIcon = Res.drawable.ic_ticket_discount
            }

            PromoItemConstants.PROMO_CAMPAIGN.key -> {
                newDiscountLabel = Res.string.save
                newDiscountText = PromoItemConstants.PROMO_CAMPAIGN.key
                newBgPromo = Res.drawable.bg_promo_yellow
                newTicketDiscountIcon = Res.drawable.ic_ticket_discount
            }

            PromoItemConstants.FLAT_COINS.key -> {
                newDiscountLabel = Res.string.get
                newDiscountText = "$discountValue"
                newBgPromo = Res.drawable.bg_promo_green
                newTicketDiscountIcon = Res.drawable.ic_earning_coins
            }
        }
        return PromoUiData(
            discountLabel = newDiscountLabel,
            discountTxt = newDiscountText,
            bgPromo = newBgPromo,
            ticketDiscountIcon = newTicketDiscountIcon,
            promoFor = promoFor.default()
        )
    }
}

@Serializable
data class PromoUiData(
    @Contextual val discountLabel: StringResource,
    val discountTxt: String,
    @Contextual val bgPromo: DrawableResource,
    @Contextual val ticketDiscountIcon: DrawableResource,
    val promoFor: String
) : JavaSerializable

enum class PromoItemConstants(val key: String) {
    PROMO_CAMPAIGN("PROMO CAMPAIGN"),
    FLAT("flat"),
    PERCENTAGE("percentage"),
    FLAT_COINS("flat_coins"),
    PERCENTAGE_OF_ORDER_COINS("percentage_of_order_coins")
}

enum class PromoType(val key: String) {
    GROCERY("grocery"),
    FOOD("food")
}

fun Promo.toPromoDetailScreen(): PromoDetailsScreen {
    return PromoDetailsScreen(
        promoId = id,
        promoName = promoName,
        promoCode = promoCode,
        startDate = startDate,
        expiryDate = expiryDate,
        minOrderValue = minOrderValue,
        discountType = discountType,
        discountValue = discountValue,
        isActive = isActive,
        promoDescription = promoDescription,
        termsAndConditions = termsAndConditions
    )
}

fun Promo.toPromoTermsBottomSheetScreen(): PromoTermsBottomSheetScreen {
    return PromoTermsBottomSheetScreen(
        promoId = id,
        promoName = promoName,
        promoCode = promoCode,
        startDate = startDate,
        expiryDate = expiryDate,
        minOrderValue = minOrderValue,
        discountType = discountType,
        discountValue = discountValue,
        isActive = isActive,
        promoDescription = promoDescription,
        termsAndConditions = termsAndConditions
    )
}