package com.localpk.app.features.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.features.cricket_live.data.CricketSwimlaneUiData
import com.localpk.app.features.cricket_live.data.PoweredByDetailUiData
import com.localpk.app.theming.ButtonSurfaceBrand
import kotlinx.coroutines.launch

@Composable
fun CricketSection(
    matches: List<CricketSwimlaneUiData>?,
    mainSponsor: PoweredByDetailUiData?,
    cricketPagerState: PagerState,
    onMatchClick: (CricketSwimlaneUiData) -> Unit
) {
    matches?.takeIf { it.isNotEmpty() }?.let {
        val scope = rememberCoroutineScope()

        Column(modifier = Modifier) {
            HorizontalPager(
                state = cricketPagerState,
                modifier = Modifier.wrapContentSize()
            ) { page ->
                ScoreCardView(
                    match = matches[page],
                    onClick = {
                        onMatchClick(matches[page])
                    },
                    mainSponsor = mainSponsor
                )
            }

            if (matches.size > 1) {
                BottomSection(
                    size = matches.size,
                    index = cricketPagerState.currentPage,
                    color = ButtonSurfaceBrand,
                    isMatchIndicator = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    scope.launch {
                        cricketPagerState.animateScrollToPage(
                            (cricketPagerState.currentPage + 1) % matches.size
                        )
                    }
                }
            }
        }
    }
}