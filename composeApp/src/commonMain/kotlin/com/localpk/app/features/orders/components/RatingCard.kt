package com.localpk.app.features.orders.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.filled_star
import dealcart.composeapp.generated.resources.rate_your_order
import dealcart.composeapp.generated.resources.unfilled_star
import dealcart.composeapp.generated.resources.you_rated
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RatingCard(
    isOrderDelivered: Boolean,
    isOrderRated: Boolean,
    onReviewClick: () -> Unit,
    rating: Float
) {
    if (isOrderDelivered) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(7.5.dp))
                .border(
                    BorderStroke(1.dp, BorderPrimaryDefault),
                    shape = RoundedCornerShape(7.5.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp,
                        vertical = 12.dp
                    )
                    .background(Color.White)
            ) {
                if (!isOrderRated) {
                    Text(
                        text = stringResource(Res.string.rate_your_order),
                        color = ColorPrimaryText,
                        fontSize = 12.sp,
                        letterSpacing = (-0.2).sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.you_rated),
                        color = ColorPrimaryText,
                        fontSize = 11.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(.1f))

                StarRatingBar(
                    rating = rating,
                    onReviewClick = onReviewClick
                )
            }
        }
    }
}

@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float,
    onReviewClick: () -> Unit,
) {
    val density = LocalDensity.current.density
    val starSize = (9f * density).dp
    val starSpacing = (2f * density).dp

    Row(
        modifier = Modifier
            .clickable { onReviewClick() }
            .padding(end = 5.dp)
            .padding(top = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon =
                painterResource(if (isSelected) Res.drawable.filled_star else Res.drawable.unfilled_star)
            Image(
                painter = icon,
                contentDescription = null, modifier = Modifier
                    .width(starSize)
                    .height(starSize)
            )
            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}