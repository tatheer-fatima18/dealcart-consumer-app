package com.localpk.app.features.checkout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.cart.data.CardPaymentScreenArgs
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.wallet.components.EasyPaisaPaymentElement
import com.localpk.app.features.wallet.components.EasyPaisaPaymentElementView
import com.localpk.app.features.wallet.components.PaymentElement
import com.localpk.app.features.wallet.components.PaymentElementView
import com.localpk.app.features.wallet.components.TopUpButton
import com.localpk.app.features.wallet.components.createEasyPaisaPaymentElement
import com.localpk.app.features.wallet.components.createPaymentElement
import com.localpk.app.features.wallet.components.getPlatformContext
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.transparent
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.clear
import dealcart.composeapp.generated.resources.close_main
import dealcart.composeapp.generated.resources.confirm_payment
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(InternalVoyagerApi::class)
@Composable
fun CheckoutPaymentBottomSheetContent(
    args: CardPaymentScreenArgs,
    triggerRedirect: () -> Unit,
    onNavigateToResponse: () -> Unit,
    onUserInitiatedBack: () -> Unit
) {

    var shouldNavigate by remember { mutableStateOf(false) }
    var isPaymentSuccessful by remember { mutableStateOf(false) }

    val platformContext = getPlatformContext()

    val navigator = LocalNavigator.currentOrThrow

    val sharedPreferencesManager = koinInject<SharedPreferencesManager>()

    val paymentElement by remember(args.paymentType) {
        mutableStateOf(
            if (args.paymentType == PaymentMethod.CARD.method) {
                createPaymentElement(platformContext)
            } else null
        )
    }

    val easyPaisaPaymentElement by remember(args.paymentType) {
        mutableStateOf(
            if (args.paymentType == PaymentMethod.EASYPAISA.method) {
                createEasyPaisaPaymentElement(platformContext)
            } else null
        )
    }

    var isCardReady by remember { mutableStateOf(false) }
    var isEasyPaisaReady by remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(paymentElement, args.paymentType) {
        if (args.paymentType == PaymentMethod.CARD.method && paymentElement != null) {
            (paymentElement as? PaymentElement)?.onReady { isReady ->
                isCardReady = isReady
            }
        }
    }
    LaunchedEffect(easyPaisaPaymentElement, args.paymentType) {
        if (args.paymentType == PaymentMethod.EASYPAISA.method && easyPaisaPaymentElement != null) {
            (easyPaisaPaymentElement as? EasyPaisaPaymentElement)?.onReady { isReady ->
                isEasyPaisaReady = isReady
            }
        }
    }

    LaunchedEffect(isCardReady, isEasyPaisaReady) {
        isButtonEnabled = when (args.paymentType) {
            PaymentMethod.CARD.method -> isCardReady
            PaymentMethod.EASYPAISA.method -> isEasyPaisaReady
            else -> true
        }
    }

    // Handle navigation when payment completes (equivalent to onResume logic)
    LaunchedEffect(shouldNavigate, isPaymentSuccessful) {
        if (shouldNavigate) {
            shouldNavigate = false
            navigator.pop()
            if (isPaymentSuccessful) {
                onNavigateToResponse()
            } else {
                triggerRedirect()
            }
        }
    }

    // Handle system back button press (Android & iOS swipe back via parent DisposableEffect)
    BackHandler(enabled = true) {
        onUserInitiatedBack()
        navigator.pop()
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .wrapContentHeight()
            .imePadding()
            .background(
                Color.White,
                shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
            )
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                painter = painterResource(Res.drawable.close_main),
                modifier = Modifier
                    .clickable {
                        onUserInitiatedBack()
                        navigator.pop()
                    },
                contentDescription = null
            )
        }

        when (args.paymentType) {
            PaymentMethod.JAZZ_CASH.method -> {
                // Jazz Cash implementation
            }

            PaymentMethod.CARD.method -> {
                PaymentElementView(
                    paymentElement = paymentElement,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }

            PaymentMethod.EASYPAISA.method -> {
                EasyPaisaPaymentElementView(
                    paymentElement = easyPaisaPaymentElement,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }

        TopUpButton(
            textColor = if (isButtonEnabled) Color.White
            else TextSubduedHalf,
            text = stringResource(Res.string.confirm_payment),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 15.dp)
                .background(
                    color = if (isButtonEnabled) ButtonSurfaceBrand
                    else SurfaceBackground,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    color = transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(enabled = isButtonEnabled,
                    onClick = {
                        when (args.paymentType) {
                            PaymentMethod.CARD.method -> {
                                try {
                                    (paymentElement as? PaymentElement)?.confirmPayment(
                                        customerName = sharedPreferencesManager.getUserName(),
                                        clientSecret = args.cardDetails?.clientSecret.default(),
                                        encryptionKey = args.cardDetails?.encryptionKey.default(),
                                        callback = { response ->
                                            paymentResponse(
                                                response,
                                                onSuccess = {
                                                    shouldNavigate = true
                                                    isPaymentSuccessful = true
                                                },
                                                onFailure = {
                                                    shouldNavigate = true
                                                    isPaymentSuccessful = false
                                                }
                                            )
                                        }
                                    )
                                    isButtonEnabled = false
                                } catch (e: Exception) {
                                    throw RuntimeException(e)
                                }
                            }

                            PaymentMethod.JAZZ_CASH.method -> {
                            }

                            PaymentMethod.EASYPAISA.method -> {
                                try {
                                    (easyPaisaPaymentElement as? EasyPaisaPaymentElement)?.confirmPayment(
                                        customerName = sharedPreferencesManager.getUserName(),
                                        clientSecret = args.cardDetails?.clientSecret.default(),
                                        encryptionKey = args.cardDetails?.encryptionKey.default(),
                                        callback = { response ->
                                            paymentResponse(
                                                response,
                                                onSuccess = {
                                                    shouldNavigate = true
                                                    isPaymentSuccessful = true
                                                },
                                                onFailure = {
                                                    shouldNavigate = true
                                                    isPaymentSuccessful = false
                                                }
                                            )
                                        }
                                    )
                                    isButtonEnabled = false
                                } catch (e: Exception) {
                                    throw RuntimeException(e)
                                }
                            }
                        }
                    }
                )
        )

        Spacer(modifier = Modifier.height(10.dp))

        TopUpButton(
            textColor = Color.White,
            text = stringResource(Res.string.clear).uppercase(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 15.dp)
                .background(
                    color = ButtonSurfaceBrand,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    color = transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(
                    onClick = {
                        when (args.paymentType) {
                            PaymentMethod.CARD.method -> {
                                (paymentElement as? PaymentElement)?.clear()
                            }

                            PaymentMethod.JAZZ_CASH.method -> {
                            }

                            PaymentMethod.EASYPAISA.method -> {
                                (easyPaisaPaymentElement as? EasyPaisaPaymentElement)?.clear()
                            }
                        }
                    }
                )
        )

        if (args.paymentType == PaymentMethod.EASYPAISA.method) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                "-Login to Easypaisa App and tap on payment notification to approve",
                modifier = Modifier.padding(horizontal = 15.dp),
                color = TextSubdued,
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFontFamily()
            )
            Text(
                "-If you miss the notification, go to My Approvals in side menu to confirm",
                modifier = Modifier.padding(horizontal = 15.dp),
                color = TextSubdued,
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFontFamily()
            )
        }
        Spacer(modifier = Modifier.height(34.dp))
    }
}

fun getPaymentMethodId(method: String): Double {
    return when (method) {
        PaymentMethod.CARD.method -> PaymentMethod.CARD.id
        PaymentMethod.EASYPAISA.method -> PaymentMethod.EASYPAISA.id
        else -> ZERO
    }
}

fun paymentResponse(
    response: String,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    try {
        val success = !response.contains("\"error\":true") && !response.contains("\"error\": true")
        if (success) {
            onSuccess()
        } else {
            onFailure()
            GlobalToastManager.showToast("Something went wrong", type = SnackbarType.ERROR)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        GlobalToastManager.showToast("Invalid response received", type = SnackbarType.ERROR)
        onFailure()
    }
}
