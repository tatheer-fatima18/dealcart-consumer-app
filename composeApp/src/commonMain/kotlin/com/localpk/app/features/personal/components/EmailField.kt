package com.localpk.app.features.personal.components

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.DCLightGrey
import com.localpk.app.theming.DCPink
import com.localpk.app.theming.DcRed
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray
import com.localpk.app.theming.Red
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.email
import dealcart.composeapp.generated.resources.email_sahi_daraj_karin
import dealcart.composeapp.generated.resources.ic_error_cross
import dealcart.composeapp.generated.resources.ic_verification_checked
import dealcart.composeapp.generated.resources.ic_verification_unchecked
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmailField(
    value: String,
    enabled: Boolean,
    isVerified: Boolean,
    hasError: Boolean,
    onValueChange: (String) -> Unit
) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(Res.string.email),
            style = TextStyle(
                fontFamily = PoppinsFontFamily(),
                color = ProfileTextGray,
                fontSize = 17.sp
            ),
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 65.dp), horizontalAlignment = Alignment.Start
        ) {
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                ) {
                    BasicTextField(
                        value = value,
                        cursorBrush = SolidColor(DCPink),
                        enabled = enabled,
                        singleLine = true,
                        onValueChange = onValueChange,
                        textStyle = TextStyle(
                            fontFamily = PoppinsFontFamily(),
                            fontSize = 15.sp,
                            color = if (hasError) Red else ColorPrimaryText
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    if (isVerified && value.isNotEmpty()) {
                        Image(
                            painter = painterResource(Res.drawable.ic_verification_checked),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(Res.drawable.ic_verification_unchecked),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                HorizontalDivider(
                    color = if (hasError) DcRed else DCLightGrey,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                )
            }
            if (hasError) {
                Spacer(
                    modifier = Modifier
                        .height(6.dp)
                        .width(3.dp)
                )
                Row {
                    Image(
                        painter = painterResource(Res.drawable.ic_error_cross),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(Res.string.email_sahi_daraj_karin),
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = DcRed
                        ),
                    )
                }
            }
        }
    }
}