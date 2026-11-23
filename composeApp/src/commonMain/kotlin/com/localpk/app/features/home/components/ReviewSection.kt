package com.localpk.app.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.PurpleNew
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_arrow_button
import dealcart.composeapp.generated.resources.ic_close_btn_round
import org.jetbrains.compose.resources.painterResource


@Composable
fun ReviewSection(
    orderIdForReview: Long?,
    startReview: () -> Unit,
    onActionCancelReview: () -> Unit,
    modifier: Modifier
) {
    if (orderIdForReview.isNotNull()) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            color = PurpleNew
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .height(45.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "How was your experience with Order #${orderIdForReview}?",
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                    ),
                    maxLines = 2
                )

                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                        .clickable { onActionCancelReview() },
                    painter = painterResource(Res.drawable.ic_close_btn_round),
                    contentDescription = "Close"
                )

                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                        .clickable { startReview() },
                    painter = painterResource(Res.drawable.ic_arrow_button),
                    contentDescription = "Forward"
                )
            }
        }
    }
}