package com.localpk.app.features.search.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.localpk.app.features.productlist.data.ProductUiData

import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.IconDanger
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.TextSubduedHalf
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.ic_back_arrow_black
import dealcart.composeapp.generated.resources.ic_search_bar_home_screen
import org.jetbrains.compose.resources.painterResource


@Composable
fun TopNavbar(
    isLoading: Boolean,
    productsLiveData: List<ProductUiData>,
    searchText: String,
    onBackClick: () -> Unit,
    onSearchChanged: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, top = 6.dp, end = 2.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(all = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp),
            colors = IconButtonDefaults.iconButtonColors(containerColor = TextSubdued)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back_arrow_black),
                contentDescription = null,
                tint = Color.White
            )
        }
        SearchInput(
            isLoading = isLoading,
            productsLiveData = productsLiveData,
            query = searchText,
            onQueryChanged = {
                onSearchChanged(it)
            },
            onSearch = {
                onSearch(searchText)
            }
        )
    }
}

@Composable
private fun SearchInput(
    isLoading: Boolean,
    productsLiveData: List<ProductUiData>,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    BasicTextField(
        modifier = Modifier
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = BorderPrimaryDefault
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = SurfaceBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
            .focusRequester(focusRequester),
        value = query,
        onValueChange = onQueryChanged,
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 13.sp,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Bold,
            color = if (productsLiveData.isEmpty() && query.length > 2 && isLoading.not()) IconDanger else  TextSubduedHalf
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearch()
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search DealCart",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Bold,
                                color = TextSubduedHalf
                            )
                        )
                    }
                    innerTextField()
                }
                Image(
                    painter = painterResource(Res.drawable.ic_search_bar_home_screen),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    )

    // Request focus automatically when the screen appears
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
} 