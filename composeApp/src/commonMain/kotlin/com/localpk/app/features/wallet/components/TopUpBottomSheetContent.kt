package com.localpk.app.features.wallet.components

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.cart.data.PaymentMethodParams
import com.localpk.app.features.wallet.data.ResponseArgs
import com.localpk.app.features.wallet.view.PaymentViewModel
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.transparent
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.top_up
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Common interfaces for payment elements
 */
expect interface PaymentElement {
    fun onReady(callback: (Boolean) -> Unit)
    fun confirmPayment(
        customerName: String,
        clientSecret: String,
        encryptionKey: String,
        callback: (String) -> Unit
    )

    fun clear()
}

expect interface EasyPaisaPaymentElement {
    fun onReady(callback: (Boolean) -> Unit)
    fun confirmPayment(
        customerName: String,
        clientSecret: String,
        encryptionKey: String,
        callback: (String) -> Unit
    )

    fun clear()
}

/**
 * Expect functions for platform-specific implementations
 */
expect fun createPaymentElement(context: Any?): Any?
expect fun createEasyPaisaPaymentElement(context: Any?): Any?

@Composable
expect fun getPlatformContext(): Any?

/**
 * Common payment response handling
 */
fun paymentResponse(
    response: String,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    callback: () -> Unit
) {
    try {
        val success = !response.contains("\"error\":true") && !response.contains("\"error\": true")
        if (success) {
            onSuccess()
//            eventLogger.executeTopUp(TopUpStatus.SUCCESS.status)
        } else {
            onFailure()
//            toastManager.showSuccess(jsonResponse.optString("message", "Something went wrong"))
//            eventLogger.executeTopUp(TopUpStatus.FAILURE.status)
        }
        callback()
    } catch (e: Exception) {
        e.printStackTrace()
//            toastManager.showError("Invalid response received")
        onFailure()
        callback()
    }
}

/**
 * Expect functions for platform-specific view rendering
 */
@Composable
expect fun PaymentElementView(paymentElement: Any?, modifier: Modifier)

@Composable
expect fun EasyPaisaPaymentElementView(paymentElement: Any?, modifier: Modifier)


@Composable
fun TopUpBottomSheetContent(
    title: String,
    paymentMethods: List<PaymentMethodParams>,
    paymentViewModel: PaymentViewModel,
    amount: Double,
    onNavigateToResponse: (ResponseArgs) -> Unit = {}
) {
    val platformContext = getPlatformContext()

    val bottomSheetNavigator = LocalBottomSheetNavigator.current

    // Payment state management
    var shouldNavigate by remember { mutableStateOf(false) }
    var isPaymentSuccessful by remember { mutableStateOf(false) }

    val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
    // Track selected payment method
    val cardPaymentDetails by paymentViewModel.cardPaymentDetails.collectAsState()
    val textFieldValue = remember {
        mutableStateOf(
            sharedPreferencesManager.getUserName().ifBlank { "" }
        )
    }
    val selectedPaymentType = remember {
        mutableStateOf<PaymentMethodParams?>(null)
    }

    // Create payment elements based on selected method
    val paymentElement by remember(selectedPaymentType.value) {
        mutableStateOf(
            if (selectedPaymentType.value?.paymentType == PaymentMethod.CARD.method) {
                createPaymentElement(platformContext)
            } else null
        )
    }

    val easyPaisaPaymentElement by remember(selectedPaymentType.value) {
        mutableStateOf(
            if (selectedPaymentType.value?.paymentType == PaymentMethod.EASYPAISA.method) {
                createEasyPaisaPaymentElement(platformContext)
            } else null
        )
    }

    // Track ready states
    var isCardReady by remember { mutableStateOf(false) }
    var isEasyPaisaReady by remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(paymentElement, selectedPaymentType.value?.paymentType) {
        if (selectedPaymentType.value?.paymentType == PaymentMethod.CARD.method && paymentElement != null) {
            (paymentElement as? PaymentElement)?.onReady { isReady ->
                isCardReady = isReady
            }
        }
    }
    LaunchedEffect(easyPaisaPaymentElement, selectedPaymentType.value?.paymentType) {
        if (selectedPaymentType.value?.paymentType == PaymentMethod.EASYPAISA.method && easyPaisaPaymentElement != null) {
            (easyPaisaPaymentElement as? EasyPaisaPaymentElement)?.onReady { isReady ->
                isEasyPaisaReady = isReady
            }
        }
    }

    LaunchedEffect(isCardReady, isEasyPaisaReady) {
        isButtonEnabled = when (selectedPaymentType.value?.paymentType) {
            PaymentMethod.CARD.method -> isCardReady
            PaymentMethod.EASYPAISA.method -> isEasyPaisaReady
            else -> true
        }
    }

    // Handle navigation when payment completes (equivalent to onResume logic)
    LaunchedEffect(shouldNavigate, isPaymentSuccessful) {
        if (shouldNavigate) {
            shouldNavigate = false
            val args = ResponseArgs(
                success = isPaymentSuccessful,
                amount = amount
            )
            onNavigateToResponse(args)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .wrapContentHeight()
            .padding(bottom = 20.dp)
            .imePadding()
            .background(
                Color.White,
                shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextSubdued,
                fontFamily = PoppinsFontFamily(),
                textAlign = TextAlign.Center
            )
        }

        LazyColumn {
            itemsIndexed(paymentMethods) { _, paymentMethod ->

                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp, vertical = 8.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            BorderPrimaryDefault,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    PaymentItem(
                        paymentMethodItem = paymentMethod,
                        textFieldValue,
                        paymentElement,
                        cardPaymentDetails,
                        easyPaisaPaymentElement
                    ) { selectedPaymentMethod ->
                        paymentMethods.forEach {
                            it.isSelected.value = it == selectedPaymentMethod
                        }
                        selectedPaymentType.value = selectedPaymentMethod
                        paymentViewModel.selectedPaymentMethod.value = selectedPaymentMethod

                        if ((selectedPaymentMethod.paymentType == PaymentMethod.CARD.method ||
                                    selectedPaymentMethod.paymentType == PaymentMethod.EASYPAISA.method) &&
                            sharedPreferencesManager.getUserName().isNotEmpty()
                        ) {
                            paymentViewModel.cardTopUpRequest(
                                paymentViewModel.argsData.amount,
                                selectedPaymentMethod.paymentType
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                TopUpButton(
                    textColor = if (isButtonEnabled) Color.White
                    else TextSubduedHalf,
                    text = stringResource(Res.string.top_up),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
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
                                val paymentType =
                                    paymentMethods.firstOrNull { it.isSelected.value }?.paymentType.default()
                                when (paymentType) {
                                    PaymentMethod.CARD.method -> {
                                        if (sharedPreferencesManager
                                                .getUserName()
                                                .isEmpty()
                                        ) {
                                            paymentViewModel.updatePersonalDetails(textFieldValue.value)
                                        }
                                        if (cardPaymentDetails.isNotNull()) {
                                            try {
                                                (paymentElement as? PaymentElement)?.confirmPayment(
                                                    customerName = sharedPreferencesManager.getUserName(),
                                                    clientSecret = cardPaymentDetails?.clientSecret.toString(),
                                                    encryptionKey = cardPaymentDetails?.encryptionKey.toString(),
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
                                                            },
                                                            callback = {
                                                                bottomSheetNavigator.hide()
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

                                    PaymentMethod.JAZZ_CASH.method -> {
                                    }

                                    PaymentMethod.EASYPAISA.method -> {
                                        if (sharedPreferencesManager
                                                .getUserName()
                                                .isEmpty()
                                        ) {
                                            paymentViewModel.updatePersonalDetails(textFieldValue.value)
                                        }
                                        if (isButtonEnabled && cardPaymentDetails.isNotNull()) {
                                            isButtonEnabled = false
                                            try {
                                                (easyPaisaPaymentElement as? EasyPaisaPaymentElement)?.confirmPayment(
                                                    customerName = sharedPreferencesManager.getUserName(),
                                                    clientSecret = cardPaymentDetails?.clientSecret.toString(),
                                                    encryptionKey = cardPaymentDetails?.encryptionKey.toString(),
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
                                                            },
                                                            callback = {
                                                                bottomSheetNavigator.hide()
                                                            }
                                                        )
                                                    }
                                                )
                                            } catch (e: Exception) {
                                                isButtonEnabled = true
                                                throw RuntimeException(e)
                                            }
                                        }
                                    }
                                }
                            }
                        )
                )
                
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}