package com.localpk.app.features.cart.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.cart.data.OOSBottomSheetArgs
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.Horizontal_Separator
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextGray
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.isNotZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.out_of_stock
import dealcart.composeapp.generated.resources.update_cart
import org.jetbrains.compose.resources.stringResource

@Composable
fun OOSBottomSheetContent(
    argsData: OOSBottomSheetArgs
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.out_of_stock),
                fontFamily = PoppinsFontFamily(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextGray
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        if (argsData.oosItems.isNotNull()) {
            LazyColumn {
                items(argsData.oosItems) { item ->
                    if (item.product?.id.isNotZero())
                        CartOOSProduct(item)
                }
            }
            Divider(
                thickness = 0.5.dp,
                color = Horizontal_Separator,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(ColorPrimary),
                onClick = {
                    argsData.onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 14.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.update_cart).uppercase(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color.White
                )
            }
        }
    }
}