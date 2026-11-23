package com.localpk.app.features.bottomsheet.components

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
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import com.localpk.app.features.bottomsheet.data.GeneralBottomSheetUiData
import com.localpk.app.theming.ButtonSurfaceBrand
import com.localpk.app.theming.TextSubduedHalf
import org.jetbrains.compose.resources.stringResource

@Composable
fun GeneralBottomSheetContent(
    args: GeneralBottomSheetUiData
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
            .padding(top = 20.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(args.title),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFontFamily(),
            color = Color.Black,
            letterSpacing = (-0.5).sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(args.description),
            fontSize = 14.sp,
            fontFamily = PoppinsFontFamily(),
            color = TextSubduedHalf,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(args.secondaryTitle),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = PoppinsFontFamily(),
            color = Color.Black,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(args.secondaryDescription),
            fontSize = 14.sp,
            fontFamily = PoppinsFontFamily(),
            color = TextSubduedHalf,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    args.onDismiss()
                    args.confirmListener()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonSurfaceBrand)
            ) {
                Text(
                    text = stringResource(args.positiveButton),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 14.sp,
                )
            }

            OutlinedButton(
                onClick = {
                    args.onDismiss()
                    args.cancelListener()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, ButtonSurfaceBrand),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text(
                    text = stringResource(args.negativeButton),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 14.sp,
                    color = ButtonSurfaceBrand
                )
            }
        }
    }
}
