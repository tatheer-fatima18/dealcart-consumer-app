package com.localpk.app.features.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.features.wallet.view.PaymentBottomSheetScreen
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackgroundGhosted
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.formatPKRCurrency
import com.localpk.app.utils.formatWithCommas
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.enter_an_amount
import dealcart.composeapp.generated.resources.navbutton_black
import dealcart.composeapp.generated.resources.top_up
import dealcart.composeapp.generated.resources.top_up_message_limit
import dealcart.composeapp.generated.resources.top_up_wallet
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopUpScreenContent() {

    val navigator = LocalNavigator.currentOrThrow
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    var rawInputValue by remember { mutableStateOf(TextFieldValue("")) }
    val topUpValues = RemoteConfigs.getTopUpValues()
    var showLimitMessage by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
    )
    {
        TopNav(
            title = stringResource(Res.string.top_up_wallet),
            backButton = Res.drawable.navbutton_black,
            onBackClick = { navigator.pop() })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Rs. ",
                            color = TextSubdued,
                            fontSize = 18.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PoppinsFontFamily(),
                        )
                        AmountInputField(
                            value = rawInputValue,
                            onValueChange = { newValue ->
                                if ((newValue.text.isEmpty() || newValue.text.all { it.isDigit() || it == ',' }) && newValue.text.length <= 6) {
                                    rawInputValue = TextFieldValue(
                                        (newValue.text).formatWithCommas(),
                                        selection = TextRange(newValue.text.length)
                                    )
                                    showLimitMessage = false
                                } else
                                    showLimitMessage = true
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.enter_an_amount),
                        color = TextSubdued,
                        fontSize = 14.sp,
                        lineHeight = 16.8.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PoppinsFontFamily(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        topUpValues.forEach { getTopUpAmount ->
                            Chip(
                                title = (getTopUpAmount.amount).formatPKRCurrency(),
                                onClick = {
                                    rawInputValue = TextFieldValue(
                                        (getTopUpAmount.amount.toString()).formatWithCommas(),
                                        selection = TextRange(getTopUpAmount.amount.toString().length)
                                    )
                                    showLimitMessage = false
                                    focusManager.clearFocus()
                                })
                        }
                    }
                    if (showLimitMessage) {
                        Spacer(modifier = Modifier.height(8.dp))
                        TopUMessageCard(Res.string.top_up_message_limit)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                TopUpButton(
                    textColor = if (rawInputValue.text.isNotEmpty() || rawInputValue.text != "0") Color.White else TextSubduedHalf,
                    text = stringResource(Res.string.top_up),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            color = if (rawInputValue.text.isEmpty() || rawInputValue.text == "0") SurfaceBackgroundGhosted else ButtonSurfaceBrand,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable(
                            enabled = rawInputValue.text.isNotEmpty() && rawInputValue.text != "0",
                            onClick = {
                                bottomSheetNavigator.show(
                                    PaymentBottomSheetScreen(
                                        amount =
                                        rawInputValue.text.replace(",", "").toDouble()
                                    )
                                )
                            }
                        )
                )
            }
        }
    }
}