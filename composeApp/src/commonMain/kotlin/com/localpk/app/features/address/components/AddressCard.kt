package com.localpk.app.features.address.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.CustomAlertDialog
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.utils.isNotZero
import com.localpk.app.utils.isZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.changing_location
import dealcart.composeapp.generated.resources.ic_address
import dealcart.composeapp.generated.resources.ic_address_grey
import dealcart.composeapp.generated.resources.ic_delete_address
import dealcart.composeapp.generated.resources.ic_edit_address
import dealcart.composeapp.generated.resources.location_pin
import dealcart.composeapp.generated.resources.not_now
import dealcart.composeapp.generated.resources.updating_address
import dealcart.composeapp.generated.resources.updating_location
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddressCard(
    model: AddressUiData,
    isAddressSelected: Boolean = false,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmAddress: () -> Unit,
    onAddressClick: () -> Unit,
    cart: Int,
    foodCart: Int,
    defaultAddressId: Long
) {
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
            onAddressClick()
        }
    }
    Card(
        modifier = Modifier
            .padding(top = 2.dp, bottom = 2.dp)
            .clickable {
                when {
                    cart.isNotZero() -> {
                        onConfirmAddress()
                    }

                    foodCart.isNotZero() -> {
                        if (model.id != defaultAddressId) {
                            shouldShowAddressChangeDialog.value = true
                        }
                    }

                    cart.isZero() && foodCart.isZero() -> {
                        onConfirmAddress()
                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(if (isAddressSelected) Res.drawable.ic_address else Res.drawable.ic_address_grey),
                contentDescription = null, modifier = Modifier.height(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f)
            ) {
                Text(
                    modifier = Modifier,
                    text = model.address,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier.clickable(onClick = onEditClick),
                    painter = painterResource(Res.drawable.ic_edit_address),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier.clickable(onClick = onDeleteClick),
                    painter = painterResource(Res.drawable.ic_delete_address),
                    contentDescription = null
                )
            }
        }
    }
}