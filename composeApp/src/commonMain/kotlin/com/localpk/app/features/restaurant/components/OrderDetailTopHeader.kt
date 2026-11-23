package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.AnimatedLottie
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.cancel_order
import dealcart.composeapp.generated.resources.ic_print
import dealcart.composeapp.generated.resources.navbutton_black
import dealcart.composeapp.generated.resources.order_detail
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OrderDetailTopHeader(
    onBackClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onPrintClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    isInvoiceVisible: Boolean,
    isCancellable: Boolean
) {
    var jsonString by remember { mutableStateOf<String?>(null) }

    // Launch coroutine to load Lottie JSON only once
    LaunchedEffect(Unit) {
        jsonString = Res.readBytes("files/chat.json").decodeToString()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp)
            .background(color = Color.White, RoundedCornerShape(8.dp))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    onBackClick()
                },
            painter = painterResource(Res.drawable.navbutton_black),
            contentDescription = null
        )
        Text(
            modifier = Modifier,
            text = stringResource(Res.string.order_detail),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = TextSubdued
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (isCancellable) {
                Image(
                    modifier = Modifier
                        .width(27.dp)
                        .height(27.dp)
                        .clickable { onCancelClick() },
                    painter = painterResource(Res.drawable.cancel_order),
                    contentDescription = null
                )
            }
            if (isInvoiceVisible) {
                Spacer(modifier = Modifier.width(2.dp))
                Image(
                    modifier = Modifier
                        .width(27.dp)
                        .height(27.dp)
                        .clickable { onPrintClick() },
                    painter = painterResource(Res.drawable.ic_print),
                    contentDescription = null
                )
            }

            jsonString?.let {
                AnimatedLottie(
                    modifier = Modifier.size(27.dp),
                    lottieSpec = LottieCompositionSpec.JsonString(it),
                    useImage = true,
                    onClick = { onHelpClick() }
                )
            }
        }
    }
}