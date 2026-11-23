package com.localpk.app.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.CustomAlertDialog
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.InputBoxStrokeColor
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.isNotZero
import com.localpk.app.utils.isZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.changing_location
import dealcart.composeapp.generated.resources.edit_address
import dealcart.composeapp.generated.resources.location_pin
import dealcart.composeapp.generated.resources.not_now
import dealcart.composeapp.generated.resources.updating_address
import dealcart.composeapp.generated.resources.updating_location
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddressCard(
    model: AddressUiData,
    isAddressSelected: Boolean,
    onEditClick: () -> Unit,
    onConfirm: (AddressUiData) -> Unit,
    cart: Int,
    foodCart: Int,
    defaultAddressId: Long,
    onAddressChange: (AddressUiData) -> Unit
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
            onAddressChange(model)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                when {
                    cart.isNotZero() -> {
                        onConfirm(model)
                    }

                    foodCart.isNotZero() -> {
                        if (model.id != defaultAddressId) {
                            shouldShowAddressChangeDialog.value = true
                        }
                    }

                    cart.isZero() && foodCart.isZero() -> {
                        onConfirm(model)
                    }
                }
            }
            .padding(vertical = 5.dp)
            .padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isAddressSelected,
            onClick = {
                when {
                    cart.isNotZero() -> {
                        onConfirm(model)
                    }

                    foodCart.isNotZero() -> {
                        if (model.id != defaultAddressId) {
                            shouldShowAddressChangeDialog.value = true
                        }
                    }

                    cart.isZero() && foodCart.isZero() -> {
                        onConfirm(model)
                    }
                }
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = ColorPrimary,
                unselectedColor = ColorPrimary
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .height(20.dp)
                .width(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Column {
                Text(
                    modifier = Modifier.width(280.dp),
                    text = model.address,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = ColorPrimaryText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PoppinsFontFamily(),
                    ),
                )
                Text(
                    modifier = Modifier,
                    text = model.city,
                    style = TextStyle(
                        color = ColorPrimaryText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PoppinsFontFamily(),
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.weight(.1f))

        Image(
            painter = painterResource(Res.drawable.edit_address),
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    onEditClick()
                })
    }
    HorizontalDivider(
        modifier = Modifier
            .padding(start = 2.dp, end = 5.dp)
            .fillMaxWidth()
            .height(1.dp),
        color = InputBoxStrokeColor
    )
}