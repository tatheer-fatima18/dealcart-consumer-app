package com.localpk.app.features.wallet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.CardPaymentDetails
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.cart.data.PaymentMethodParams
import com.localpk.app.common_components.WhiteBackgroundRoundedTextField
import com.localpk.app.theming.PURPLE_COLOR
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.capitalizeFirstLetter
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.name_
import dealcart.composeapp.generated.resources.name_example
import dealcart.composeapp.generated.resources.visaa
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun PaymentItem(
    paymentMethodItem: PaymentMethodParams,
    textFieldValue: MutableState<String>,
    paymentElement: Any?,
    cardPaymentDetails: CardPaymentDetails?,
    easyPaisaPaymentElement: Any?,
    onItemSelected: (PaymentMethodParams) -> Unit,
) {
    val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
    Column(
        modifier = Modifier.padding(vertical = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable {
                    onItemSelected(paymentMethodItem)
                    paymentMethodItem.select()
                }
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = paymentMethodItem.isSelected.value,
                onClick = {
                    onItemSelected(paymentMethodItem)
                    paymentMethodItem.select()
                },
                colors = RadioButtonDefaults.colors(selectedColor = PURPLE_COLOR),
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
                Text(
                    text = paymentMethodItem.name,
                    color = TextSubdued,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily()
                )

                if (paymentMethodItem.paymentType == PaymentMethod.JAZZ_CASH.method &&
                    sharedPreferencesManager.getJazzPhoneNumber().isNotEmpty()
                ) {
                    Text(
                        text = sharedPreferencesManager.getJazzPhoneNumber(),
                        color = TextSubdued,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PoppinsFontFamily()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(paymentMethodItem.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .width(38.dp)
                    .height(38.dp)
            )

            if (paymentMethodItem.paymentType == PaymentMethod.CARD.method) {
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(Res.drawable.visaa),
                    contentDescription = null,
                    modifier = Modifier
                        .width(38.dp)
                        .height(38.dp)
                )
            }
        }

        if (paymentMethodItem.isSelected.value) {
            when (paymentMethodItem.paymentType) {
                PaymentMethod.JAZZ_CASH.method -> {
                    // Jazz Cash implementation
                }

                PaymentMethod.CARD.method -> {
                    if (sharedPreferencesManager.getUserName()
                            .isEmpty() || cardPaymentDetails.isNotNull()
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.name_),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSubdued
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            WhiteBackgroundRoundedTextField(
                                modifier = Modifier
                                    .padding(top = 3.dp),
                                value = textFieldValue.value.capitalizeFirstLetter(),
                                prefix = { Text(text = EMPTY_STRING, color = Color.Black) },
                                placeholder = {
                                    Text(
                                        text = stringResource(Res.string.name_example),
                                        color = TextSubduedHalf,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp,
                                        fontFamily = PoppinsFontFamily()
                                    )
                                },
                                cursorColor = Color.Black,
                                readOnly = sharedPreferencesManager.getUserName().isNotBlank(),
                                onValueChange = {
                                    textFieldValue.value = it
                                },
                                keyboardOption = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text,
                                ),
                                cornerShape = RoundedCornerShape(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        if (cardPaymentDetails.isNotNull()) {
                            PaymentElementView(
                                paymentElement = paymentElement,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = TextSubdued
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = TextSubdued
                            )
                        }
                    }
                }

                PaymentMethod.EASYPAISA.method -> {
                    if (sharedPreferencesManager.getUserName()
                            .isEmpty() || cardPaymentDetails.isNotNull()
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.name_),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSubdued
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            WhiteBackgroundRoundedTextField(
                                modifier = Modifier
                                    .padding(top = 3.dp),
                                value = textFieldValue.value.capitalizeFirstLetter(),
                                prefix = { Text(text = EMPTY_STRING, color = Color.Black) },
                                placeholder = {
                                    Text(
                                        text = stringResource(Res.string.name_example),
                                        color = TextSubduedHalf,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp,
                                        fontFamily = PoppinsFontFamily()
                                    )
                                },
                                cursorColor = Color.Black,
                                readOnly = sharedPreferencesManager.getUserName().isNotBlank(),
                                onValueChange = {
                                    textFieldValue.value = it
                                },
                                keyboardOption = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Text,
                                ),
                                cornerShape = RoundedCornerShape(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        if (cardPaymentDetails.isNotNull()) {
                            EasyPaisaPaymentElementView(
                                paymentElement = easyPaisaPaymentElement,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = TextSubdued
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = TextSubdued
                            )
                        }
                    }
                }
            }
        }
    }
}