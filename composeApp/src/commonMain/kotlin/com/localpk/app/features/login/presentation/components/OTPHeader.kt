package com.localpk.app.features.login.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.Black80
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ResendOtpTextColor
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.change_number
import dealcart.composeapp.generated.resources.dc_icon_login
import dealcart.composeapp.generated.resources.login_heading
import dealcart.composeapp.generated.resources.resend_in
import dealcart.composeapp.generated.resources.we_sent_verification
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OTPHeader(
    phoneNumber: String,
    resendButtonState: Pair<Boolean, String>,
    onBackClick: () -> Unit,
    onResendClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(73.dp)
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ),
            painter = painterResource(Res.drawable.dc_icon_login),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.login_heading),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = ColorPrimary,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal,
                fontFamily = PoppinsFontFamily()
            ),
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = stringResource(Res.string.we_sent_verification),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(),
            style = TextStyle(
                color = Black80,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontFamily = PoppinsFontFamily()
            ),
        )

        Row {
            Text(
                text = phoneNumber,
                modifier = Modifier.padding(end = 4.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Black80,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    fontFamily = PoppinsFontFamily()
                ),
            )
            Text(
                text = stringResource(Res.string.change_number),
                modifier = Modifier.clickable { onBackClick() },
                style = TextStyle(
                    color = ColorPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal,
                    fontFamily = PoppinsFontFamily()
                ),
            )
        }

        Text(
            text = if (resendButtonState.first) stringResource(Res.string.resend_in) else resendButtonState.second,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onResendClick() }
                .fillMaxWidth(),
            style = TextStyle(
                color = if (resendButtonState.first) ColorPrimary else ResendOtpTextColor,
                fontSize = 14.sp,
                fontWeight = if (resendButtonState.first) FontWeight.SemiBold else FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontFamily = PoppinsFontFamily()
            ),
        )
    }
} 