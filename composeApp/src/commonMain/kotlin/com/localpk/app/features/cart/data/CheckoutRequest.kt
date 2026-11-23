package com.localpk.app.features.cart.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO_INT
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_cod
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.compose.resources.DrawableResource
import cafe.adriel.voyager.core.lifecycle.JavaSerializable

@Serializable
data class CheckoutRequest(
    val customer: Long,
    val address: Long,
    val latitude: Double,
    val longitude: Double,
    val specialInstructions: String = EMPTY_STRING,
    val paymentType: String = PaymentMethod.CASH_ON_DELIVERY.method,
    val paymentCnic: String? = EMPTY_STRING,
    val platformType: String? = EMPTY_STRING,
    val paymentPhoneNumber: String? = EMPTY_STRING,
    val isCoinEnabled: Boolean = false,
    val dcCoinDiscount: Int = ZERO_INT,
    val optionalContributionFee: Int? = null,
    val city: String,
    val packagingFee: Int? = null,
    val platformFee: Int? = null,
    val paymentDetails: PaymentDetails,
    val deliveryType: String
) : JavaSerializable

@Serializable
data class PaymentMethodUiData(
    val paymentType: String = PaymentMethod.CASH_ON_DELIVERY.method,
    val paymentId: Double = PaymentMethod.CASH_ON_DELIVERY.id,
    @Transient
    val listener: (paymentMethod: String, id: Double) -> Unit = { _, _ -> },
    val isFood: Boolean
) : JavaSerializable

@Serializable
data class CardPaymentScreenArgs(
    val cardDetails: CardDetails?,
    val paymentType: String
) : JavaSerializable

@Serializable
data class CardDetails(
    val encryptionKey: String?,
    val clientSecret: String?
) : JavaSerializable

@Serializable
data class PaymentMethodParams(
    val id: Double,
    @Contextual val imageRes: DrawableResource,
    val name: String,
    val paymentType: String,
    val isSelected: MutableState<Boolean>
) : JavaSerializable {
    fun select() {
        isSelected.value = true
    }
}

fun defaultPaymentMethod(paymentId: Double): PaymentMethodParams {
    val id = PaymentMethod.CASH_ON_DELIVERY.id
    return PaymentMethodParams(
        id = id,
        imageRes = Res.drawable.ic_cod,
        name = "Cash on Delivery",
        paymentType = PaymentMethod.CASH_ON_DELIVERY.method,
        isSelected = mutableStateOf(
            paymentId == PaymentMethod.CASH_ON_DELIVERY.id
        )
    )
}


enum class PaymentMethod(val method: String, val id: Double) {
    CASH_ON_DELIVERY("cod", 3.0),
    JAZZ_CASH("jazzcash", 1.0),
    CARD("card", 2.0),
    WALLET("wallet", 4.0),
    EASYPAISA("easypaisa", 5.0)
}

@Serializable
data class GetContributionValue(val contribution: Int) : JavaSerializable

@Serializable
data class PaymentDetails(
    val wallet: Double? = null,
    val card: Double? = null,
    val jazzcash: Double? = null,
    val cod: Double? = null,
    val easypaisa: Double? = null
) : JavaSerializable

enum class OrderPlacementPlatformType(val key: String) {
    ANDROID("android"),
    IOS("ios"),
}

enum class DeliveryType(val displayName: String, val key: String) {
    STANDARD("Standard delivery", "next_day"),
    EXPRESS("Express delivery", "same_day")
}

