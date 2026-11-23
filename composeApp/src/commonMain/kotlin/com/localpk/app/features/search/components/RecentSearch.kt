package com.localpk.app.features.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.menu_drop_down
import org.jetbrains.compose.resources.painterResource


@Composable
fun RecentSearch(
    searchLiveData: List<String>,
    onClearAll: () -> Unit,
    onClickChange: (String) -> Unit,
    onClickRemove: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "History",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = TextSubdued,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.clickable {
                    onClearAll()
                },
                text = "Clear All",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = IconDanger
                )
            )
        }
        Column {
            searchLiveData.forEach { data ->
                RecentSearchItem(text = data,
                    onClickRemove = { onClickRemove(data) },
                    onClickChange = { onClickChange(data) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        HorizontalDivider()
    }
}

@Composable
private fun RecentSearchItem(
    text: String,
    onClickChange: (String) -> Unit,
    onClickRemove: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onClickChange(text)
                },
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                color = TextSubdued
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            modifier = Modifier.clickable {
                onClickRemove(text)
            },
            painter = painterResource(Res.drawable.menu_drop_down),
            contentDescription = null,
            tint = TextSubdued
        )
    }
} 