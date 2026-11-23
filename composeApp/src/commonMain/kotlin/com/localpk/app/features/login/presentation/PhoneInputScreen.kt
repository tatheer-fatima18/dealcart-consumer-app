@file:OptIn(ExperimentalUuidApi::class)

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.login.components.TermsBottomSheet
import com.localpk.app.features.login.components.TermsContent
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.data.toOTPScreen
import com.localpk.app.features.login.presentation.components.PhoneInputField
import com.localpk.app.features.login.presentation.components.PhoneInputFooter
import com.localpk.app.features.login.presentation.components.PhoneInputHeader
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.back_btn
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

class PhoneInputScreen(val navSource: String = EMPTY_STRING) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<PhoneInputViewModel>()
        val phoneNumber by viewModel.phone.collectAsState()
        val isContinueEnabled by viewModel.isContinueEnabled.collectAsState()
        val isOtpFetchingInProgress by viewModel.isOtpFetchingInProgress.collectAsState()
        val openSheet = remember { mutableStateOf(false) }
        val eventLogger: EventLogger = koinInject()

        val sharedPreferencesManager: SharedPreferencesManager = koinInject()
        LaunchedEffect(Unit) {
            eventLogger.signupScreen(ScreenSource.SIGNUP_SCREEN.key)
        }

        LaunchedEffect(Unit) {
            if (sharedPreferencesManager.isLoggedIn().not())
                viewModel.resetPhone()
        }
        StatusBarIconColor(backgroundColor = Color.White)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .background(color = Color.White)
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
            PhoneInputHeader()

            // Phone input field
            PhoneInputField(
                phoneNumber = phoneNumber,
                isContinueEnabled = isOtpFetchingInProgress,
                onPhoneChange = { viewModel.updatePhone(it) }
            )

            // Footer section
            PhoneInputFooter(
                isContinueEnabled = isContinueEnabled,
                onContinueClick = {
                    viewModel.onActionSignUp(phoneNumber, navSource) { otpArgs ->
                        navigator.push(
                            otpArgs.toOTPScreen()
                        )
                    }
                },
                onTermsClick = { openSheet.value = true }
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
            TermsBottomSheet(onClose = { openSheet.value = false }, content = { TermsContent() })
        }
    }
}