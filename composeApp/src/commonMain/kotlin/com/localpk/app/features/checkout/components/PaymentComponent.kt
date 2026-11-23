package com.localpk.app.features.checkout.components

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.base.Platform
import com.localpk.app.features.cart.components.PaymentOptionSection
import com.localpk.app.features.cart.components.WalletPaymentSection
import com.localpk.app.features.restaurant.data.FoodCartUIData
import com.localpk.app.features.wallet.data.WalletBalance
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.cash_icon
import dealcart.composeapp.generated.resources.easypaisa_checkout
import dealcart.composeapp.generated.resources.edit_disabled_icon
import dealcart.composeapp.generated.resources.edit_icon
import dealcart.composeapp.generated.resources.jz_new_white
import dealcart.composeapp.generated.resources.master_icon
import dealcart.composeapp.generated.resources.pay_using
import dealcart.composeapp.generated.resources.visa_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentComponent(
    discountedCartTotal: Int?,
    isWalletEnabled: Boolean?,
    walletAmount: WalletBalance?,
    foodCart: FoodCartUIData?,
    updateWalletUsage: (checked: Boolean) -> Unit,
    onClick: () -> Unit,
    paymentMethod: String?,
    jazzPhoneNumber: String,
    paymentId: Double,
    onClickToSelectPayment: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(
                color = Color.White,
                RoundedCornerShape(8.dp)
            )
            .padding(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.pay_using),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = TextSubdued,
                )
            )

            if (foodCart.isNull()) {
                Spacer(modifier = Modifier.width(7.dp))

                Image(
                    painter = painterResource(Res.drawable.cash_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )

                if (Platform.isAndroid()) {
                    if (RemoteConfigs.isCardEnabled()) {
                        Image(
                            painter = painterResource(Res.drawable.visa_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                        )

                        Image(
                            painter = painterResource(Res.drawable.master_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                        )
                    }

                    if (RemoteConfigs.isJazzcashEnabled()) {
                        Image(
                            painter = painterResource(Res.drawable.jz_new_white),
                            contentDescription = null,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                        )
                    }

                    if (RemoteConfigs.isEasyPaisaEnabled()) {
                        Image(
                            painter = painterResource(Res.drawable.easypaisa_checkout),
                            contentDescription = null,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                        )
                    }
                }

                if (Platform.isIos()) {
                    if (RemoteConfigs.isIosCardEnabled()) {
                        Image(
                            painter = painterResource(Res.drawable.visa_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                        )

                        Image(
                            painter = painterResource(Res.drawable.master_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                        )
                    }

                    if (RemoteConfigs.isIosJazzcashEnabled()) {
                        Image(
                            painter = painterResource(Res.drawable.jz_new_white),
                            contentDescription = null,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                        )
                    }

                    if (RemoteConfigs.isIosEasyPaisaEnabled()) {
                        Image(
                            painter = painterResource(Res.drawable.easypaisa_checkout),
                            contentDescription = null,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(.1f))

                if (Platform.isAndroid())
                    if (RemoteConfigs.isJazzcashEnabled() || RemoteConfigs.isCardEnabled() || RemoteConfigs.isEasyPaisaEnabled()) {
                        Image(
                            painter = painterResource(if ((walletAmount?.Balance.getValueOrDefault() >= discountedCartTotal.getValueOrDefault()) && (isWalletEnabled.default())) Res.drawable.edit_disabled_icon else Res.drawable.edit_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .clickable(
                                    enabled = !(isWalletEnabled == true && walletAmount?.Balance.getValueOrDefault() >= discountedCartTotal.getValueOrDefault()),
                                    onClick = {
                                        onClickToSelectPayment()
                                    }
                                )
                        )
                    }
                if (Platform.isIos())
                    if (RemoteConfigs.isIosJazzcashEnabled() || RemoteConfigs.isIosCardEnabled() || RemoteConfigs.isIosEasyPaisaEnabled()) {
                        Image(
                            painter = painterResource(if ((walletAmount?.Balance.getValueOrDefault() >= discountedCartTotal.getValueOrDefault()) && (isWalletEnabled.default())) Res.drawable.edit_disabled_icon else Res.drawable.edit_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .clickable(
                                    enabled = !(isWalletEnabled == true && walletAmount?.Balance.getValueOrDefault() >= discountedCartTotal.getValueOrDefault()),
                                    onClick = {
                                        onClickToSelectPayment()
                                    }
                                )
                        )
                    }
            }
        }
        if (walletAmount != null && discountedCartTotal != null && isWalletEnabled != null && foodCart.isNull()) {
            WalletPaymentSection(
                isWalletEnabled = isWalletEnabled,
                walletAmount = walletAmount,
                updateWalletUsage = { updateWalletUsage(it) },
                onClick = {
                    onClick()
                }
            )
        }

        PaymentOptionSection(
            paymentMethod = paymentMethod.default(),
            jazzPhoneNumber = jazzPhoneNumber,
            paymentId = paymentId,
            discountedCartTotal = discountedCartTotal,
            isWalletEnabled = isWalletEnabled,
            walletAmount = walletAmount
        )
    }
}