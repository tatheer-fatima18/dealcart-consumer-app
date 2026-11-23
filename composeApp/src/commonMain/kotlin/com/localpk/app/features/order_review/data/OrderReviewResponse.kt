package com.localpk.app.features.order_review.data

import com.localpk.app.features.orders.data.OrderDetailSalesOrderRating
import com.localpk.app.utils.formatOrderDeliveryDate
import kotlinx.serialization.Serializable


@Serializable
data class OrderReviewResponse(
    val body: OrderForReview?,
    val success: Boolean?,
)

@Serializable
data class OrderForReview(
    val shipmentId: Long,
    val salesOrderId: Long,
    val orderTotal: Long,
    val createdAt: String,
    val deliveredAt: String,
    val ratingStatus: String
) {

    fun mapToOrderReviewBottomSheetUIData(): OrderReviewBottomSheetUIData {
        return OrderReviewBottomSheetUIData(
            salesOrderId = this.salesOrderId,
            deliveryDate = this.deliveredAt.formatOrderDeliveryDate(),
            orderTotal = this.orderTotal
        )
    }
}

@Serializable
data class OrderReviewBottomSheetUIData(
    val salesOrderId: Long,
    val deliveryDate: String,
    val orderTotal: Long,
    val shipmentId: Long? = null,
    var continueListener: (rating: Int) -> Unit = { },
    var closeListener: (() -> Unit)? = null
) {

    fun mapToOrderReviewBottomSheetUIData(): OrderReviewBottomSheetUIData {
        return OrderReviewBottomSheetUIData(
            salesOrderId = this.salesOrderId,
            deliveryDate = this.deliveryDate.formatOrderDeliveryDate(),
            orderTotal = this.orderTotal,
            continueListener = this.continueListener
        )
    }
}


@Serializable
data class OrderReviewCategoriesResponse(
    val success: Boolean,
    val body: List<CategoryUIData>
)

@Serializable
data class CategoryUIData(
    val id: Long,
    val image: Image,
    val name: String,
    val reasons: List<ReasonUIData>
)
enum class RatingType(val value: String) {
    SCREEN_1_3_RATING("screen_1-3_rating"),
    SCREEN_4_RATING("screen_4_rating")
}

enum class ReviewStatus(val statusValue: String) {
    PENDING("pending"),
    SENT_TO_CUSTOMER("sent_to_consumer"),
    CUSTOMER_CANCELLED("customer_cancelled"),
    REVIEWED("reviewed")
}

@Serializable
data class Image(
    val full: String,
    val medium: String,
    val path: String,
)

@Serializable
data class ReasonUIData(
    val id: Long,
    val name: String
)

@Serializable
data class OrderDetailSalesOrderRating(
    val rating: String,
)

@Serializable
data class OrderReviewData(
    val shipmentId: Long,
    val salesOrderId: Long,
    val orderTotal: Long,
    val createdAt: String,
    val deliveredAt: String,
    val salesOrderRating: OrderDetailSalesOrderRating?,
    val orderStatus: String,
)