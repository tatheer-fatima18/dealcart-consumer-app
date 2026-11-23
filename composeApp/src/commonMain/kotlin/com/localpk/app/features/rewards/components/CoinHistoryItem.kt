package com.localpk.app.features.rewards.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.rewards.data.CoinsHistoryListUiData
import com.localpk.app.features.rewards.data.TransactionType
import com.localpk.app.theming.CoinGrey
import com.localpk.app.theming.ErrorRed80
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.ProfileTextGray
import com.localpk.app.theming.Yellow
import com.localpk.app.utils.EMPTY_STRING
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_down_linear_arrow
import dealcart.composeapp.generated.resources.ic_earning_coins
import dealcart.composeapp.generated.resources.ic_time_expire
import dealcart.composeapp.generated.resources.ic_up_linear_arrow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CoinsHistoryItem(data: CoinsHistoryListUiData) {
    var transactionType = EMPTY_STRING
    var coinIconColor: Color = Yellow
    var icon: DrawableResource = Res.drawable.ic_down_linear_arrow
    when {
        data.transactionType == TransactionType.CREDIT.key -> {
            transactionType = "Coins Added"
            coinIconColor = Yellow
            icon = Res.drawable.ic_down_linear_arrow
        }

        data.transactionType == TransactionType.DEBIT.key && data.transactionDetailType != TransactionType.EXPIRED.key -> {
            transactionType = "Coins Deducted"
            coinIconColor = ErrorRed80
            icon = Res.drawable.ic_up_linear_arrow
        }

        data.transactionType == TransactionType.ON_HOLD.key || data.transactionDetailType == TransactionType.EXPIRED.key -> {
            transactionType = "Coins On Hold"
            coinIconColor = CoinGrey
            icon = Res.drawable.ic_time_expire
        }
    }
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(45.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transactionType,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily(),
                    textAlign = TextAlign.Center,
                    color = ProfileTextGray
                ),
            )
            Text(
                text = data.transactionDetailType.split("_").joinToString(" ")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = PoppinsFontFamily(),
                    textAlign = TextAlign.Center,
                    color = ProfileTextGray
                )
            )
        }
        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            painter = painterResource(Res.drawable.ic_earning_coins),
            contentDescription = null,
            tint = coinIconColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = data.formattedCoinAmount,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFontFamily(),
                textAlign = TextAlign.Center,
                color = ProfileTextGray
            ),
        )
    }
}