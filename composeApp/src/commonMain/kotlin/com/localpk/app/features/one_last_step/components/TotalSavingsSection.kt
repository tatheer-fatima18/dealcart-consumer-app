package com.localpk.app.features.one_last_step.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SavingsBgColor
import com.localpk.app.theming.SavingsStrokeColor
import com.localpk.app.theming.TextGray
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_savings
import dealcart.composeapp.generated.resources.total_bachat
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TotalSavingsSection(formattedDiscountTotal: String) {
    Card(
        modifier = Modifier.height(44.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = SavingsBgColor),
        border = BorderStroke(1.dp, color = SavingsStrokeColor),
        shape = RoundedCornerShape(30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painterResource(Res.drawable.ic_savings), "Savings")
            Spacer(Modifier.width(16.dp))
            Text(
                text = stringResource(Res.string.total_bachat),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                overflow = TextOverflow.Ellipsis,
                color = TextGray
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = formattedDiscountTotal,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily(),
                overflow = TextOverflow.Ellipsis,
                color = TextGray
            )
        }
    }
}