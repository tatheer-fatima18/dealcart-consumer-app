package com.localpk.app.features.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.wallet.data.WalletBalance
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.ButtonSurfaceSubdued
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackgroundGhosted
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.VioletBlue
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.dc_pay
import dealcart.composeapp.generated.resources.pay_wallet
import dealcart.composeapp.generated.resources.tap_to_topup
import dealcart.composeapp.generated.resources.use_wallet_first
import dealcart.composeapp.generated.resources.your_wallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WalletPaymentSection(
    isWalletEnabled: Boolean,
    walletAmount: WalletBalance,
    updateWalletUsage: (checked: Boolean) -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                BorderStroke(1.dp, BorderPrimaryDefault),
                shape = RoundedCornerShape(8.dp)
            ),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(4.dp))

                    Image(
                        painter = painterResource(Res.drawable.pay_wallet),
                        contentDescription = null,
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column {
                            Text(
                                modifier = Modifier
                                    .padding(start = 3.dp),
                                text = stringResource(Res.string.dc_pay),
                                style = TextStyle(
                                    color = TextSubdued,
                                    fontSize = 14.sp,
                                    lineHeight = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Text(
                                modifier = Modifier
                                    .padding(start = 3.dp),
                                text = stringResource(Res.string.use_wallet_first),
                                style = TextStyle(
                                    color = TextSubdued,
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(.1f))

                    Text(
                        modifier = Modifier
                            .padding(start = 3.dp),
                        text = walletAmount.formattedTotalWalletBalance(),
                        style = TextStyle(
                            color = TextSubdued,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )


                    Spacer(modifier = Modifier.width(10.dp))

                    Box {
                        DCCoinsSwitch(
                            isDCCoinEnabled = walletAmount.Balance > 0,
                            checked = isWalletEnabled,
                            onCheckedChange = { updateWalletUsage(it) },
                            trackWidth = 48.dp,
                            trackHeight = 24.dp,
                            thumbSize = 18.dp,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ButtonSurfaceBrand,
                                uncheckedThumbColor = ButtonSurfaceSubdued,
                                uncheckedTrackColor = SurfaceBackgroundGhosted
                            )
                        )
                        if (walletAmount.Balance > 0) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { updateWalletUsage(isWalletEnabled.not()) }
                            )
                        }
                    }
                }
                if (walletAmount.Balance.toInt() == 0) {
                    Row(
                        modifier = Modifier.padding(start = 4.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = stringResource(Res.string.your_wallet),
                            style = TextStyle(
                                color = TextSubdued,
                                fontSize = 12.sp,
                                lineHeight = 14.4.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Normal
                            )
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        if (RemoteConfigs.isTopUpEnabled())
                            Text(
                                modifier = Modifier
                                    .clickable {
                                        onClick()
                                    },
                                text = stringResource(Res.string.tap_to_topup),
                                style = TextStyle(
                                    color = VioletBlue,
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}