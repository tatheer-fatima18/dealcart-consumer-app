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
import com.localpk.app.theming.CartAmountColor
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.login_continue
import org.jetbrains.compose.resources.stringResource

@Composable
fun PhoneInputFooter(
    isContinueEnabled: Boolean,
    onContinueClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    LoginButton(
        buttonValue = stringResource(Res.string.login_continue),
        modifier = Modifier.padding(top = 1.dp),
        onClick = onContinueClick,
        enabled = isContinueEnabled
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
            .clickable { onTermsClick() },
        textAlign = TextAlign.Center,
        style = TextStyle(
            color = CartAmountColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontFamily = PoppinsFontFamily()
        )
    )
} 