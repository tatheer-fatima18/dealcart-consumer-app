package com.localpk.app.features.user_location.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.Grey50
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_gps
import org.jetbrains.compose.resources.painterResource

@Composable
fun GetCurrentLocationView(
    onGetLocationClicked: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                onGetLocationClicked()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                color = ColorPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(Res.drawable.ic_gps),
                contentDescription = null,
                tint = ColorPrimary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
            text = when {
                isLoading -> "Getting your location..."
                else -> "Get my current location"
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFontFamily(),
                color = ColorPrimary
            )
        )

        if (!isLoading) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Grey50
            )
        }
    }
} 