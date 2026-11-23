package com.localpk.app.features.checkout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PURPLE_COLOR
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun DeliveryOptionItem(
    icon: DrawableResource,
    title: String,
    subtext: String,
    price: String,
    selected: Boolean,
    onClick: () -> Unit,
    isIconNeeded: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .border(
                1.dp,
                BorderPrimaryDefault,
                shape = RoundedCornerShape(10.dp)
            )
            .then(
                if (isIconNeeded) Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 13.dp
                )
                else
                    Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isIconNeeded) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PoppinsFontFamily(),
                    color = TextSubdued
                )
                Text(
                    text = subtext,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily(),
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = price,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFontFamily(),
                color = TextSubdued
            )

            Spacer(modifier = Modifier.width(2.dp))

            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PURPLE_COLOR,
                    unselectedColor = BorderPrimaryDefault
                )
            )
        }
    }
}
