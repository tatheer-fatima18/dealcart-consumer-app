package com.localpk.app.features.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.base.Platform
import com.localpk.app.features.cart.data.PaymentMethod
import com.localpk.app.features.cart.data.PaymentMethodParams
import com.localpk.app.features.wallet.data.CardTopUpRequest
import com.localpk.app.features.wallet.data.ResponseArgs
import com.localpk.app.features.wallet.view.PaymentViewModel
import com.localpk.app.features.wallet.view.TopUpScreen
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.choose_payment_method
import dealcart.composeapp.generated.resources.easypaisa_icon
import dealcart.composeapp.generated.resources.master_card
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentContent(
    args: CardTopUpRequest,
    paymentViewModel: PaymentViewModel
) {

    // State to handle navigation after bottom sheet is hidden
    var pendingNavigation by remember { mutableStateOf<ResponseArgs?>(null) }

    // Handle navigation after bottom sheet is hidden
    LaunchedEffect(pendingNavigation) {
        pendingNavigation?.let { responseArgs ->
            TopUpScreen.updateIsTopUpCompleted(responseArgs)
            pendingNavigation = null
        }
    }
    val paymentMethods = buildList {

        if (Platform.isAndroid())
            if (RemoteConfigs.isCardEnabled()) {
                add(
                    PaymentMethodParams(
                        PaymentMethod.CARD.id,
                        Res.drawable.master_card,
                        "Pay via Card",
                        PaymentMethod.CARD.method,
                        mutableStateOf(false)
                    )
                )
            }
        if (Platform.isIos())
            if (RemoteConfigs.isIosCardEnabled()) {
                add(
                    PaymentMethodParams(
                        PaymentMethod.CARD.id,
                        Res.drawable.master_card,
                        "Pay via Card",
                        PaymentMethod.CARD.method,
                        mutableStateOf(false)
                    )
                )
            }

        if (Platform.isAndroid())
            if (RemoteConfigs.isEasyPaisaEnabled()) {
                add(
                    PaymentMethodParams(
                        PaymentMethod.EASYPAISA.id,
                        Res.drawable.easypaisa_icon,
                        "easypaisa",
                        PaymentMethod.EASYPAISA.method,
                        mutableStateOf(
                            false
                        )
                    )
                )
            }
        if (Platform.isIos())
            if (RemoteConfigs.isIosEasyPaisaEnabled()) {
                add(
                    PaymentMethodParams(
                        PaymentMethod.EASYPAISA.id,
                        Res.drawable.easypaisa_icon,
                        "easypaisa",
                        PaymentMethod.EASYPAISA.method,
                        mutableStateOf(
                            false
                        )
                    )
                )
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .background(Color.White, shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        TopUpBottomSheetContent(
            title = stringResource(Res.string.choose_payment_method),
            paymentMethods = paymentMethods,
            paymentViewModel = paymentViewModel,
            amount = args.amount,
            onNavigateToResponse = { responseArgs ->
                pendingNavigation = responseArgs
            }
        )
    }
}