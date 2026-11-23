package com.localpk.app.features.product_detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.AppTypography
import com.localpk.app.theming.DecorYellow
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.TextPrimary
import com.localpk.app.utils.hour
import com.localpk.app.utils.minutes
import com.localpk.app.utils.seconds
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.baqaya_waqt
import dealcart.composeapp.generated.resources.ic_persons
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FlashDealComponent(
    remainingTime: Long,
    remainingPersons: Int
) {
    val colors = listOf(
        Color(0xFFFF403C),
        Color(0x00FDA34B)
    )

    var tvHours by remember { mutableStateOf("00") }
    var tvMinutes by remember { mutableStateOf("00") }
    var tvSeconds by remember { mutableStateOf("00") }

    val currentTime by rememberUpdatedState(newValue = remainingTime)

    LaunchedEffect(currentTime) {
        var timeLeft = currentTime
        while (timeLeft > 0) {
            timeLeft -= 1000
            tvHours = timeLeft.hour()
            tvMinutes = timeLeft.minutes()
            tvSeconds = timeLeft.seconds()
            delay(1000)
        }
        // Set to 00:00:00 on finish
        tvHours = "00"
        tvMinutes = "00"
        tvSeconds = "00"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(2.dp),
    ) {
        Row(
            Modifier
                .background(color = DecorYellow, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_persons),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "$remainingPersons more person to grab this amazing deal!",
                style =
                AppTypography().bodyMedium.copy(
                    fontSize = 12.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                )
            )
            Row(
                Modifier
                    .background(
                        Brush.linearGradient(colors = colors),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(Res.string.baqaya_waqt)}: ",
                    style = AppTypography().bodyMedium.copy(
                        fontSize = 12.sp,
                        color = Color.White
                    )
                )
                Text(
                    modifier = Modifier
                        .height(26.dp)
                        .width(22.dp)
                        .background(
                            color = IconDanger,
                            RoundedCornerShape(2.dp)
                        )
                        .padding(vertical = 5.dp),
                    text = tvHours,
                    style = AppTypography().bodyMedium.copy(
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = ":",
                    style = AppTypography().bodyMedium.copy(
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    modifier = Modifier
                        .height(26.dp)
                        .width(22.dp)
                        .background(
                            color = IconDanger,
                            RoundedCornerShape(2.dp)
                        )
                        .padding(vertical = 5.dp),
                    text = tvMinutes,
                    style = AppTypography().bodyMedium.copy(
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = ":",
                    style = AppTypography().bodyMedium.copy(
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    modifier = Modifier
                        .height(26.dp)
                        .width(22.dp)
                        .background(
                            color = IconDanger,
                            RoundedCornerShape(2.dp)
                        )
                        .padding(vertical = 5.dp),
                    text = tvSeconds,
                    style = AppTypography().bodyMedium.copy(
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}