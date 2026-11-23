package com.localpk.app.features.login.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.cart.view.CartScreen
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.checkout.view.CartCheckoutScreen
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.login.components.TermsBottomSheet
import com.localpk.app.features.login.components.TermsContent
import com.localpk.app.features.login.data.AuthUiModel
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.data.User
import com.localpk.app.features.login.presentation.components.OTPFooter
import com.localpk.app.features.login.presentation.components.OTPHeader
import com.localpk.app.features.login.presentation.components.OTPInputField
import com.localpk.app.features.orders.presentation.OrdersScreen
import com.localpk.app.features.promo.view.PromosScreen
import com.localpk.app.features.share_to_friend.presentation.ShareToFriendScreen
import com.localpk.app.utils.EMPTY_STRING
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.back_btn
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import com.localpk.app.otp.getSharedOtpProvider
import androidx.compose.runtime.DisposableEffect
import com.localpk.app.features.address.presentation.AddressListViewModel
import com.localpk.app.features.frequently_ordered.viewModel.FrequentlyOrderedViewModel
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.PRODUCT_LIMIT_MIN
import com.localpk.app.utils.StatusBarIconColor


class OTPScreen(
    val phone: String = EMPTY_STRING,
    var hash: String = EMPTY_STRING,
    var fullName: String = EMPTY_STRING,
    var otp: String = EMPTY_STRING,
    val source: String = EMPTY_STRING,
    var id: Long = 0L,
    var firstName: String? = EMPTY_STRING,
    var lastName: String? = EMPTY_STRING,
    var phoneNumber: String = EMPTY_STRING,
    var email: String = EMPTY_STRING,
    var isVerified: Boolean,
    var isCL: Boolean,
    val gender: String? = EMPTY_STRING,
    val organization: String? = EMPTY_STRING
) : Screen {
    @Composable
    override fun Content() {

        val otpArgs = AuthUiModel(
            phone = phone,
            hash = hash,
            user = User(
                id = id,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                email = email,
                isVerified = isVerified,
                isCL = isCL,
                gender = gender,
                organization = organization
            ),
            fullName = fullName,
            otp = otp,
            source = source
        )

        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val viewModel = koinViewModel<OTPViewModel>()
        val cartViewModel = koinInject<CartViewModel>()
        val addressesViewModel = koinViewModel<AddressListViewModel>()
        val frequentlyOrderedViewModel = koinViewModel<FrequentlyOrderedViewModel>()
        viewModel.setOTPArgs(otpArgs)
        viewModel.resendButtonState()
        // Collect state
        val resendButton by viewModel.resendButtonState.collectAsState()
        val fullName by viewModel.fullName.collectAsState()
        val otpVal by viewModel.OTP.collectAsState()
        val isOTPIncorrect by viewModel.isOTPIncorrect.collectAsState()
        val verificationSuccess by viewModel.verificationSuccess.collectAsState()
        val openSheet = remember { mutableStateOf(false) }
        val errorMessage by viewModel.errorMessage.collectAsState()

        // Reset OTP/error once per screen entry to clear stale state (no recomposition wipe)
        val didInitialReset = remember { mutableStateOf(false) }
        if (!didInitialReset.value) {
            viewModel.resetOTP()
            didInitialReset.value = true
        }

        // Start OTP listening when screen composes
        val otpProvider = remember { getSharedOtpProvider() }
        LaunchedEffect(otpProvider, viewModel) {
            otpProvider.startListening { code ->
                viewModel.OTP.value = code
            }
        }
        // Stop OTP listening when screen disposes
        DisposableEffect(otpProvider, viewModel) {
            onDispose {
                otpProvider.stopListening()
            }
        }
        // Handle keyboard dismissal
        LaunchedEffect(viewModel) {
            viewModel.actionDismissKeyboard.events.collect {
                keyboardController?.hide()
            }
        }


        fun navigateBasedOnSource(source: String) {
            navigator.popUntil { screen ->
                screen !is OTPScreen && screen !is PhoneInputScreen
            }

            when (source) {
                ScreenSource.CART.key -> {
                    cartViewModel.setOnScreenResumeCallback {
                        cartViewModel.setIsCartLoading(true)
                        cartViewModel.executeCheckoutAfterCartLoad(
                            bottomSheetNavigator,
                            navigateToCheckout = {
                                navigator.push(CartCheckoutScreen())
                            },
                            cartMovConfirmListener = {
                                navigator.push(HomeScreen())
                            }
                        )
                    }
                    navigator.popUntil { it is CartScreen }
                }

                ScreenSource.PROMO_CART.key -> {
                    cartViewModel.setOnScreenResumeCallback {
                        cartViewModel.navigateToMorePromos(bottomSheetNavigator)
                    }
                    navigator.popUntil { it is CartScreen }
                }

                ScreenSource.ORDERS_LISTING.key -> {
                    navigator.popUntilRoot()
                    navigator.push(OrdersScreen())
                }

                ScreenSource.SHARE_TO_FRIEND.key -> {
                    navigator.push(ShareToFriendScreen())
                }

                ScreenSource.WEB_VIEW.key -> {
                    // Navigate to web view
                    navigator.popUntilRoot()
                }

                ScreenSource.PROMO_LISTING.key -> {
                    navigator.push(PromosScreen())
                }

                ScreenSource.WALLET.key, ScreenSource.MATCH_DETAIL_SCREEN.key -> {}

                else -> {
                    navigator.popUntilRoot()
                }
            }
        }

        // Callback for OTP verification
        val verificationCallback = remember {
            {
                keyboardController?.hide()
                viewModel.fullName.value = fullName
                addressesViewModel.setAddressesEmpty()
                addressesViewModel.loadSavedAddresses()
                frequentlyOrderedViewModel.emptyFrequentlyOrderedList()
                frequentlyOrderedViewModel.getFrequentlyOrderedItems(
                    limit = PRODUCT_LIMIT_MIN,
                    page = FIRST_PAGE,
                    cart = cartViewModel.cartState.value
                )
                cartViewModel.checkCartType()
                navigateBasedOnSource(otpArgs.source)
            }
        }

        // Auto-submit when OTP becomes complete (e.g., filled from SMS)
        LaunchedEffect(otpVal) {
            if (otpVal.length == 6) {
                viewModel.verifyOtp(verificationCallback)
            }
        }

        StatusBarIconColor(backgroundColor = Color.White)
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White).imePadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
            ) {
                // Back button
                Image(
                    modifier = Modifier.clickable(onClick = { navigator.pop() }),
                    painter = painterResource(Res.drawable.back_btn),
                    contentDescription = null,
                )

                // Header section
                OTPHeader(
                    phoneNumber = otpArgs.phoneNumberWithCountryCodeToPhone(),
                    resendButtonState = resendButton,
                    onBackClick = { navigator.pop() },
                    onResendClick = { viewModel.regenerateOtpWithSMS() }
                )

                // OTP input field
                OTPInputField(
                    otpValue = otpVal,
                    isOTPIncorrect = isOTPIncorrect,
                    onOtpChange = { viewModel.OTP.value = it },
                    onOtpComplete = { }
                )

                // Footer section
                OTPFooter(
                    isOTPIncorrect = isOTPIncorrect,
                    verificationSuccess = verificationSuccess,
                    onVerifyOtp = { viewModel.verifyOtp(verificationCallback) },
                    onTermsClick = { openSheet.value = true },
                    errorMessage = errorMessage
                )
            }

            AnimatedVisibility(
                visible = openSheet.value,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )

            ) {
                TermsBottomSheet(
                    onClose = { openSheet.value = false },
                    content = { TermsContent() })
            }
        }
    }
}