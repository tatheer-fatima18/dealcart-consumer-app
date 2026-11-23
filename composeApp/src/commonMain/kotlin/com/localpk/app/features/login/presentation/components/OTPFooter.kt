package com.localpk.app.features.login.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.login.components.LoginButton
import com.localpk.app.theming.CartAmountColor90
import com.localpk.app.theming.Pinkish
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.start_shopping
import org.jetbrains.compose.resources.stringResource

@Composable
fun OTPFooter(
    isOTPIncorrect: Boolean,
    verificationSuccess: Boolean,
    onVerifyOtp: () -> Unit,
    onTermsClick: () -> Unit,
    errorMessage: String
) {
    if (isOTPIncorrect) {
        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 64.dp).padding(horizontal = 16.dp)
                .fillMaxWidth(),
            style = TextStyle(
                color = Pinkish,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontFamily = PoppinsFontFamily()
            ),
        )
    }

    LoginButton(
        buttonValue = stringResource(Res.string.start_shopping),
        modifier = Modifier.padding(top = 50.dp),
        onClick = onVerifyOtp,
        enabled = verificationSuccess
    )

    val text = buildAnnotatedString {
        append("By continuing, you agree to our ")
        pushStyle(
            SpanStyle(
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline
            )
        )
        append("Terms of Service & Privacy")
    }

    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 150.dp)
            .clickable { onTermsClick() },
        textAlign = TextAlign.Center,
        style = TextStyle(
            color = CartAmountColor90,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontFamily = PoppinsFontFamily()
        ),
    )
} 