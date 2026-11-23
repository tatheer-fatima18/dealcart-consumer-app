package com.localpk.app.features.address.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.features.address.presentation.MapScreen
import com.localpk.app.features.address.presentation.AddAddressBottomSheetScreen
import com.localpk.app.features.address.presentation.AddPhoneNumberBottomSheetScreen
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class CustomerAddressesResponse(
    val body: List<AddressWrapper>,
    val success: Boolean?,
) : JavaSerializable

@Serializable
data class AddressWrapper(
    val address: Address,
    val id: Long,
) : JavaSerializable {
    fun map(): AddressUiData {
        return AddressUiData(
            id = address.id,
            defaultAddress =
            if (address.addressLine1.default().isNotEmpty() && address.addressLine2.default()
                    .isNotEmpty()
            )
                address.addressLine1 + ", " + address.addressLine2
            else if (address.addressLine1.isNullOrBlank().not())
                address.addressLine1.default()
            else address.addressLine2.default("Address: " + address.id),
            addressDescription = address.addressLine2.default(),
            address = address.concatenatedAddress.default()
                .ifEmpty { address.addressLine2.default() },
            lat = address.lat.getValueOrDefault(),
            lng = address.lng.getValueOrDefault(),
            city = address.city.default(),
            area = address.area.default(),
            additionalAddressDetails = address.additionalAddressDetails,
            addressType = address.addressType.default(),
            nearestLandmark = address.nearestLandmark.default(),
            streetName = address.streetName.default(),
            isOrderReturned = address.isOrderReturned ?: false,
            secondaryPhoneNumber = if (!address.phoneNumber.isNullOrEmpty() && address.phoneNumber.startsWith(
                    "0"
                )
            ) {
                address.phoneNumber.substring(1)
            } else {
                address.phoneNumber
            },
            isSecondaryContactNeeded = address.isSecondaryContactNeeded ?: false
        )
    }
}

@Serializable
data class Address(
    val id: Long,
    val addressLine1: String?,
    val addressLine2: String?,
    val city: String?,
    val country: String?,
    val createdAt: String?,
    var isDefault: Boolean,
    val postalCode: Int?,
    val state: String?,
    val status: Boolean?,
    val updatedAt: String?,
    val lat: Double?,
    val lng: Double?,
    val area: String?,
    val addressType: String?,
    val additionalAddressDetails: AdditionalAddressDetails?,
    val nearestLandmark: String?,
    val concatenatedAddress: String?,
    val streetName: String?,
    val isOrderReturned: Boolean?,
    val phoneNumber: String?,
    val isSecondaryContactNeeded: Boolean?
) : JavaSerializable

@Serializable
data class AddressUiData(
    val id: Long,
    val defaultAddress: String,
    val address: String,
    val addressDescription: String,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    var selected: Boolean = false,
    var selectedPosition: Int = -1,
    var city: String,
    val area: String,
    val addressType: String,
    val additionalAddressDetails: AdditionalAddressDetails?,
    val nearestLandmark: String?,
    val streetName: String,
    val navigationFrom: String? = EMPTY_STRING,
    val isOrderReturned: Boolean,
    val secondaryPhoneNumber: String?,
    val isSecondaryContactNeeded: Boolean
) : JavaSerializable {
    @Transient
    var listener: () -> Unit = { }
}


fun AddressUiData.toMapScreen(): MapScreen {
    return MapScreen(
        id = id,
        defaultAddress = defaultAddress,
        address = address,
        addressDescription = addressDescription,
        lat = lat,
        lng = lng,
        selected = selected,
        selectedPosition = selectedPosition,
        city = city,
        area = area,
        addressType = addressType,
        flatNo = additionalAddressDetails?.flatNo,
        floorNo = additionalAddressDetails?.floorNo,
        buildingName = additionalAddressDetails?.buildingName,
        houseNumber = additionalAddressDetails?.houseNumber,
        shopName = additionalAddressDetails?.shopName,
        shopNumber = additionalAddressDetails?.shopNumber,
        nearestLandmark = nearestLandmark ?: EMPTY_STRING,
        streetName = streetName,
        navigationFrom = navigationFrom ?: EMPTY_STRING,
        isOrderReturned = isOrderReturned,
        secondaryPhoneNumber = secondaryPhoneNumber,
        isSecondaryContactNeeded = isSecondaryContactNeeded
    )
}

fun AddressUiData.toAddAddressBottomSheetScreen(warehouseId: Long): AddAddressBottomSheetScreen {
    return AddAddressBottomSheetScreen(
        id = id,
        address = address,
        lng = lng,
        lat = lat,
        street = streetName,
        city = city,
        area = area,
        addressType = addressType,
        navigationFrom = navigationFrom ?: EMPTY_STRING,
        nearestLandmark = nearestLandmark ?: EMPTY_STRING,
        phoneNumber = secondaryPhoneNumber,
        flatNo = additionalAddressDetails?.flatNo,
        floorNo = additionalAddressDetails?.floorNo,
        buildingName = additionalAddressDetails?.buildingName,
        houseNumber = additionalAddressDetails?.houseNumber,
        shopName = additionalAddressDetails?.shopName,
        shopNumber = additionalAddressDetails?.shopNumber,
        warehouseId = warehouseId
    )
}

fun AddressUiData.toAddPhoneNumberBottomSheetScreen(): AddPhoneNumberBottomSheetScreen {
    return AddPhoneNumberBottomSheetScreen(
        lng = lng,
        lat = lat,
        city = city,
        addressType = addressType,
        country = "Pakistan",
        isDefault = true,
        status = true,
        id = id,
        area = area,
        streetName = streetName,
        nearestLandmark = nearestLandmark ?: EMPTY_STRING,
        phoneNumber = secondaryPhoneNumber,
        navigationFrom = navigationFrom,
        flatNo = additionalAddressDetails?.flatNo,
        floorNo = additionalAddressDetails?.floorNo,
        buildingName = additionalAddressDetails?.buildingName,
        houseNumber = additionalAddressDetails?.houseNumber,
        shopName = additionalAddressDetails?.shopName,
        shopNumber = additionalAddressDetails?.shopNumber
    )
}


