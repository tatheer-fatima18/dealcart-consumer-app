package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextGray
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.mubarak_ho_
import dealcart.composeapp.generated.resources.one_last_step_order_confirmed
import dealcart.composeapp.generated.resources.one_last_step_order_confirmed_second_line
import org.jetbrains.compose.resources.stringResource

@Composable
fun CongratulationsSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = stringResource(Res.string.mubarak_ho_),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            fontFamily = PoppinsFontFamily(),
            textAlign = TextAlign.Center,
            style = TextStyle(
                lineHeight = 14.sp
            ),
            color = TextGray
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = stringResource(Res.string.one_last_step_order_confirmed),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            fontFamily = PoppinsFontFamily(),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            color = TextGray
        )
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = stringResource(Res.string.one_last_step_order_confirmed_second_line),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            fontFamily = PoppinsFontFamily(),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            color = TextGray
        )
    }
}