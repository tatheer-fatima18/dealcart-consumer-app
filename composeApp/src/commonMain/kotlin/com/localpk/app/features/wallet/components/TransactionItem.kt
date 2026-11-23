package com.localpk.app.features.wallet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.rewards.data.CoinsHistoryListUiData
import com.localpk.app.features.rewards.data.TransactionDetailType
import com.localpk.app.features.rewards.data.TransactionType
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.theming.TextSuccess
import com.localpk.app.utils.formatDate
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ellipse
import dealcart.composeapp.generated.resources.expired_
import dealcart.composeapp.generated.resources.order_Placed
import dealcart.composeapp.generated.resources.order_icon
import dealcart.composeapp.generated.resources.refund_icon
import dealcart.composeapp.generated.resources.refund_processed
import dealcart.composeapp.generated.resources.top_up_
import dealcart.composeapp.generated.resources.transaction_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionItem(data: CoinsHistoryListUiData) {
    val transactionType: StringResource
    val icon: DrawableResource
    val description: String
    when (data.getTransactionDetailType()) {
        TransactionDetailType.ORDER -> {
            icon = Res.drawable.order_icon
            transactionType = Res.string.order_Placed
            description = "Payment processed for Order# ${data.transactionDetailReferenceNumber}"
        }

        TransactionDetailType.REFUND -> {
            icon = Res.drawable.refund_icon
            transactionType = Res.string.refund_processed
            description =
                "Refund for ${data.getRefundReason()}"
        }

        TransactionDetailType.TOP_UP -> {
            icon = Res.drawable.transaction_icon
            transactionType = Res.string.top_up_
            description = "Added via ${data.transactionDetailMetadata?.topUpMethod}"
        }

        else -> {
            icon = Res.drawable.transaction_icon
            transactionType = Res.string.expired_
            description = "Your balance is expired"
        }
    }
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .width(35.dp)
                .height(35.dp)
                .background(
                    SurfaceBackground,
                    shape = RoundedCornerShape(4.dp, 4.dp, 4.dp, 4.dp)
                )
                .clip(shape = RoundedCornerShape(4.dp, 4.dp, 4.dp, 4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .width(13.dp)
                    .height(12.dp),
                painter = painterResource(icon),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(transactionType),
                    color = TextSubdued,
                    fontSize = 14.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PoppinsFontFamily(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(10.dp))

                Image(
                    painter = painterResource(Res.drawable.ellipse),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(5.dp)
                        .height(5.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = data.coinsDate.formatDate(),
                    modifier = Modifier
                        .padding(top = 2.dp),
                    color = TextSubdued,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PoppinsFontFamily(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(.1f))

                Text(
                    text = if (data.transactionType == TransactionType.CREDIT.key)
                        data.formattedCoinAmount
                    else data.formattedCoinAmount,
                    color = if (data.transactionType == TransactionType.CREDIT.key) TextSuccess else TextSubdued,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = PoppinsFontFamily(),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = description,
                color = TextSubduedHalf,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = PoppinsFontFamily(),
                textAlign = TextAlign.Center
            )
        }
    }
}
