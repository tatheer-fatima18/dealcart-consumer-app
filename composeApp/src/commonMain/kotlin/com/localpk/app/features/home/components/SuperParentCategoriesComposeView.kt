package com.localpk.app.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.all
import dealcart.composeapp.generated.resources.all_super_parent
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SuperParentCategoriesComposeView(
    selectedSuperParentCategory: CategoryUiModel?,
    superParentCategories: List<CategoryUiModel>,
    onSuperParentClick: (CategoryUiModel) -> Unit,
    onAllClick: () -> Unit,
    topColor: Color,
    tileBg: Color,
    animatedHeight: Dp,
    categoryColour: Color
) {
    Column(modifier = Modifier.background(color = topColor)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(start = 2.dp)
        ) {
            SuperParentCategoriesComponent(
                superParentCategories = superParentCategories,
                selectedSuperParentCategory = selectedSuperParentCategory,
                tileBg = tileBg,
                animatedHeight = animatedHeight,
                categoryColour = categoryColour,
                onSuperParentClick = onSuperParentClick,
                onAllClick = onAllClick
            )
        }
    }
}

@Composable
fun SuperParentCategoriesComponent(
    superParentCategories: List<CategoryUiModel>,
    selectedSuperParentCategory: CategoryUiModel?,
    tileBg: Color,
    animatedHeight: Dp,
    categoryColour: Color,
    onSuperParentClick: (CategoryUiModel) -> Unit,
    onAllClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SuperParentCategoryAllComponent(
                selectedSuperParentCategory = selectedSuperParentCategory,
                tileBg = tileBg,
                onClick = onAllClick,
                animatedHeight = animatedHeight
            )
        }
        itemsIndexed(superParentCategories) { _, category ->
            SuperParentCategoryItemComponent(
                data = category,
                selectedSuperParentCategory = selectedSuperParentCategory,
                tileBg = tileBg,
                onSuperParentCategoryClick = onSuperParentClick,
                animatedHeight = animatedHeight,
                categoryColour = categoryColour
            )
        }
    }
}

@Composable
fun SuperParentCategoryAllComponent(
    selectedSuperParentCategory: CategoryUiModel?,
    tileBg: Color,
    onClick: () -> Unit = {},
    animatedHeight: Dp
) {
    Box(
        modifier = Modifier
            .height(animatedHeight)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .then(
                if (animatedHeight == 32.dp) {
                    Modifier
                        .width(74.dp)
                } else {
                    Modifier
                        .width(84.dp)

                }
            )
            .background(tileBg)
    ) {
        if (animatedHeight == 32.dp) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = if (selectedSuperParentCategory?.id == null) Color.White else tileBg
                    )
                    .clickable {
                        onClick()
                    }
                    .padding(horizontal = 2.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.all_super_parent),
                    contentDescription = null,
                    modifier = Modifier
                        .width(36.dp)
                        .height(36.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.all),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 10.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = if (selectedSuperParentCategory?.id == null) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selectedSuperParentCategory?.id == null) ColorPrimary else TextSubdued,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = if (selectedSuperParentCategory?.id == null) Color.White else tileBg
                    )
                    .clickable {
                        onClick()
                    }
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.all_super_parent),
                    contentDescription = null,
                    modifier = Modifier
                        .width(44.dp)
                        .height(44.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                    text = stringResource(Res.string.all),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 10.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = if (selectedSuperParentCategory?.id == null) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selectedSuperParentCategory?.id == null) ColorPrimary else TextSubdued,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
@Composable
fun SuperParentCategoryItemComponent(
    data: CategoryUiModel,
    selectedSuperParentCategory: CategoryUiModel?,
    tileBg: Color,
    onSuperParentCategoryClick: (CategoryUiModel) -> Unit,
    categoryColour: Color,
    animatedHeight: Dp
) {
    Box(
        modifier = Modifier
            .then(
                if (animatedHeight == 32.dp) {
                    Modifier
                        .width(118.dp)
                } else {
                    Modifier
                        .width(96.dp)
                }
            )
            .height(animatedHeight)
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(tileBg)
    ) {
        if (animatedHeight == 32.dp) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = if (data.id == selectedSuperParentCategory?.id) Color.White else tileBg
                    )
                    .clickable {
                        onSuperParentCategoryClick(data)
                    }
                    .padding(horizontal = 2.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                loadImagePainterWithValidation(
                    imageUrl = data.imageUrl,
                    fallbackRes = Res.drawable.ic_dealcart_placeholder,
                    contentDescription = null,
                    modifier = Modifier
                        .width(36.dp)
                        .height(36.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    modifier = Modifier,
                    text = data.name,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 10.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = if (data.id == selectedSuperParentCategory?.id) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (data.id == selectedSuperParentCategory?.id) categoryColour else TextSubdued,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = if (data.id == selectedSuperParentCategory?.id) Color.White else tileBg
                    )
                    .clickable {
                        onSuperParentCategoryClick(data)
                    }
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                loadImagePainterWithValidation(
                    imageUrl = data.imageUrl,
                    fallbackRes = Res.drawable.ic_dealcart_placeholder,
                    contentDescription = null,
                    modifier = Modifier
                        .width(44.dp)
                        .height(44.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                    text = data.name,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 10.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = if (data.id == selectedSuperParentCategory?.id) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (data.id == selectedSuperParentCategory?.id) categoryColour else TextSubdued,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
} 