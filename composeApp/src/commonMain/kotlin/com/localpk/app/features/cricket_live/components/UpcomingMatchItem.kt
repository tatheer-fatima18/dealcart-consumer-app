package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadComposableAsyncImage
import com.localpk.app.features.cricket_live.data.UpcomingMatchesUiData
import com.localpk.app.theming.ColorTermCondition
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.VersesScoreBg
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.pakistan_logo
import dealcart.composeapp.generated.resources.vs
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun UpcomingMatchItem(
    match: UpcomingMatchesUiData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                loadComposableAsyncImage(
                    imageUrl = match.teams.flag1,
                    fallbackRes = Res.drawable.pakistan_logo,
                    modifier = Modifier
                        .size(20.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = match.teams.name1,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = match.startAt,
                        fontSize = 9.sp,
                        color = Color.Black,
                        fontFamily = PoppinsFontFamily()
                    )
                    Text(
                        text = match.startDate,
                        fontSize = 10.sp,
                        color = Color.Black,
                        fontFamily = PoppinsFontFamily()
                    )

                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.vs),
                    fontSize = 10.sp,
                    color = ColorTermCondition,
                    fontFamily = PoppinsFontFamily(),
                    modifier = Modifier
                        .background(
                            color = VersesScoreBg,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 6.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                loadComposableAsyncImage(
                    imageUrl = match.teams.flag2,
                    fallbackRes = Res.drawable.pakistan_logo,
                    modifier = Modifier
                        .size(20.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = match.teams.name2,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontFamily = PoppinsFontFamily(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}