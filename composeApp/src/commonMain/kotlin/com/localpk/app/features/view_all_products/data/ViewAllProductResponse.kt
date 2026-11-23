package com.localpk.app.features.view_all_products.data

import com.localpk.app.features.productlist.data.GroupRange
import com.localpk.app.features.productlist.data.Inventory
import com.localpk.app.features.productlist.data.ProductImage
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import kotlinx.serialization.Serializable
import cafe.adriel.voyager.core.lifecycle.JavaSerializable

@Serializable
data class ViewAllProductResponse(
    val success: Boolean,
    val body: List<ViewAllProduct>?
)

@Serializable
data class ViewAllProduct(
    val id: Long?,
    val name: String?,
    val unit: String?,
    val unitValue: Double? = null,
    val image: ProductImage?,
    val inventory: Inventory?,
    val groupRange: GroupRange?,
    val isMRPRestricted: Boolean?
) {

    fun inventoryQty(): Int =
        if (RemoteConfigs.imsFeatureFlag())
            inventory?.dcImsQuantityAvailable?.toInt().getValueOrDefault()
        else inventory?.quantity ?: 0

    fun map(): ProductUiData {
        return ProductUiData(
            id = id.getValueOrDefault(),
            name = name.default(),
            description = EMPTY_STRING,
            imageUrl = image?.medium,
            productBundle = null,
            hasVariants = false,
            price = inventory?.listPrice.getValueOrDefault(),
            finalPrice = inventory?.finalPrice.getValueOrDefault(),
            discountedPrice = groupRange?.discountedPrice.getValueOrDefault(),
            inventoryQty = if (RemoteConfigs.imsFeatureFlag()) inventory?.dcImsQuantityAvailable?.toInt()
                .getValueOrDefault()
            else inventory?.quantity ?: 1,
            productCategory = null,
            purchasePrice = ZERO,
            images = null,
            isFavourite = false,
            isBackInStockSubscribed = false,
            isSponsored = false,
            slot = ZERO_INT,
            showMrp = isMRPRestricted == true || inventory?.listPrice.getValueOrDefault() <= groupRange?.discountedPrice.getValueOrDefault(),
            purchaseLimit = 0,
            itemQuantity = 0
        )
    }

    fun getQuantity(quantity: Int? = 1): Int {
        return quantity.getValueOrDefault(5)
    }
}

@Serializable
data class ViewAllProductArgs(
    val isPromotional: Boolean,
    val categoryId: Long? = null,
    val categoryName: String,
    val swimlaneId: Int? = null,
    val swimlaneType: String?,
    val isDiscount: Boolean
) : JavaSerializable




