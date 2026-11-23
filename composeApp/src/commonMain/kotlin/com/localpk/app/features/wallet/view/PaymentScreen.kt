package com.localpk.app.features.wallet.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.features.wallet.components.PaymentContent
import com.localpk.app.features.wallet.data.CardTopUpRequest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PaymentBottomSheetScreen(
    val amount: Double
) : Screen {

    @OptIn(ExperimentalUuidApi::class)
    @Composable
    override fun Content() {

        val args = CardTopUpRequest(amount = amount)
        val viewModel = koinViewModel<PaymentViewModel>(key = "Payment_${Uuid.random()}") {
            parametersOf(args)
        }

        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            PaymentContent(
                args = args,
                paymentViewModel = viewModel
            )
        }
    }
}