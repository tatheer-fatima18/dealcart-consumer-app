package com.localpk.app.features.personal.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextGray
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.congratulations
import dealcart.composeapp.generated.resources.emailVerifyHouChuki
import dealcart.composeapp.generated.resources.emailVerifyKarien
import dealcart.composeapp.generated.resources.ic_email_verification_pending
import dealcart.composeapp.generated.resources.ic_email_verification_success
import dealcart.composeapp.generated.resources.startShoppingBtn
import dealcart.composeapp.generated.resources.verifyEmail
import dealcart.composeapp.generated.resources.verifyEmailBtn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmailVerificationBottomSheetContent(onClick: () -> Unit, isEmailVerified: Boolean?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 23.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter =
            painterResource(if (isEmailVerified == true) Res.drawable.ic_email_verification_success else Res.drawable.ic_email_verification_pending),
            contentDescription = null,
            modifier = Modifier
                .width(64.dp)
                .height(64.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(
                if (isEmailVerified ==
                    true
                ) Res.string.congratulations else Res.string.verifyEmail
            ),
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = TextGray
            ),
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(
                if (isEmailVerified ==
                    true
                ) Res.string.emailVerifyHouChuki else Res.string.emailVerifyKarien
            ),
            modifier = Modifier.padding(vertical = 12.dp),
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                color = TextGray,
                textAlign = TextAlign.Center
            ),
        )

        Button(
            onClick = { onClick() },
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
                .height(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
            shape = RoundedCornerShape(30.dp),
        ) {
            Text(
                text = stringResource(
                    if (isEmailVerified ==
                        true
                    ) Res.string.startShoppingBtn else Res.string.verifyEmailBtn
                ).uppercase(),
                style = TextStyle(
                    letterSpacing = 1.sp,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}