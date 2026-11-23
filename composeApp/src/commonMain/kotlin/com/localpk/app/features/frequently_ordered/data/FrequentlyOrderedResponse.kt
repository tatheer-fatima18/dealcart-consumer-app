package com.localpk.app.features.frequently_ordered.data

import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import kotlinx.serialization.Serializable

@Serializable
data class FrequentlyOrderedResponse(
    val body: List<FrequentlyOrderedProductDetails>?,
    val success: Boolean,
)

@Serializable
data class FrequentlyOrderedProductDetails(
    val daysSinceLastOrder: Int?,
    val mrp: Double?,
    val sellingPrice: Double?,
    val productName: String?,
    val productId: Long,
    val productImage: String?,
    var isFavourite: Boolean?,
    val activeFlashDealId: Long? = ZERO_LONG,
    val isMRPRestricted: Boolean?
) {

    fun map(): ProductUiData {
        return ProductUiData(
            id = productId.getValueOrDefault(),
            flashDealId = activeFlashDealId.getValueOrDefault(),
            name = productName.default(),
            description = EMPTY_STRING,
            imageUrl = productImage,
            price = mrp.getValueOrDefault(),
            finalPrice = ZERO,
            discountedPrice = sellingPrice.getValueOrDefault(),
            inventoryQty = 0,
            productCategory = null,
            purchasePrice = ZERO,
            isFavourite = isFavourite.default(),
            isBackInStockSubscribed = false,
            isSponsored = false,
            slot = 0,
            isFlashDeal = activeFlashDealId.isNotNull(),
            showMrp = isMRPRestricted == true || sellingPrice.getValueOrDefault() >= mrp.getValueOrDefault(),
            purchaseLimit = 0,
            itemQuantity = 0
        )
    }

}



