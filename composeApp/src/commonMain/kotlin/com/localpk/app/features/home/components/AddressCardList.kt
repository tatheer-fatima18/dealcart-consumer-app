package com.localpk.app.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.features.address.data.AddressUiData

@Composable
fun AddressCardList(
    addressList: List<AddressUiData>,
    selectedAddressId: Long,
    onEdit: (AddressUiData) -> Unit,
    onConfirm: (AddressUiData) -> Unit,
    cart: Int,
    foodCart: Int,
    defaultAddressId: Long,
    onAddressChange: (AddressUiData) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(addressList) { address ->
            AddressCard(
                model = address,
                isAddressSelected = selectedAddressId == address.id,
                onEditClick = {
                    onEdit(address)
                },
                onConfirm = { onConfirm(address) },
                cart = cart,
                foodCart = foodCart,
                defaultAddressId = defaultAddressId,
                onAddressChange = { onAddressChange(address) }
            )
        }
    }
}