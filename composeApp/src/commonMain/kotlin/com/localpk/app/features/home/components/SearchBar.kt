package com.localpk.app.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.AppTypography
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.default
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.search
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchBar(
    modifier: Modifier,
    placeholder: String? = "Search DealCart"
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = placeholder.default(),
            style = AppTypography().bodyMedium.copy(
                lineHeight = 14.sp,
                color = TextSubduedHalf,
                fontWeight = FontWeight.Bold
            )
        )
        Icon(
            painter = painterResource(Res.drawable.search),
            contentDescription = null,
            tint = TextSubduedHalf
        )
    }
}