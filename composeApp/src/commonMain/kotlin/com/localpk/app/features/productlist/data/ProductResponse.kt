package com.localpk.app.features.productlist.data

import com.localpk.app.features.product_detail.presentation.ProductDetailScreen
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.utils.default
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.formatDateTime
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import kotlin.math.ceil

@Serializable
data class ProductResponse(val body: ProductList, val success: Boolean)

@Serializable
data class ProductList(var results: List<Product>?)

@Serializable
data class Product(
    val id: Long?,
    val name: String?,
    val description: String?,
    val unit: String?,
    val unitValue: Double? = null,
    val order: Long?,
    val productCategory: List<ProductCategory> = emptyList(),
    val images: List<ProductImage>?,
    val inventories: List<Inventory>?,
    val groupRanges: List<GroupRange>?,
    val groups: List<ProductGroup>? = null,
    var isFavourite: Boolean = false,
    val isSponsored: Boolean = false,
    val productBundle: List<ProductBundle> = emptyList(),
    val slot: Int? = ZERO_INT,
    val isMRPRestricted: Boolean = false,
    val attributes: List<Attribute>? = emptyList(),
    val variants: List<Variant>? = emptyList(),
    val tiers: List<Tiers>? = emptyList(),
    val isBackInStockSubscribed: Boolean? = false,
    val productPurchaseLimit: Int = ZERO_INT,
    val message: String = EMPTY_STRING,
    val storeName: String? = EMPTY_STRING,
    val storeImage: String = EMPTY_STRING,
    val deliveryDateRange: DeliveryDateRange? = null,
    val isProductToBeDeliveredByThirdParty: Boolean = false
) {

    fun inventoryQty(): Int =
        if (RemoteConfigs.imsFeatureFlag()) inventories?.sumOf {
            it.dcImsQuantityAvailable?.toInt().getValueOrDefault()
        } ?: 1
        else inventories?.sumOf { it.quantity } ?: 0

    fun map(): ProductUiData {
        return ProductUiData(
            id = id.getValueOrDefault(),
            name = name.default(),
            description = description ?: EMPTY_STRING,
            imageUrl = images?.firstOrNull()?.medium,
            productBundle = productBundle.firstOrNull(),
            hasVariants = productBundle.isNotEmpty(),
            price = if (RemoteConfigs.imsFeatureFlag()) inventories?.firstOrNull()?.dcImsMrp
                ?: 0.0 else inventories?.firstOrNull()?.listPrice ?: 0.0,
            finalPrice = inventories?.firstOrNull()?.finalPrice ?: 0.0,
            discountedPrice = groupRanges?.firstOrNull()?.discountedPrice
                ?: inventories?.firstOrNull()?.finalPrice ?: 0.0,
            inventoryQty = if (RemoteConfigs.imsFeatureFlag()) inventories?.sumOf {
                it.dcImsQuantityAvailable?.toInt().getValueOrDefault()
            } ?: 1
            else inventories?.sumOf { it.quantity } ?: 1,
            productCategory = productCategory,
            purchasePrice = ZERO,
            images = images?.orEmptyArrayList(),
            isFavourite = isFavourite ?: false,
            isBackInStockSubscribed = isBackInStockSubscribed ?: false,
            isSponsored = isSponsored ?: false,
            slot = slot ?: 999,
            showMrp = isMRPRestricted || (if (RemoteConfigs.imsFeatureFlag()) inventories?.firstOrNull()?.dcImsMrp
                ?: 0.0 else inventories?.firstOrNull()?.listPrice
                ?: 0.0) <= (groupRanges?.firstOrNull()?.discountedPrice
                ?: inventories?.firstOrNull()?.finalPrice ?: 0.0),
            purchaseLimit = if (productPurchaseLimit.getValueOrDefault() < inventoryQty()) {
                productPurchaseLimit.getValueOrDefault()
            } else {
                inventoryQty()
            },
            itemQuantity = 0,
            storeName = storeName.default(),
            deliveryDateRange = deliveryDateRange ?: DeliveryDateRange(ZERO_INT, ZERO_INT),
            isProductToBeDeliveredByThirdParty = isProductToBeDeliveredByThirdParty
        )
    }

    fun getQuantity(quantity: Int? = 1): Int {
        return quantity.getValueOrDefault(5)
    }

    fun mapDetail() =
        ProductDetailUiData(
            id = id.getValueOrDefault(),
            name = name.default(),
            unit = unit.default(),
            unitValue = unitValue.getValueOrDefault(),
            description = description.default(),
            shortDescription = if (description.default().length > 82) description.default()
                .substring(0, 82) else description.default(),
            imageUrl = images?.firstOrNull()?.medium ?: images?.firstOrNull()?.path,
            price = if (RemoteConfigs.imsFeatureFlag()) inventories?.firstOrNull()?.dcImsMrp
                ?: 0.0 else inventories?.firstOrNull()?.listPrice ?: 0.0,
            finalPrice = inventories?.firstOrNull()?.finalPrice ?: 0.0,
            groupEnabled = groupRanges?.isNotEmpty() == true && inventoryQty() > 0,
            groupPrice = groupRanges?.firstOrNull()?.discountedPrice
                ?: inventories?.firstOrNull()?.finalPrice ?: 0.0,
            groupId = groupRanges?.firstOrNull()?.id.getValueOrDefault(),
            groups = emptyList(),
            groupSize = groupRanges?.firstOrNull()?.quantity ?: 0,
            purchasePrice = ZERO,
            images = images?.orEmptyArrayList(),
            isFavourite = false,
            isBackInStockSubscribed = isBackInStockSubscribed ?: false,
            inventoryQty = inventoryQty(),
            attributes = attributes?.map { attribute -> attribute.toMapAttributeUiData() },
            variants = variants,
            selectedVariant = variants?.firstOrNull { it.productId == id } ?: Variant(
                0,
                0,
                arrayListOf()
            ),
            selectedVariantDetails = variants?.firstOrNull { it.productId == id }?.details
                ?: arrayListOf(),
            tiers = tiers,
            showMrp = isMRPRestricted.default() || (if (RemoteConfigs.imsFeatureFlag()) inventories?.firstOrNull()?.dcImsMrp
                ?: 0.0 else inventories?.firstOrNull()?.listPrice
                ?: 0.0) <= (groupRanges?.firstOrNull()?.discountedPrice
                ?: inventories?.firstOrNull()?.finalPrice ?: 0.0),
            message = message.default(),
            storeName = storeName.default(),
            deliveryDateRange = deliveryDateRange ?: DeliveryDateRange(ZERO_INT, ZERO_INT),
            storeImage = storeImage.default(),
            isProductToBeDeliveredByThirdParty = isProductToBeDeliveredByThirdParty
        )


    fun mapToProductHeaderAdapterUiData(): ProductHeaderAdapterUiData {
        return ProductHeaderAdapterUiData(
            type = HeaderRecyclerAdapterConstants.ITEM,
            parentData = null,
            parentPosition = null,
            childData = ProductUiData(
                id = id.getValueOrDefault(),
                name = name.default(),
                description = description ?: "",
                productBundle = productBundle.firstOrNull(),
                imageUrl = images?.firstOrNull()?.medium,
                price = if (RemoteConfigs.imsFeatureFlag()) inventories?.firstOrNull()?.dcImsMrp
                    ?: 0.0 else inventories?.firstOrNull()?.listPrice ?: 0.0,
                finalPrice = inventories?.firstOrNull()?.finalPrice ?: 0.0,
                discountedPrice = groupRanges?.firstOrNull()?.discountedPrice
                    ?: inventories?.firstOrNull()?.finalPrice ?: 0.0,
                inventoryQty = if (RemoteConfigs.imsFeatureFlag()) inventories?.sumOf {
                    it.dcImsQuantityAvailable?.toInt().getValueOrDefault()
                } ?: 0
                else inventories?.sumOf { it.quantity } ?: 0,
                productCategory = productCategory,
                purchasePrice = ZERO,
                images = images?.orEmptyArrayList(),
                isFavourite = isFavourite ?: false,
                isBackInStockSubscribed = isBackInStockSubscribed ?: false,
                isSponsored = isSponsored ?: false,
                slot = slot ?: 999,
                hasVariants = productBundle.isNotEmpty(),
                showMrp = isMRPRestricted || (if (RemoteConfigs.imsFeatureFlag()) inventories?.firstOrNull()?.dcImsMrp
                    ?: 0.0 else inventories?.firstOrNull()?.listPrice
                    ?: 0.0) <= (groupRanges?.firstOrNull()?.discountedPrice
                    ?: inventories?.firstOrNull()?.finalPrice ?: 0.0),
                purchaseLimit = if (productPurchaseLimit.getValueOrDefault() < inventoryQty()) {
                    productPurchaseLimit.getValueOrDefault()
                } else {
                    inventoryQty()
                },
                itemQuantity = 0,
                storeName = storeName.default()
            ),
            bannerData = null
        )
    }
}

@Serializable
data class ProductUiData(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Double,
    val finalPrice: Double,
    val purchasePrice: Double,
    val discountedPrice: Double,
    val inventoryQty: Int,
    val productCategory: List<ProductCategory>?,
    val isDeal: Boolean = false,
    val hasVariants: Boolean = false,
    val images: List<ProductImage>? = arrayListOf(),
    val productBundle: ProductBundle? = null,
    var isFavourite: Boolean,
    var isBackInStockSubscribed: Boolean,
    var isSponsored: Boolean,
    var slot: Int,
    val isFromNotification: Boolean = false,
    val isFlashDeal: Boolean = false,
    val flashDealId: Long = ZERO_LONG,
    val isDeepLink: Boolean = false,
    var itemQuantity: Int,
    var hasVariant: Boolean? = false,
    val showMrp: Boolean?,
    val purchaseLimit: Int?,
    val storeName: String = EMPTY_STRING,
    val storeImage: String = EMPTY_STRING,
    val deliveryDateRange: DeliveryDateRange = DeliveryDateRange(ZERO_INT, ZERO_INT),
    val isProductToBeDeliveredByThirdParty: Boolean = false
) {

    val itemQuantityValue: Int
        get() = itemQuantity

    fun formattedPrice(): String = price.formatPKRCurrency()

    fun productDisplayName(): String =
        if (!hasVariants) name else productBundle?.bundle?.displayName.default()

    fun productDisplayImage(): String =
        if (!hasVariants) imageUrl.default() else productBundle?.bundle?.displayImageUrl().default()

    fun formattedDiscountedPrice(): String = discountedPrice.formatPKRCurrency()

    fun toProductDetailUiData() = ProductDetailUiData(
        id = id,
        name = name,
        description = description,
        shortDescription = if (description.default().length > 82) description.default()
            .substring(0, 82) else description,
        imageUrl = imageUrl,
        price = price,
        finalPrice = finalPrice,
        groupEnabled = discountedPrice > 0 && inventoryQty > 0,
        groupPrice = discountedPrice,
        groupId = 0,
        groups = arrayListOf(),
        groupSize = 0,
        purchasePrice = purchasePrice,
        images = if (images.isNullOrEmpty()) {
            arrayListOf(defaultProductImage)
        } else {
            images
        },
        isFavourite = isFavourite,
        isBackInStockSubscribed = isBackInStockSubscribed,
        inventoryQty = inventoryQty,
        flashDealId = flashDealId,
        showMrp = !showMrp.default() || price <= discountedPrice,
        storeName = storeName,
        deliveryDateRange = deliveryDateRange,
        storeImage = storeImage,
        isProductToBeDeliveredByThirdParty = isProductToBeDeliveredByThirdParty
    )

    fun percentageOff(): String {

        val formattedValue = (price - discountedPrice).formatPKRCurrency()

        return if ((((price - discountedPrice) / price) * 100) <= 0)
            "1" // this is by design, as recommended by product team.
        else if (RemoteConfigs.isDiscountTypeAbsolute()) "$formattedValue OFF"
        else ceil((((price - discountedPrice) / price) * 100)).toInt().toString() + "% OFF"
    }

    fun formattedValue(): String {
        val formattedValue = (price - discountedPrice).formatPKRCurrency()
        return "$formattedValue OFF"
    }
}


fun getProduct(id: Long): ProductUiData {
    return ProductUiData(
        id = id,
        name = EMPTY_STRING,
        description = EMPTY_STRING,
        imageUrl = EMPTY_STRING,
        price = ZERO,
        finalPrice = ZERO,
        discountedPrice = ZERO,
        inventoryQty = 0,
        productCategory = null,
        purchasePrice = ZERO,
        isFavourite = false,
        isBackInStockSubscribed = false,
        isSponsored = false,
        slot = 0,
        showMrp = false,
        purchaseLimit = 0,
        itemQuantity = 0,
        storeName = EMPTY_STRING
    )
}

fun productUiData(id: Long) = ProductUiData(
    id = id,
    name = EMPTY_STRING,
    description = EMPTY_STRING,
    imageUrl = EMPTY_STRING,
    price = ZERO,
    finalPrice = ZERO,
    discountedPrice = ZERO,
    inventoryQty = 0,
    productCategory = null,
    purchasePrice = ZERO,
    isDeal = true,
    isFavourite = false,
    isBackInStockSubscribed = false,
    isSponsored = false,
    slot = 0,
    showMrp = false,
    purchaseLimit = 0,
    itemQuantity = 0,
    storeName = EMPTY_STRING
)


@Serializable
data class ProductDetailUiData(
    val id: Long,
    val name: String,
    val description: String,
    val shortDescription: String,
    val imageUrl: String?,
    val price: Double,
    val finalPrice: Double,
    val groupEnabled: Boolean,
    val groupPrice: Double,
    val groupId: Long,
    val groups: List<GroupUiData>,
    val groupSize: Int,
    val attributes: List<AttributeUiData>? = arrayListOf(),
    val variants: List<Variant>? = arrayListOf(),
    val selectedVariantDetails: List<VariantDetail> = listOf(),
    var selectedVariant: Variant = Variant(0, 0, arrayListOf()),
    val tiers: List<Tiers>? = arrayListOf(),
    val purchasePrice: Double,
    val images: List<ProductImage>? = arrayListOf(),
    var isFavourite: Boolean,
    var isBackInStockSubscribed: Boolean,
    val unitValue: Double? = null,
    val unit: String = EMPTY_STRING,
    val inventoryQty: Int,
    val isFlashDeal: Boolean = false,
    val discountedPercentage: Double = ZERO,
    val totalPersonsJoined: Int = ZERO_INT,
    val totalPersonsRequired: Int = ZERO_INT,
    val timeLeft: Long = ZERO_LONG,
    val remainingPersons: Int = ZERO_INT,
    val dealPresence: String = EMPTY_STRING,
    val isDealNone: Boolean = false,
    val maxPurchasableQuantity: Int = ZERO_INT,
    val flashDealId: Long = ZERO_LONG,
    val expiryEndTime: Long = ZERO_LONG,
    val showMrp: Boolean = false,
    var quantity: Int = 1,
    val message: String = EMPTY_STRING,
    val storeName: String,
    val storeImage: String,
    val deliveryDateRange: DeliveryDateRange = DeliveryDateRange(ZERO_INT, ZERO_INT),
    val isProductToBeDeliveredByThirdParty: Boolean = false
) {
    fun formattedPrice(totalQuantity: Int? = 1): String =
        (price * totalQuantity.getValueOrDefault(1)).formatPKRCurrency()

    fun showShortDescription(): Boolean = description.default().length > 82
    fun formattedFinalPrice(): String = finalPrice.formatPKRCurrency()
    fun formattedGroupPrice(
        totalQuantity: Int? = 1
    ): String {
        return if (tiers?.size.getValueOrDefault() > 1) {
            val matchingTier = tiers.orEmptyArrayList()
                .find {
                    totalQuantity.getValueOrDefault() in it.lowerLimit..it.upperLimit.getValueOrDefault(
                        100000
                    )
                }
            val price = matchingTier?.price.getValueOrDefault()
            (price * totalQuantity.getValueOrDefault(1)).formatPKRCurrency()
        } else {
            (groupPrice * totalQuantity.getValueOrDefault(1)).formatPKRCurrency()
        }
    }

    fun updateTiers(quantity: Int?): List<Tiers> {
        tiers?.map { item ->
            item.isActive = quantity.getValueOrDefault(1) in item.lowerLimit..(item.upperLimit
                ?: Long.MAX_VALUE)
        }
        return tiers.orEmptyArrayList()
    }

    fun percentageOff(totalQuantity: Int? = 1): String {
        val newGroupPrice = if (tiers?.size.getValueOrDefault() > 1) {
            val matchingTier = tiers.orEmptyArrayList()
                .find {
                    totalQuantity.getValueOrDefault() in it.lowerLimit..it.upperLimit.getValueOrDefault(
                        100000
                    )
                }
            matchingTier?.price.getValueOrDefault()
        } else {
            groupPrice
        }
        return if (((((price * totalQuantity.getValueOrDefault(1)) - (newGroupPrice * totalQuantity.getValueOrDefault(
                1
            ))) / (price * totalQuantity.getValueOrDefault(1))) * 100) <= 0
        )
            "1" // this is by design, as recommended by product team.
        else if (RemoteConfigs.isDiscountTypeAbsolute()) (
                ((price * totalQuantity.getValueOrDefault(
                    1
                )) - (newGroupPrice * totalQuantity.getValueOrDefault(1)))
                ).formatPKRCurrency()
        else ceil(
            ((((price * totalQuantity.getValueOrDefault(1)) - (newGroupPrice * totalQuantity.getValueOrDefault(
                1
            ))) / (price * totalQuantity.getValueOrDefault(1))) * 100)
        ).toInt()
            .toString() + "% OFF"
    }

    fun isDiscountTypeAbsolute(): Boolean {
        return RemoteConfigs.isDiscountTypeAbsolute()
    }
}

fun ProductDetailUiData.toProductUiData(): ProductUiData {
    val hasVariantOptions = attributes.isNullOrEmpty().not()
    return ProductUiData(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        price = price,
        finalPrice = finalPrice,
        purchasePrice = purchasePrice,
        discountedPrice = groupPrice,
        inventoryQty = inventoryQty,
        productCategory = null,
        isDeal = groupEnabled,
        hasVariants = hasVariantOptions,
        images = images,
        productBundle = null,
        isFavourite = isFavourite,
        isBackInStockSubscribed = isBackInStockSubscribed,
        isSponsored = false,
        slot = 0,
        isFromNotification = false,
        isFlashDeal = isFlashDeal,
        flashDealId = flashDealId,
        isDeepLink = false,
        itemQuantity = quantity,
        hasVariant = hasVariantOptions,
        showMrp = showMrp,
        purchaseLimit = maxPurchasableQuantity,
        storeName = storeName,
        storeImage = storeImage,
        deliveryDateRange = deliveryDateRange,
        isProductToBeDeliveredByThirdParty = isProductToBeDeliveredByThirdParty
    )
}

@Serializable
data class RecommendationUIData(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Double,
    val finalPrice: Double,
    val groupEnabled: Boolean,
    val inventoryQty: Int,
    val discountedPrice: Double,
    val isFavourite: Boolean,
    val groupPrice: Double,
    val groupId: Long,
    val groups: List<GroupUiData>,
    val groupSize: Int,
    val purchasePrice: Double,
    val images: List<ProductImage>? = arrayListOf(),
    val showMrp: Boolean?
) {
    fun percentageOff(): String {
        return if ((((price - groupPrice) / price) * 100) <= 0)
            "1" // this is by design, as recommended by product team.
        else ceil((((price - groupPrice) / price) * 100)).toInt().toString()
    }

}

/**
 * Custom serializer for ProductImage that handles cases where image fields
 * can be either strings (URLs) or error objects.
 */
object ProductImageSerializer : KSerializer<ProductImage> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ProductImage") {
        element<String?>("full", isOptional = true)
        element<String?>("path", isOptional = true)
        element<String?>("thumbnail", isOptional = true)
        element<String?>("medium", isOptional = true)
    }

    override fun deserialize(decoder: Decoder): ProductImage {
        require(decoder is JsonDecoder)
        val jsonElement = decoder.decodeJsonElement()
        val jsonObject = jsonElement.jsonObject

        fun extractString(key: String): String? {
            val element = jsonObject[key] ?: return null
            return when {
                element is JsonPrimitive && element.isString -> element.content
                element is JsonObject -> null // Error object, return null
                else -> null
            }
        }

        return ProductImage(
            full = extractString("full"),
            path = extractString("path"),
            thumbnail = extractString("thumbnail"),
            medium = extractString("medium")
        )
    }

    override fun serialize(encoder: Encoder, value: ProductImage) {
        require(encoder is JsonEncoder)
        val jsonObject = buildJsonObject {
            value.full?.let { put("full", it) }
            value.path?.let { put("path", it) }
            value.thumbnail?.let { put("thumbnail", it) }
            value.medium?.let { put("medium", it) }
        }
        encoder.encodeJsonElement(jsonObject)
    }
}

@Serializable(with = ProductImageSerializer::class)
data class ProductImage(
    val full: String? = null,
    val path: String? = null,
    val thumbnail: String? = null,
    val medium: String? = null
)

@Serializable
data class Inventory(
    val listPrice: Double,
    val finalPrice: Double? = null,
    val quantity: Int,
    val dcImsMrp: Double? = null,
    val dcImsQuantityAvailable: Double?
)

@Serializable
data class GroupRange(val id: Long? = null, val discountedPrice: Double, val quantity: Int? = null)

@Serializable
data class ProductGroup(
    val id: Long,
    val quantity: Int,
    val purchaseCount: Int,
    val discountedPrice: Double,
    val expiryEndTime: Long,
    val remainingTime: Long,
    val customer: GroupCustomer?,
) {

    fun map(userId: Long): GroupUiData = GroupUiData(
        id = id,
        title = "" + customer?.firstName.default() + " ka group",
        price = discountedPrice,
        addedMember = purchaseCount,
        requiredMember = quantity - purchaseCount,
        totalMemberCapacity = quantity,
        createdAt = remainingTime.formatDateTime(),
        remainingTime = remainingTime,
        isMyGroup = customer?.id == userId,
    )
}

@Serializable
data class GroupCustomer(
    val id: Long,
    val firstName: String,
    val lastName: String,
)

@Serializable
data class GroupUiData(
    val id: Long,
    val remainingTime: Long,
    val title: String,
    val price: Double,
    val addedMember: Int,
    val requiredMember: Int,
    val totalMemberCapacity: Int,
    var createdAt: String,
    val isMyGroup: Boolean = false,
    var name: String = EMPTY_STRING,
    var image: String = EMPTY_STRING,
    var weight: String = EMPTY_STRING
)

@Serializable
data class DeliveryDateRange(
    val deliveryEndRange: Int,
    val deliveryStartRange: Int,
)

data class ProductPagedList(
    val page: Int,
    val items: List<ProductUiData>,
)

@Serializable
data class ProductCategory(
    val id: Long,
    val categoryId: Long,
    val isPreferred: Boolean,
    val preferredOrder: Int?
)

@Serializable
data class ProductBundle(
    val id: Long,
    val priority: Long,
    val bundle: Bundle?
)

@Serializable
data class Bundle(
    val id: Long,
    val displayName: String,
    val displayImage: ProductImage?,
) {
    fun displayImageUrl(): String = displayImage?.medium.default()
}

@Serializable
data class AttributeOption(
    val id: Long,
    val optionName: String,
    val showImage: Boolean,
    val imageUrl: String?,
) {
    fun toMapAttributeOptionUiData(): AttributeOptionUiData = AttributeOptionUiData(
        id = id,
        optionName = optionName,
        showImage = showImage,
        imageUrl = imageUrl.default()
    )
}

@Serializable
data class AttributeOptionUiData(
    val id: Long,
    val optionName: String,
    val showImage: Boolean,
    val imageUrl: String,
    var isSelected: Boolean = false,
    var isDisabled: Boolean = false
)

@Serializable
data class Attribute(
    val id: Long,
    val name: String,
    val options: List<AttributeOption>,
) {
    fun toMapAttributeUiData(): AttributeUiData = AttributeUiData(
        id = id,
        name = name,
        options = options.map { it.toMapAttributeOptionUiData() }
    )
}

@Serializable
data class AttributeUiData(
    val id: Long,
    val name: String,
    val options: List<AttributeOptionUiData>
)

@Serializable
data class VariantDetail(
    var attributeId: Long,
    var optionId: Long
)

@Serializable
data class Variant(
    val productId: Long,
    val quantity: Int,
    val details: List<VariantDetail>
)

@Serializable
data class Tiers(
    val id: Long? = ZERO_LONG,
    val lowerLimit: Long,
    val upperLimit: Long?,
    val price: Double?,
    var isActive: Boolean? = false
) {
    fun formattedTierPrice(): String = price?.formatPKRCurrency() ?: EMPTY_STRING
}

enum class SwimLaneType(val status: String) {
    CART("cart"),
    NEWLY_ADDED("newly-added"),
}

val defaultProductImage = ProductImage(
    full = EMPTY_STRING,
    medium = EMPTY_STRING,
    path = EMPTY_STRING,
    thumbnail = EMPTY_STRING
)

enum class StoreType(val storeName: String) {
    DEAL_CART("DealCart")
}

fun ProductUiData.toProductDetailScreen(): ProductDetailScreen {
    return ProductDetailScreen(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        price = price,
        finalPrice = finalPrice,
        purchasePrice = purchasePrice,
        discountedPrice = discountedPrice,
        inventoryQty = inventoryQty,
        isSponsored = isSponsored,
        slot = slot,
        isFromNotification = isFromNotification,
        isFlashDeal = isFlashDeal,
        flashDealId = flashDealId,
        isDeepLink = isDeepLink,
        itemQuantity = itemQuantity,
        hasVariant = hasVariant,
        showMrp = showMrp,
        purchaseLimit = purchaseLimit
    )
}
