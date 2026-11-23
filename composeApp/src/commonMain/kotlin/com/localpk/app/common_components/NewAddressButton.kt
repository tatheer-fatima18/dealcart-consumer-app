package com.localpk.app.common_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.naya_address_darj_karein
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewAddressButton(
    onClick: () -> Unit
) {
    Column {
        Button(
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp
            ),
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp, top = 16.dp)
                .height(56.dp)
        ) {
            Text(
                text = stringResource(Res.string.naya_address_darj_karein),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                maxLines = 2
            )
        }
    }
}