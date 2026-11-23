package com.localpk.app.features.address.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_back_arrow_black
import dealcart.composeapp.generated.resources.ic_no_address_image
import dealcart.composeapp.generated.resources.no_address_added
import dealcart.composeapp.generated.resources.no_address_added_to_this_account
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddressNotFoundScreen(onClick: () -> Unit) {
    Image(painter = painterResource(Res.drawable.ic_back_arrow_black),
        contentDescription = null,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_no_address_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(8.dp)
        )

        Text(
            text = stringResource(Res.string.no_address_added),
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily(),
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = stringResource(Res.string.no_address_added_to_this_account),
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontFamily = PoppinsFontFamily(),
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}