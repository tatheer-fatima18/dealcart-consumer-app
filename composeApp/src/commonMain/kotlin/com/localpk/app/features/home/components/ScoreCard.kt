package com.localpk.app.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.features.cricket_live.data.CricketSwimlaneUiData
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.theming.ColorPrimary

@Composable
fun ScoreCardView(
    match: CricketSwimlaneUiData,
    onClick: () -> Unit,
    mainSponsor: PoweredByDetailUiData?
) {
    val cardShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 10.dp,
        bottomEnd = 10.dp
    )
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .padding(top = 8.dp)
    ) {
        // Blue banner above the card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(
                    color = ColorPrimary,
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        ) {
            MatchInfoHeader(mainSponsor = mainSponsor)
        }
        
        // White card below the banner
        Card(
            modifier = Modifier
                .clickable { onClick() },
            shape = cardShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(cardShape)
                    .padding(1.dp)
            ) {
                LiveMatchContent(
                    match = match
                )
            }
        }
    }
}
