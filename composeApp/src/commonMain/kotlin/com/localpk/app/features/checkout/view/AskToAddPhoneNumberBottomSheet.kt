package com.localpk.app.features.checkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.add_secondary_phone_number
import dealcart.composeapp.generated.resources.add_secondary_phone_number_button
import dealcart.composeapp.generated.resources.add_secondary_phone_number_text
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

class AskToAddPhoneNumberBottomSheet : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<CartCheckoutViewModel>()
        val listener by viewModel.addPhoneNumberListener.collectAsState()
        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = stringResource(Res.string.add_secondary_phone_number),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = ColorPrimaryText, // address_text equivalent
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    letterSpacing = 0.02.em
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description text
                Text(
                    text = stringResource(Res.string.add_secondary_phone_number_text),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = TextSubdued, // subAddress equivalent
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    letterSpacing = 0.02.em,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Button
                Button(
                    onClick = {
                        listener?.invoke()
                        viewModel.updateAskToAddPhoneNumberBottomSheetListener(null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp), // Equivalent to paddingVertical = 10dp
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorPrimary // bg_primary_rounded_7 equivalent
                    ),
                    shape = RoundedCornerShape(7.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.add_secondary_phone_number_button),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
} 