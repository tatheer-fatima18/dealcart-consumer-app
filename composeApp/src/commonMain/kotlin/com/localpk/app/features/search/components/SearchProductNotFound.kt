package com.localpk.app.features.search.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.no_result_found
import dealcart.composeapp.generated.resources.no_result_found_desc
import dealcart.composeapp.generated.resources.no_search_item
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun SearchProductNotFound() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.no_search_item),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(Res.string.no_result_found),
            style = TextStyle(
                color = TextSubdued,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFontFamily(),
                textAlign = TextAlign.Center,
            )
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(Res.string.no_result_found_desc),
            style = TextStyle(
                color = TextSubduedHalf,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = PoppinsFontFamily(),
                textAlign = TextAlign.Center,
            )
        )
        HorizontalDivider()
    }
} 