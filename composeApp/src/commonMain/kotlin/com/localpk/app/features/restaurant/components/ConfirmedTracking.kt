package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.AnimatedLottie
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.theming.BarlowCondensedFontFamily
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.call_restaurant
import dealcart.composeapp.generated.resources.confirmed_order
import dealcart.composeapp.generated.resources.confirming_order
import dealcart.composeapp.generated.resources.discover_restaurant
import dealcart.composeapp.generated.resources.fallback_logo
import dealcart.composeapp.generated.resources.need_help
import dealcart.composeapp.generated.resources.open_my_wallet
import dealcart.composeapp.generated.resources.order_cancelled
import dealcart.composeapp.generated.resources.order_details
import dealcart.composeapp.generated.resources.please_wait_while_your_order
import dealcart.composeapp.generated.resources.preparing
import dealcart.composeapp.generated.resources.want_to_check
import dealcart.composeapp.generated.resources.we_are_sorry
import dealcart.composeapp.generated.resources.we_are_sorry_cancel
import dealcart.composeapp.generated.resources.we_are_sorry_refund
import dealcart.composeapp.generated.resources.your_order_is_being
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConfirmedTracking(
    onBack: () -> Unit,
    onHelp: () -> Unit,
    isPrepaid: Boolean,
    isDelayed: Boolean,
    onClickWallet: () -> Unit,
    onClickRestaurants: () -> Unit,
    onCall: () -> Unit,
    onClickOrders: () -> Unit,
    pendingStatus: Boolean,
    confirmedStatus: Boolean,
    cancelledStatus: Boolean,
    deliveryTime: String,
    logo: String,
    isCheckOut: Boolean
) {
    Top(onBack, onHelp, isCheckOut)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (pendingStatus) {
            var jsonString by remember { mutableStateOf<String?>(null) }

            // Launch coroutine to load Lottie JSON only once
            LaunchedEffect(Unit) {
                jsonString = Res.readBytes("files/order_loader.json").decodeToString()
            }

            jsonString?.let {
                AnimatedLottie(lottieSpec = LottieCompositionSpec.JsonString(it),
                    modifier = Modifier
                        .size(width = 350.dp, height = 239.dp)
                    )
            }
        } else {
            if (cancelledStatus)
                loadImagePainterWithValidation(
                    imageUrl = logo,
                    fallbackRes = Res.drawable.fallback_logo,
                    contentDescription = null,
                    modifier = Modifier
                        .width(84.dp)
                        .height(84.dp)
                        .clip(CircleShape)
                )
            else
                Image(
                    painter = painterResource(Res.drawable.confirmed_order),
                    contentDescription = null,
                    modifier = Modifier
                        .width(84.dp)
                        .height(84.dp)
                )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(if (pendingStatus) Res.string.confirming_order else if (cancelledStatus) Res.string.order_cancelled else Res.string.preparing),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Bold,
                color = if (pendingStatus || confirmedStatus || isDelayed) ButtonSurfaceBrand else IconDanger,
            ),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(horizontal = 30.dp),
            text = stringResource(
                if (pendingStatus) Res.string.please_wait_while_your_order else if (isPrepaid) Res.string.we_are_sorry_refund else if (cancelledStatus) Res.string.we_are_sorry_cancel
                else if (isDelayed) Res.string.we_are_sorry else Res.string.your_order_is_being
            ),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = TextSubdued,
            ),
            textAlign = TextAlign.Center
        )
        if (confirmedStatus || isDelayed) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = deliveryTime,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = BarlowCondensedFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = ButtonSurfaceBrand,
                ),
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        if (pendingStatus || confirmedStatus || isDelayed) {
            Text(
                text = stringResource(if (isDelayed) Res.string.want_to_check else Res.string.need_help),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = TextSubdued,
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
                    .background(
                        color = ButtonSurfaceBrand,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onCall() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.call_restaurant).uppercase(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
                    .background(
                        color = SurfaceBackground,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onClickOrders() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.order_details).uppercase(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        color = TextSubdued
                    )
                )
            }
        }
        if (cancelledStatus) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
                    .background(
                        color = ButtonSurfaceBrand,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onClickRestaurants() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.discover_restaurant).uppercase(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }
            if (isPrepaid) {
                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .background(
                            color = SurfaceBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onClickWallet() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.open_my_wallet).uppercase(),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = TextSubdued
                        )
                    )
                }
            }
        }
    }
}