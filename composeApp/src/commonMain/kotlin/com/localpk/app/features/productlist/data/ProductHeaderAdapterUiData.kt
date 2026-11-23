package com.localpk.app.features.productlist.data

import com.localpk.app.features.banner.data.Banner
import com.localpk.app.features.category.data.CategoryUiModel
import kotlinx.serialization.Serializable

@Serializable
data class ProductHeaderAdapterUiData(
    override var type: Int,
    override var parentData: CategoryUiModel?,
    val parentPosition: Int?,
    override var childData: ProductUiData?,
    override var bannerData: Banner?
) : HeaderRecyclerViewParent<Banner, CategoryUiModel, ProductUiData>()

open class HeaderRecyclerViewParent<S, T, V> {
    open var type: Int = 1
    open var parentData: T? = null
    open var childData: V? = null
    open var bannerData: S? = null
}

object HeaderRecyclerAdapterConstants {
    const val HEADER = 0
    const val ITEM = 1
    const val BANNER = 2
}