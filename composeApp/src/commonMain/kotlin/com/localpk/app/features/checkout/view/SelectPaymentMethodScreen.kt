package com.localpk.app.features.checkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.base.Platform
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.cart.data.PaymentMethodParams
import com.localpk.app.features.checkout.components.CustomPopUp
import com.localpk.app.features.checkout.components.PaymentMethodListing
import com.localpk.app.features.checkout.components.TopBarCheckout
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.SELECT_PAYMENT_METHOD_SCREEN_BG
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.changee_payment
import dealcart.composeapp.generated.resources.easypaisa_logo
import dealcart.composeapp.generated.resources.enter_name
import dealcart.composeapp.generated.resources.ic_cod
import dealcart.composeapp.generated.resources.master_card
import dealcart.composeapp.generated.resources.name_mandatory
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class SelectPaymentMethodScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<CartCheckoutViewModel>()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val args = viewModel.paymentMethodUiData.value
        val selectPaymentMethodViewModel =
            koinViewModel<SelectPaymentMethodViewModel>(parameters = { parametersOf(args) })

        if (args == null) {
            return
        }

        val paymentMethods = buildList {
            if (Platform.isAndroid())
                if (RemoteConfigs.isCardEnabled()) {
                    add(
                        PaymentMethodParams(
                            PaymentMethod.CARD.id,
                            Res.drawable.master_card,
                            "Credit/Debit Card",
                            PaymentMethod.CARD.method,
                            mutableStateOf(
                                args.paymentId == PaymentMethod.CARD.id
                            )
                        )
                    )
                }
            if (Platform.isIos())
                if (RemoteConfigs.isIosCardEnabled()) {
                    add(
                        PaymentMethodParams(
                            PaymentMethod.CARD.id,
                            Res.drawable.master_card,
                            "Credit/Debit Card",
                            PaymentMethod.CARD.method,
                            mutableStateOf(
                                args.paymentId == PaymentMethod.CARD.id
                            )
                        )
                    )
                }

//                if (RemoteConfigs.isJazzcashEnabled()) {
//                    add(
//                        PaymentMethodParams(
//                            PaymentMethod.JAZZ_CASH.id,
//                            Res.drawable.jc_new_logo,
//                            "JazzCash",
//                            PaymentMethod.JAZZ_CASH.method,
//                            mutableStateOf(
//                                args.paymentId == PaymentMethod.JAZZ_CASH.id
//                            )
//                        )
//                    )
//                }

            if (Platform.isAndroid())
                if (RemoteConfigs.isEasyPaisaEnabled()) {
                    add(
                        PaymentMethodParams(
                            PaymentMethod.EASYPAISA.id,
                            Res.drawable.easypaisa_logo,
                            "easypaisa",
                            PaymentMethod.EASYPAISA.method,
                            mutableStateOf(
                                args.paymentId == PaymentMethod.EASYPAISA.id
                            )
                        )
                    )
                }
            if (Platform.isIos())
                if (RemoteConfigs.isIosEasyPaisaEnabled()) {
                    add(
                        PaymentMethodParams(
                            PaymentMethod.EASYPAISA.id,
                            Res.drawable.easypaisa_logo,
                            "easypaisa",
                            PaymentMethod.EASYPAISA.method,
                            mutableStateOf(
                                args.paymentId == PaymentMethod.EASYPAISA.id
                            )
                        )
                    )
                }
            add(
                PaymentMethodParams(
                    PaymentMethod.CASH_ON_DELIVERY.id,
                    Res.drawable.ic_cod,
                    "Cash on Delivery",
                    PaymentMethod.CASH_ON_DELIVERY.method,
                    mutableStateOf(
                        args.paymentId == PaymentMethod.CASH_ON_DELIVERY.id
                    )
                )
            )
        }

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        var showPopUp by remember { mutableStateOf(false) }

        val navigator = LocalNavigator.currentOrThrow

        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize().statusBarsPadding()
                    .background(color = SELECT_PAYMENT_METHOD_SCREEN_BG)
            ) {
                Column {
                    TopBarCheckout(stringResource(Res.string.changee_payment), onClick = {
                        args.listener.invoke(
                            selectPaymentMethodViewModel.selectedPaymentMethod.value.paymentType.default(),
                            selectPaymentMethodViewModel.selectedPaymentMethod.value.id.getValueOrDefault(),
                        )
                        navigator.pop()
                    })
                    PaymentMethodListing(
                        paymentMethods = paymentMethods,
                        onPaymentMethodSelected = { selectedPaymentMethod ->
                            selectPaymentMethodViewModel.selectedPaymentMethod.value =
                                selectedPaymentMethod
                            when (selectedPaymentMethod.paymentType) {
                                PaymentMethod.JAZZ_CASH.method -> {
//                                eventLogger.selectJazzCashPaymentOption(sharedPreferencesManager.getUserId())
                                    args.listener.invoke(
                                        PaymentMethod.JAZZ_CASH.method,
                                        selectedPaymentMethod.id
                                    )
                                    navigator.pop()
                                }

                                PaymentMethod.CARD.method -> {
//                                eventLogger.selectCardPaymentOption(sharedPreferencesManager.getUserId())
                                    args.listener.invoke(
                                        PaymentMethod.CARD.method,
                                        selectedPaymentMethod.id
                                    )
//                                eventLogger.switchPaymentOption(PaymentMethod.CARD.method)

                                    if (sharedPreferencesManager.getUserName()
                                            .isNotBlank()
                                    ) {
                                        navigator.pop()
                                    } else {
                                        showPopUp = true
                                    }
                                }

                                PaymentMethod.EASYPAISA.method -> {
                                    args.listener.invoke(
                                        PaymentMethod.EASYPAISA.method,
                                        selectedPaymentMethod.id
                                    )

                                    if (sharedPreferencesManager.getUserName()
                                            .isNotBlank()
                                    ) {
                                        navigator.pop()
                                    } else {
                                        showPopUp = true
                                    }
                                }

                                else -> {
                                    args.listener.invoke(
                                        selectedPaymentMethod.paymentType.default(),
                                        selectedPaymentMethod.id
                                    )
                                    navigator.pop()
                                }
                            }
                        }
                    )
                }

                if (showPopUp) {
                    ModalBottomSheet(
                        containerColor = Color.White,
                        onDismissRequest = {},
                        sheetState = sheetState,
                        dragHandle = {},
                    ) {
                        CustomPopUp(
                            title1 = stringResource(Res.string.enter_name),
                            title2 = stringResource(Res.string.name_mandatory),
                            onDismiss = {
                                showPopUp = false
                                navigator.pop()
                            },
                            updateName = {
                                selectPaymentMethodViewModel.updatePersonalDetails(it)
                            }
                        )
                    }
                }
            }
        }

    }
}
