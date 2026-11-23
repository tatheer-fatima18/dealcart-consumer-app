package com.localpk.app.features.wallet.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.features.rewards.data.CoinsHistoryListUiData
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf

@Composable
fun TransactionList(
    title: String,
    subTitle: String,
    onClick: () -> Unit,
    walletHistory: List<CoinsHistoryListUiData>,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()
    var hasTriggeredLoadMore by remember { mutableStateOf(false) }
    var previousTotalItemsCount = 0


    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val currentTotalItemsCount = layoutInfo.totalItemsCount

                if (lastVisibleItemIndex != null && currentTotalItemsCount > 0) {
                    if (lastVisibleItemIndex >= currentTotalItemsCount - 8 && !hasTriggeredLoadMore) {
                        onLoadMore()
                        hasTriggeredLoadMore = true
                    }
                }

                if (currentTotalItemsCount > previousTotalItemsCount) {
                    hasTriggeredLoadMore = false
                    previousTotalItemsCount = currentTotalItemsCount
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 14.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier, verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = title,
                color = TextSubdued,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFontFamily()
            )

            Spacer(modifier = Modifier.weight(.1f))

            Text(
                text = subTitle,
                modifier = Modifier.clickable { onClick() },
                color = TextSubduedHalf,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFontFamily()
            )

        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(walletHistory) { _, it ->
                TransactionItem(it)
            }
        }
    }
}