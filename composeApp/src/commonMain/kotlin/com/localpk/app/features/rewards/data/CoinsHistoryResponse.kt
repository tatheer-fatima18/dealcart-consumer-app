package com.localpk.app.features.rewards.data

import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import kotlinx.serialization.Serializable

@Serializable
data class CoinsHistoryResponse(
    val body: CoinsHistoryData,
    val success: Boolean
)

@Serializable
data class CoinsHistoryData(
    val data: CoinsHistory
)

@Serializable
data class CoinsHistory(
    val transactionHistory: List<CoinsHistoryDataList>,
    val totalCoinBalance: Double
) {
    fun formattedTotalCoinsBalance() = (totalCoinBalance).formatPKRCurrency()
}

@Serializable
data class CoinsHistoryDataList(
    val transactionDetailId: Long,
    val transactionDetailType: String,
    val transactionId: Long,
    val transactionType: String,
    val transactionUserId: Long,
    val transactionAmount: Double,
    val coinAmount: Double,
    val transactionCreatedAt: String,
    val transactionDetailReferenceNumber: String?,
    val transactionDetailMetadata: TransactionDetailMetadata? = null
) {
    fun toMapCoinsHistoryListUiData() = CoinsHistoryListUiData(
        transactionDetailId = transactionDetailId,
        transactionDetailType = transactionDetailType,
        transactionId = transactionId,
        transactionType = transactionType,
        transactionUserId = transactionUserId,
        transactionAmount = transactionAmount,
        formattedTransactionAmount = (transactionAmount).formatPKRCurrency(),
        coinAmount = coinAmount,
        coinsDate = transactionCreatedAt,
        formattedCoinAmount = (
            coinAmount
        ).formatPKRCurrency(),
        transactionDetailReferenceNumber = transactionDetailReferenceNumber.default(),
        transactionDetailMetadata = transactionDetailMetadata
    )
}


@Serializable
data class CoinsHistoryListUiData(
    val transactionDetailId: Long,
    val transactionDetailType: String,
    val transactionId: Long,
    val transactionType: String,
    val transactionUserId: Long,
    val transactionAmount: Double,
    val formattedTransactionAmount: String,
    val coinAmount: Double,
    val coinsDate: String,
    val formattedCoinAmount: String,
    val transactionDetailReferenceNumber: String,
    val transactionDetailMetadata: TransactionDetailMetadata?,
) {
    fun getTransactionDetailType(): TransactionDetailType? {
        return when {
            transactionDetailType.equals(
                TransactionDetailType.ORDER.key,
                ignoreCase = true
            ) -> TransactionDetailType.ORDER

            transactionDetailType.equals(
                TransactionDetailType.TOP_UP.key,
                ignoreCase = true
            ) -> TransactionDetailType.TOP_UP

            transactionDetailType.equals(
                TransactionDetailType.REFUND.key,
                ignoreCase = true
            ) || transactionDetailType.equals(
                TransactionDetailType.SALES_ORDER_OUT_OF_STOCK_REFUND.key,
                ignoreCase = true
            ) || transactionDetailType.equals(
                TransactionDetailType.SALES_ORDER_CANCELLED_REFUND.key,
                ignoreCase = true
            ) || transactionDetailType.equals(
                TransactionDetailType.FLASH_DEAL_REFUND.key,
                ignoreCase = true
            ) || transactionDetailType.equals(
                TransactionDetailType.SALES_ORDER_RETURNED_REFUND.key,
                ignoreCase = true
            ) || transactionDetailType.equals(
                TransactionDetailType.SALES_ORDER_ITEM_REFUND.key,
                ignoreCase = true
            ) || transactionDetailType.equals(
                TransactionDetailType.MANUAL_TOP_UP.key,
                ignoreCase = true
            ) -> TransactionDetailType.REFUND

            else -> null
        }
    }

    fun getRefundReason(): String? {
        when {
            transactionDetailType.contains(
                TransactionDetailType.MANUAL_TOP_UP.key,
                ignoreCase = true
            ) -> {
                return if (transactionDetailMetadata?.topUpReason?.equals("other") == true)
                    transactionDetailMetadata.additionalDetail.default()
                else
                    transactionDetailMetadata?.topUpReason.default()
            }

            transactionDetailType.contains(
                TransactionDetailType.SALES_ORDER_RETURNED_REFUND.key,
                ignoreCase = true
            ) -> {
                return "Returned Order# $transactionDetailReferenceNumber"
            }

            transactionDetailType.contains(
                TransactionDetailType.SALES_ORDER_ITEM_REFUND.key,
                ignoreCase = true
            ) -> {
                return "Sales Order Item"
            }

            transactionDetailType.contains(
                TransactionDetailType.SALES_ORDER_CANCELLED_REFUND.key,
                ignoreCase = true
            ) -> {
                return "Order# $transactionDetailReferenceNumber Cancellation"
            }

            transactionDetailType.contains(
                TransactionDetailType.SALES_ORDER_OUT_OF_STOCK_REFUND.key,
                ignoreCase = true
            ) -> {
                return "Out of Stock Items"
            }

            transactionDetailType.contains(
                TransactionDetailType.FLASH_DEAL_REFUND.key,
                ignoreCase = true
            ) -> {
                return "Group Deal Expiry"
            }
        }
        return null
    }
}


@Serializable
data class TransactionDetailMetadata(
    val additionalDetail: String? = null,
    val topUpReason: String? = null,
    val topUpMethod: String? = null,
)

enum class TransactionDetailType(val key: String) {
    REFUND("refund"),
    TOP_UP("top_up"),
    ORDER("order_redemption"),
    FLASH_DEAL_REFUND("flash_deal_refund"),
    MANUAL_TOP_UP("manual_top_up"),
    SALES_ORDER_CANCELLED_REFUND("sales_order_cancelled_refund"),
    SALES_ORDER_ITEM_REFUND("sales_order_item_refund"),
    SALES_ORDER_OUT_OF_STOCK_REFUND("sales_order_out_of_stock_refund"),
    SALES_ORDER_RETURNED_REFUND("sales_order_returned_refund"),
}

enum class TransactionType(val key: String) {
    ON_HOLD("on-hold"),
    CREDIT("credit"),
    DEBIT("debit"),
    EXPIRED("expired")
}
