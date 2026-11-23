package com.localpk.app.features.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
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
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.apnay_filhal
import dealcart.composeapp.generated.resources.items_dekhain
import dealcart.composeapp.generated.resources.no_orders
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyOrderComponent(
    orderText: String = stringResource(Res.string.no_orders),
    orderTextDescription: String = stringResource(Res.string.apnay_filhal),
    buttonText: String = stringResource(Res.string.items_dekhain),
    isButtonVisible: Boolean,
    onClickViewAll: () -> Unit = {}
) {
    var jsonString by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        jsonString = Res.readBytes("files/cart.json").decodeToString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        jsonString?.let {
            AnimatedLottie(
                modifier = Modifier.size(width = 350.dp, height = 239.dp),
                lottieSpec = LottieCompositionSpec.JsonString(it)
            )
        }
        Text(
            text = orderText,
            style = TextStyle(
                color = TextSubdued,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFontFamily()
            )
        )
        Text(
            text = orderTextDescription,
            style = TextStyle(
                color = TextSubdued,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily()
            )
        )
        if (isButtonVisible)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = ButtonSurfaceBrand)
                    .clickable { onClickViewAll() }
                    .padding(16.dp),
                text = buttonText,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            )
    }
}