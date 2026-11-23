package com.localpk.app.features.address.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.addresses
import dealcart.composeapp.generated.resources.ic_back_arrow_black
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TopBar(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.clickable(onClick = { onClick() }),
            painter = painterResource(Res.drawable.ic_back_arrow_black),
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(Res.string.addresses),
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                fontFamily = PoppinsFontFamily(),
                color = ColorPrimaryText,
            )
        }
    }
}