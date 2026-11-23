package com.localpk.app.features.search.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued

import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.menu_drop_down
import dealcart.composeapp.generated.resources.trending_search
import org.jetbrains.compose.resources.painterResource

@Composable
fun TrendingSearch(
    trendingSearchLiveData: List<String>,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trending Searches \uD83D\uDD25",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = TextSubdued,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        LazyHorizontalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            rows = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = true
        ) {
            items(trendingSearchLiveData) { data ->
                TrendingSearchItem(text = data, onClick = { onClick(data) })
            }
        }
    }
}

@Composable
private fun TrendingSearchItem(
    text: String,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .border(
                BorderStroke(1.dp, color = BorderPrimaryDefault),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable {
                onClick(text)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.trending_search),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = TextStyle(
                fontFamily = PoppinsFontFamily(),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSubdued
            )
        )
    }
} 