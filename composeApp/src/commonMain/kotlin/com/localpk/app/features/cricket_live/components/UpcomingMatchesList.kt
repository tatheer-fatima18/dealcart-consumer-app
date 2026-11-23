package com.localpk.app.features.cricket_live.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.features.cricket_live.data.UpcomingMatchesUiData

@Composable
fun UpcomingMatchesList(
    matches: List<UpcomingMatchesUiData>,
    onMatchClick: (UpcomingMatchesUiData) -> Unit = {}
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight().padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(matches) { match ->
            UpcomingMatchItem(
                match = match,
                onClick = { onMatchClick(match) }
            )
        }
    }
}


