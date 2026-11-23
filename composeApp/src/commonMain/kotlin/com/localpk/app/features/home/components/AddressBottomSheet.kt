package com.localpk.app.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.base.getScreenHeightDp
import com.localpk.app.common_components.CustomAlertDialog
import com.localpk.app.common_components.NewAddressButton
import com.localpk.app.features.address.data.AddressUiData
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.changing_location
import dealcart.composeapp.generated.resources.location_pin
import dealcart.composeapp.generated.resources.not_now
import dealcart.composeapp.generated.resources.updating_address
import dealcart.composeapp.generated.resources.updating_location
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBottomSheet(
    onDismiss: () -> Unit,
    addresses: List<AddressUiData>,
    areAddressLoaded: Boolean = false,
    onClearFoodCart: () -> Unit,
    onConfirm: (AddressUiData) -> Unit,
    defaultAddressId: Long,
    onAddressChange: (AddressUiData) -> Unit,
    onEdit: (AddressUiData) -> Unit,
    cart: Int,
    foodCart: Int,
    newAddressClick: () -> Unit
) {
    val maxSheetHeight = getScreenHeightDp().dp * 0.5f
    val shouldShowAddressChangeDialog = remember { mutableStateOf(false) }
    if (shouldShowAddressChangeDialog.value) {
        CustomAlertDialog(
            isAddressDialog = false,
            shouldShowDialog = shouldShowAddressChangeDialog,
            icon = Res.drawable.location_pin,
            title = stringResource(Res.string.updating_location),
            message = stringResource(Res.string.changing_location),
            confirmButton = stringResource(Res.string.updating_address),
            cancelButton = stringResource(Res.string.not_now)
        ) {
            onClearFoodCart()
        }
    }
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        dragHandle = null,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.heightIn(max = maxSheetHeight)) {
                if (areAddressLoaded)
                    AddressCardList(
                        addressList = addresses,
                        selectedAddressId = defaultAddressId,
                        onEdit = { address ->
                            onEdit(address)
                            onDismiss()
                        },
                        onConfirm = { model ->
                            onConfirm(model)
                        },
                        cart = cart,
                        foodCart = foodCart,
                        defaultAddressId = defaultAddressId,
                        onAddressChange = { model ->
                            onAddressChange(model)
                        }
                    )
                else
                    AddressCardListShimmer()
            }
            NewAddressButton(
                onClick = {
                    onDismiss()
                    newAddressClick()
                }
            )
        }
    }
}