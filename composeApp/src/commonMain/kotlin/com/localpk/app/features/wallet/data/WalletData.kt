package com.localpk.app.features.wallet.data

import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.utils.formatPKRCurrency
import kotlinx.serialization.Serializable
import cafe.adriel.voyager.core.lifecycle.JavaSerializable

@Serializable
data class WalletBalanceResponse(
    val success: Boolean,
    val body: WalletBalance?
)

@Serializable
data class GetTopUpAmount(val amount: Double)

@Serializable
data class WalletBalance(val Balance: Double) {

    fun formattedTotalWalletBalance(): String {
        return Balance.formatPKRCurrency()
    }
}

@Serializable
data class ResponseArgs(val success: Boolean, val amount: Double)

@Serializable
data class CardTopUpRequest(
    val amount: Double,
    val paymentType: String? = PaymentMethod.CARD.method
) : JavaSerializable

enum class TopUpSource(val source: String) {
    WALLET("wallet"),
    CHECKOUT("checkout")
}

enum class TopUpStatus(val status: String) {
    SUCCESS("success"),
    FAILURE("failure")
}