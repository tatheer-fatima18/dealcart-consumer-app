package com.localpk.app.features.restaurant.data

import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotZero
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.ceil
import cafe.adriel.voyager.core.lifecycle.JavaSerializable

@Serializable
data class RestaurantDetail(
    val id: Long,
    val name: String?,
    val additionalDetails: AdditionalDetails? = null,
    val menus: List<RestaurantMenu>? = null
) {
    fun map(): RestaurantDetailUI {
        return RestaurantDetailUI(
            id = id,
            name = name.default(),
            additionalDetails = additionalDetails,
            menu = menus?.firstOrNull()?.map()
        )
    }
}

@Serializable
data class Timings(
    val day: String?,
    val openingTime: String?,
    val closingTime: String?,
    val isOpen: Boolean?
)


@Serializable
data class AdditionalDetails(
    val address: RestaurantAddress?,
    val timings: List<Timings>? = null
)

@Serializable
data class RestaurantAddress(
    val addressLine1: String?,
    val addressLine2: String?,
    val city: String?,
    val id: Long? = ZERO_LONG,
    val zipCode: Int?,
    val state: String?,
    val latitude: Double?,
    val longitude: Double?
) {
    fun completeAddress(): String {
        return "$addressLine1 $addressLine2"
    }
}

@Serializable
data class RestaurantMenu(
    val id: Long,
    val name: String?,
    val description: String?,
    val categories: List<MenuCategory>?
) {
    fun map(): RestaurantMenuUI {
        return RestaurantMenuUI(
            id = id,
            name = name,
            description = description,
            categories = categories?.map { it.map() }
        )
    }
}

@Serializable
data class MenuCategory(
    val id: Long,
    val name: String?,
    val description: String?,
    val items: List<CategoryItem>?
) {
    fun map(): MenuCategoryUI {
        return MenuCategoryUI(
            id = id,
            name = name,
            description = description,
            items = items?.map { it.map() }
        )
    }
}


@Serializable
data class CategoryItem(
    val id: Long,
    val name: String?,
    val description: String?,
    val image: Image? = null,
    val prices: ItemPrice?,
    val variations: List<ItemVariation>?,
    val choiceGroups: List<ItemChoiceGroup>?
) {
    fun map(): CategoryItemUIData {
        return CategoryItemUIData(
            id = id,
            name = name.default(),
            description = description.default(),
            image = image,
            prices = prices,
            variations = variations,
            choiceGroups = choiceGroups,
            itemQuantity = 0
        )
    }
}

@Serializable
data class CategoryItemUIData(
    val id: Long,
    val name: String?,
    val description: String?,
    val image: Image? = null,
    val prices: ItemPrice?,
    val variations: List<ItemVariation>?,
    val choiceGroups: List<ItemChoiceGroup>?,
    var itemQuantity: Int,
    @Transient
    var quantityListener: (quantity: Int) -> Unit = {}
) : JavaSerializable {
    fun formattedTotalAmount(): String = prices?.listPrice.getValueOrDefault().formatPKRCurrency()
    fun formattedDiscountedAmount(): String =
        prices?.discountPrice.getValueOrDefault().formatPKRCurrency()

    fun percentageOff(): String {
        return if ((((prices?.listPrice.getValueOrDefault() - prices?.discountPrice.getValueOrDefault()) / prices?.listPrice.getValueOrDefault()) * 100) <= 0)
            "1"
        else ceil(((prices?.listPrice.getValueOrDefault() - prices?.discountPrice.getValueOrDefault()) / prices?.listPrice.getValueOrDefault()) * 100).toInt()
            .toString() + "% OFF"
    }

    fun hasVariationsOrChoiceGroups(): Boolean {
        return variations.isNullOrEmpty().not() || choiceGroups.isNullOrEmpty().not()
    }
}

@Serializable
data class ItemPrice(
    val discountPrice: Double?,
    val listPrice: Double?,
) : JavaSerializable

@Serializable
data class ItemVariation(
    val id: Long,
    val name: String?,
    val price: Double? = null,
    val choiceGroup: ChoiceGroup? = null,
    val prices: ItemPrice? = null
) : JavaSerializable {
    fun formattedAmount(): String = price.getValueOrDefault().formatPKRCurrency()
    fun formattedVariationAmount(): String =
        prices?.discountPrice.getValueOrDefault().formatPKRCurrency()
}

@Serializable
data class ItemChoiceGroup(
    val id: Long,
    val name: String?,
    val isRequired: Boolean?,
    val maxSelectable: Int?,
    val minSelectable: Int?,
    val choices: List<ItemVariation>?
) : JavaSerializable {
    fun options(): String {
        return if (minSelectable.isNotZero()) "Select at least $minSelectable up to $maxSelectable" else "Select up to $maxSelectable options"
    }
}

@Serializable
data class RestaurantDetailUI(
    val id: Long,
    val name: String,
    val additionalDetails: AdditionalDetails? = null,
    val menu: RestaurantMenuUI? = null
)

@Serializable
data class ChoiceGroup(
    val id: Long,
) : JavaSerializable

@Serializable
data class RestaurantMenuUI(
    val id: Long,
    val name: String?,
    val description: String?,
    val categories: List<MenuCategoryUI>?
)

@Serializable
data class MenuCategoryUI(
    val id: Long,
    val name: String?,
    val description: String?,
    val items: List<CategoryItemUIData>?
)

@Serializable
data class CategoryItemUIDataAndRestaurantDetails(
    val id: Long,
    val name: String?,
    val description: String?,
    val image: Image? = null,
    val prices: ItemPrice?,
    val variations: List<ItemVariation>?,
    val choiceGroups: List<ItemChoiceGroup>?,
    var itemQuantity: Int,
    @Transient
    var quantityListener: (quantity: Int) -> Unit = {},
    val restaurantId: Long,
    val restaurantName: String,
    val logoImage: Image? = null,
    val cuisines: List<String>? = null,
    val deliveryFee: Long,
    val prepTime: String,
    val minimumOrderValue: Int
) : JavaSerializable {
    fun formattedTotalAmount(): String = prices?.listPrice.getValueOrDefault().formatPKRCurrency()
    fun formattedDiscountedAmount(): String =
        prices?.discountPrice.getValueOrDefault().formatPKRCurrency()

    fun percentageOff(): String {
        return if ((((prices?.listPrice.getValueOrDefault() - prices?.discountPrice.getValueOrDefault()) / prices?.listPrice.getValueOrDefault()) * 100) <= 0)
            "1"
        else ceil(((prices?.listPrice.getValueOrDefault() - prices?.discountPrice.getValueOrDefault()) / prices?.listPrice.getValueOrDefault()) * 100).toInt()
            .toString() + "% OFF"
    }

    fun hasVariationsOrChoiceGroups(): Boolean {
        return variations.isNullOrEmpty().not() || choiceGroups.isNullOrEmpty().not()
    }
}
