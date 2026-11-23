package com.localpk.app.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.localpk.app.features.category.data.CategoryUiModel
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.categories
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max

@Composable
fun CategoriesComponent(
    parentCategories: List<CategoryUiModel>,
    onCategoryClick: (CategoryUiModel) -> Unit,
    categoryColour: Color,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val screenWidth = maxWidth
        val itemMinWidth = 81.dp
        val spacing = 8.dp

        val itemsPerRow = remember(screenWidth) {
            max(1, ((screenWidth + spacing) / (itemMinWidth + spacing)).toInt())
        }

        val rowsPerPage = 2
        val itemsPerPage = itemsPerRow * rowsPerPage
        val totalPages = (parentCategories.size + itemsPerPage - 1) / itemsPerPage

        // Pages
        val pages = parentCategories.chunked(itemsPerPage)

        val lazyListState = rememberLazyListState()

        val flingBehavior = rememberSnapFlingBehavior(lazyListState)
        // derive current page from scroll position
        val currentPage by remember {
            derivedStateOf {
                lazyListState.firstVisibleItemIndex
            }
        }

        Column(modifier = Modifier) {
            Text(
                text = stringResource(Res.string.categories),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    color = TextSubdued,
                )
            )
            Spacer(Modifier.height(8.dp))

            LazyRow(
                state = lazyListState,
                flingBehavior = flingBehavior, // snap page by page
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (totalPages > 1) {
                            Modifier.height(280.dp)
                        } else {
                            Modifier.wrapContentHeight()
                        }
                    )
            ) {
                items(pages.size) { pageIndex ->
                    val pageItems = pages[pageIndex]

                    Column(
                        modifier = Modifier
                            .width(screenWidth)
                            .padding(horizontal = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        repeat(rowsPerPage) { rowIndex ->
                            val rowItems =
                                pageItems.drop(rowIndex * itemsPerRow).take(itemsPerRow)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(spacing),
                            ) {
                                rowItems.forEach { category ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        CategoryItemComponent(
                                            data = category,
                                            onCategoryClick = onCategoryClick,
                                            categoryColour = categoryColour
                                        )
                                    }
                                }

                                if (rowItems.size < itemsPerRow) {
                                    repeat(itemsPerRow - rowItems.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            BottomSection(
                size = totalPages,
                index = currentPage,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                isMatchIndicator = false
            )
        }
    }
}

