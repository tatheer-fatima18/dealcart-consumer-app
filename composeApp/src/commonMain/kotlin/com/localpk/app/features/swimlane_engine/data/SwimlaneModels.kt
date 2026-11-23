package com.localpk.app.features.swimlane_engine.data


import com.localpk.app.features.productlist.data.Product
import com.localpk.app.features.productlist.data.ProductUiData
import kotlinx.serialization.Serializable

@Serializable
data class SwimlaneRequestParams(
    val landingPageType: String,
    val userType: String,
    val superParentCategoryId: String,
    val id: Int? = null
)

@Serializable
data class SwimlaneResponse(
    val success: Boolean,
    val body: List<Swimlane>
)

@Serializable
data class SwimlaneDataResponse(
    val success: Boolean,
    val body: Swimlane
)

@Serializable
data class Swimlane(
    val id: Int?,
    val title: String? = null,
    val slug: String? = null,
    val products: List<Product>? = null,
    val categories: List<PromotionalCategory>? = null,
    val subtitle: String? = null,
    val value: Int? = null,
) {
    fun map(): SwimlaneUI {
        return SwimlaneUI(
            id = id ?: 0, // getValueOrDefault()
            title = title,
            slug = slug ?: "",
            products = products?.map { it.map() },
            categories = categories,
            subtitle = subtitle,
            value = value
        )
    }
}

@Serializable
data class SwimlaneUI(
    val id: Int,
    val title: String? = null,
    val slug: String,
    val products: List<ProductUiData>? = null,
    val categories: List<PromotionalCategory>? = null,
    val subtitle: String? = null,
    val value: Int? = null
) {
    fun getSwimlaneType(): SwimlaneType? {
        return when {
            slug.contains(
                SwimlaneType.PROMOTIONAL.type,
                ignoreCase = true
            ) -> SwimlaneType.PROMOTIONAL

            slug.contains(SwimlaneType.CART.type, ignoreCase = true) -> SwimlaneType.CART
            slug.contains(SwimlaneType.HOME.type, ignoreCase = true) -> SwimlaneType.HOME
            slug.contains(
                SwimlaneType.NEWLY_ADDED.type,
                ignoreCase = true
            ) -> SwimlaneType.NEWLY_ADDED

            slug.contains(
                SwimlaneType.GROUP_DEALS.type,
                ignoreCase = true
            ) -> SwimlaneType.GROUP_DEALS

            slug.contains(
                SwimlaneType.MAX_ABS_DISCOUNT.type,
                ignoreCase = true
            ) -> SwimlaneType.MAX_ABS_DISCOUNT

            slug.contains(
                SwimlaneType.MIN_AMOUNT_OFF.type,
                ignoreCase = true
            ) -> SwimlaneType.MIN_AMOUNT_OFF

            slug.contains(
                SwimlaneType.MIN_PERCENTAGE_OFF.type,
                ignoreCase = true
            ) -> SwimlaneType.MIN_PERCENTAGE_OFF

            slug.contains(
                SwimlaneType.UNDER_AMOUNT.type,
                ignoreCase = true
            ) -> SwimlaneType.UNDER_AMOUNT

            slug.contains(
                SwimlaneType.BEST_SELLING.type,
                ignoreCase = true
            ) -> SwimlaneType.BEST_SELLING

            else -> null
        }
    }
} 