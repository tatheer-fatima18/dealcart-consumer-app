package com.localpk.app.features.address.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    onDeleteClick: (id: Long) -> Unit,
    onEditClick: (address: AddressUiData) -> Unit,
    onConfirmAddress: (AddressUiData) -> Unit,
    onAddressClick: (AddressUiData) -> Unit,
    cart: Int,
    foodCart: Int
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(addressList) { address ->
            AddressCard(
                model = address,
                isAddressSelected = selectedAddressId == address.id,
                onEditClick = {
                    onEditClick(address)
                },
                onDeleteClick = {
                    onDeleteClick(address.id)
                },
                onConfirmAddress = { onConfirmAddress(address) },
                onAddressClick = { onAddressClick(address) },
                cart, foodCart, selectedAddressId
            )
        }
    }
}