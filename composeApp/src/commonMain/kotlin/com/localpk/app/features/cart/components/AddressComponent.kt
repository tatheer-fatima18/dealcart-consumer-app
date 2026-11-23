package com.localpk.app.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.StaticMap
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.features.address.data.AddressUiData
import com.localpk.app.features.user_location.data.Geolocation
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.add_second_phone
import dealcart.composeapp.generated.resources.cart_address
import dealcart.composeapp.generated.resources.default_label
import dealcart.composeapp.generated.resources.edit_address
import dealcart.composeapp.generated.resources.plus
import dealcart.composeapp.generated.resources.second_phone
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddressComponent(
    selectedAddressUiData: AddressUiData?,
    defaultPhoneNumber: String,
    secondaryPhoneNumber: String,
    onEditAddressClick: () -> Unit,
    onEditPhoneNumberClick: () -> Unit,
    onAddPhoneNumberClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(
                color = Color.White,
                RoundedCornerShape(8.dp)
            )
            .padding(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RadioButton(
                selected = true,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = ButtonSurfaceBrand,
                )
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = selectedAddressUiData?.city ?: EMPTY_STRING,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextSubdued,
                    )
                )
                Text(
                    text = "${selectedAddressUiData?.address.default()}\n${selectedAddressUiData?.addressDescription.default()}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        color = TextSubdued
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = {
                    onEditAddressClick()
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(Res.drawable.edit_address),
                    contentDescription = null,
                    tint = TextSubdued
                )
            }
        }
        selectedAddressUiData?.let {
            val initialLat = it.lat
            val initialLng = it.lng

            val initialPosition = Geolocation(initialLat, initialLng)
            StaticMap(
                modifier = Modifier
                    .fillMaxSize()
                    .height(88.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                location = initialPosition
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = SurfaceBackground,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Default Phone: ")
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                            )
                        ) {
                            append(defaultPhoneNumber)
                        }
                    },
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubdued,
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(Res.string.default_label),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = TextSubdued,
                    )
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                thickness = 1.dp,
                color = BorderPrimaryDefault
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        if (selectedAddressUiData?.secondaryPhoneNumber?.isNotEmpty() == true) {
                            withStyle(
                                SpanStyle(
                                    color = TextSubdued
                                )
                            ) {
                                append(stringResource(Res.string.second_phone))
                            }
                            append(" ")
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSubdued
                                )
                            ) {
                                append(secondaryPhoneNumber)
                            }
                        } else {
                            withStyle(
                                SpanStyle(
                                    color = ButtonSurfaceBrand
                                )
                            ) {
                                append(stringResource(Res.string.add_second_phone))
                            }
                        }
                    },
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        if (selectedAddressUiData?.secondaryPhoneNumber?.isNotEmpty() == true) {
                            onEditPhoneNumberClick()
                        } else {
                            onAddPhoneNumberClick()
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(if (selectedAddressUiData?.secondaryPhoneNumber?.isNotEmpty() == true) Res.drawable.edit_address else Res.drawable.plus),
                        contentDescription = null,
                        tint = if (selectedAddressUiData?.secondaryPhoneNumber?.isNotEmpty() == true) TextSubdued else ButtonSurfaceBrand
                    )
                }
            }
        }
    }
}