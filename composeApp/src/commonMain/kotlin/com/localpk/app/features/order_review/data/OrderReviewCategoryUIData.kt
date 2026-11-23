package com.localpk.app.features.order_review.data


import kotlinx.serialization.Serializable


@Serializable
data class OrderReviewCategoryUIData(
    val id: Long,
    val name: String,
    val image: String,
)

@Serializable
data class OrderReviewCategoryReasonUIData(
    val id: Long,
    val name: String,
    var isSelected: Boolean = false
)

@Serializable
data class OrderReviewCategoryArgs(
    val id: Long,
    val shipmentId: Long,
    val rating: Int,
    var continueListener: (rating: Int) -> Unit = { },
    var successListener: (() -> Unit) = { }

)

@Serializable
data class OrderReviewSuccessArgs(
    val rating: Long,
    val orderId: Long,
    var continueListener: (() -> Unit) = { },
    var chatListener: (() -> Unit)= {},
)

@Serializable
data class AddOrderReviewRequest(
    val salesOrderId: Long,
    val shipmentId: Long,
    val rating: Long,
    val comment: String,
    val reasons: List<Long>
)

@Serializable
data class OrderReviewActionContinueArgs(
    val salesOrderId: Long,
    val shipmentId: Long,
    val rating: Int,
)

@Serializable
enum class RatingLevel(val value: String) {
    WORST("Badtareen"),
    BAD("Bura"),
    NEUTRAL("Munasib"),
    GOOD("Acha"),
    EXCELLENT("Behtareen")
}