package com.localpk.app.features.product_detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.localpk.app.features.home.components.SearchBar
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.SurfaceBackground
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.navbutton_black
import dealcart.composeapp.generated.resources.search_dealcart
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Header(
    modifier: Modifier,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    onBackClick()
                },
            painter = painterResource(Res.drawable.navbutton_black),
            contentDescription = null
        )
        SearchBar(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = SurfaceBackground,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(1.dp, BorderPrimaryDefault, shape = RoundedCornerShape(8.dp))
                .clickable { onSearchClick() }
                .padding(horizontal = 12.dp, vertical = 12.dp),
            placeholder = stringResource(Res.string.search_dealcart)
        )
    }
}