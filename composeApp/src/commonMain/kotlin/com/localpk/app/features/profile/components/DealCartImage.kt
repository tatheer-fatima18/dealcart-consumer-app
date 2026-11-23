package com.localpk.app.features.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.dealcart_logo
import dealcart.composeapp.generated.resources.ic_deal_cart
import org.jetbrains.compose.resources.painterResource

@Composable
fun DealCartImage(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Image(
            painter = painterResource(Res.drawable.dealcart_logo),
            contentDescription = "DealCart",
            modifier = Modifier
                .padding(top = 40.dp)
                .clickable { onClick() }
        )
    }
}