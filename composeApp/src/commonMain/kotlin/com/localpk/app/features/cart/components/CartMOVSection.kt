package com.localpk.app.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.CartMOVSheetArgs
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.grocery_k_bgair_checkout_karien
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.DealcartGreen
import com.localpk.app.theming.ErrorRedColor
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.ic_cart_empty_mov
import dealcart.composeapp.generated.resources.mazeed_grocery_khareedin
import dealcart.composeapp.generated.resources.mazeed_grocery_khareedin_btn

@Composable
fun CartMOVSection(
    argsData: CartMOVSheetArgs,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    if (argsData.feeNeeded > 0) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(Res.drawable.ic_cart_empty_mov),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Fit
            )

            val description = "Almost there! Add Rs. ${argsData.feeNeeded} more to place your grocery order."

            val styledText = buildAnnotatedString {
                val green = DealcartGreen
                val red = ErrorRedColor

                append(description)

                description.indexOf("free delivery").takeIf { it >= 0 }?.let {
                    addStyle(SpanStyle(color = green), it, it + "free delivery".length)
                }

                val feeText = "Rs. ${argsData.feeNeeded}"
                description.indexOf(feeText).takeIf { it >= 0 }?.let {
                    addStyle(SpanStyle(color = red), it, it + feeText.length)
                }
            }

            Text(
                text = styledText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFontFamily(),
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Text(
                text = stringResource(Res.string.mazeed_grocery_khareedin),
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                color = TextSubduedHalf,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Button(
                    onClick = {
                        onDismiss()
                        onConfirm()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonSurfaceBrand)
                ) {
                    Text(
                        text = stringResource(Res.string.mazeed_grocery_khareedin_btn).uppercase(),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 14.sp,
                        letterSpacing = 0.02.em
                    )
                }

                if (argsData.nonGroceryCheck == true) {
                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            onCancel()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, ButtonSurfaceBrand),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text(
                            text = stringResource(Res.string.grocery_k_bgair_checkout_karien).uppercase(),
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PoppinsFontFamily(),
                            fontSize = 14.sp,
                            letterSpacing = 0.02.em,
                            color = ButtonSurfaceBrand
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
