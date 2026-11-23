package com.localpk.app.features.wallet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.wallet.data.ResponseArgs
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.congratulations
import dealcart.composeapp.generated.resources.done
import dealcart.composeapp.generated.resources.failure_response
import dealcart.composeapp.generated.resources.payment_failed
import dealcart.composeapp.generated.resources.please_try_again
import dealcart.composeapp.generated.resources.success_response
import dealcart.composeapp.generated.resources.top_up_later
import dealcart.composeapp.generated.resources.try_payment_method
import dealcart.composeapp.generated.resources.your_wallet_has_been_topped_up
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResponseScreenContent(args: ResponseArgs) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp).statusBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(243.dp),
                painter = painterResource(
                    if (args.success) Res.drawable.success_response else Res.drawable.failure_response
                ),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )

            Text(
                text = stringResource(
                    if (args.success) Res.string.congratulations else Res.string.payment_failed
                ),
                color = TextSubdued,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PoppinsFontFamily()
            )

            Text(
                text = stringResource(
                    if (args.success) Res.string.your_wallet_has_been_topped_up else
                        Res.string.please_try_again
                ),
                color = TextSubdued,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = PoppinsFontFamily()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!args.success) {
                TopUpButton(
                    textColor = Color.White,
                    text = stringResource(Res.string.try_payment_method),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            color = ButtonSurfaceBrand,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable(
                            onClick = {
//                                navigate(
//                                    ResponseFragmentDirections.actionResponseToPayment(
//                                        CardTopUpRequest(args.amount)
//                                    )
//                                )
                            }
                        )
                )
            }

            TopUpButton(
                textColor = Color.White,
                text = if (args.success) stringResource(Res.string.done) else
                    stringResource(Res.string.top_up_later),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = ButtonSurfaceBrand,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(
                        onClick = {
                            //  responseViewModel.onActionBack()
                        }
                    )
            )
        }
    }
}