package com.localpk.app.features.order_review.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.common_components.CustomCheckbox
import com.localpk.app.common_components.loadImagePainterWithValidation
import com.localpk.app.features.order_review.data.OrderReviewCategoryArgs
import com.localpk.app.features.order_review.data.OrderReviewCategoryReasonUIData
import com.localpk.app.features.order_review.data.OrderReviewCategoryUIData
import com.localpk.app.features.order_review.viewModel.OrderReviewCategoriesListingViewModel
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.theming.TextSubdued
import com.localpk.app.theming.transparent
import com.localpk.app.utils.EMPTY_STRING
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources._5_star_rate_na_kernay_ki_waja
import dealcart.composeapp.generated.resources.how_was_your_experience_with_
import dealcart.composeapp.generated.resources.ic_dealcart_placeholder
import dealcart.composeapp.generated.resources.ic_order_review_star_filled
import dealcart.composeapp.generated.resources.ic_review_sad
import dealcart.composeapp.generated.resources.ik_se_ziada_reason_select_karskte_hain
import dealcart.composeapp.generated.resources.navbutton_black
import dealcart.composeapp.generated.resources.select_category
import dealcart.composeapp.generated.resources.select_reasons
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

class OrderReviewCategoriesListingScreen(
    private val id: Long,
    private val shipmentId: Long,
    private val rating: Int,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val viewModel = koinViewModel<OrderReviewCategoriesListingViewModel>()
        val state by viewModel.state.collectAsState()

        val argsData = OrderReviewCategoryArgs(
            id = id,
            shipmentId = shipmentId,
            rating = rating
        )

        LaunchedEffect(Unit) {
            viewModel.updateArgsAndState(argsData)
        }

        Surface(
            modifier = Modifier.fillMaxSize().imePadding().navigationBarsPadding(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top section with back button and emoji
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Back button
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                if (state.isCategorySelected) {
                                    viewModel.updateIsCategorySelected(false)
                                } else {
                                    navigator.pop()
                                }
                            },
                        painter = painterResource(Res.drawable.navbutton_black),
                        contentDescription = null
                    )

                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(Res.drawable.ic_review_sad),
                        contentDescription = null
                    )

                    // Empty space for alignment
                    Spacer(modifier = Modifier.width(26.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Order number text
                Text(
                    text = "Apka order #${argsData.id}",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 0.02.em,
                    color = ColorPrimaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Review question text
                Text(
                    text = stringResource(Res.string.how_was_your_experience_with_),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.02.em,
                    color = ColorPrimaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Rating bar (read-only)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(5) { index ->
                        Icon(
                            painter = painterResource(Res.drawable.ic_order_review_star_filled),
                            contentDescription = "Star ${index + 1}",
                            tint = if (state.rating > index) ColorPrimary else SurfaceBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Rating text
                Text(
                    text = state.reviewText,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = ColorPrimaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category selection section
                if (!state.isCategorySelected) {
                    // Select category text
                    Text(
                        text = stringResource(Res.string.select_category),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = ColorPrimaryText,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Divider
                    Divider(
                        color = Color(0xFFE4E5ED), // dc_Text_grey equivalent
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Categories list
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(state.categoriesData) { category ->
                            CategoryItem(
                                category = category,
                                onClick = { viewModel.onActionClickCategory(category.id) }
                            )
                        }
                    }
                } else {
                    // Selected category and reasons section
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(
                            text = state.selectedCategoryName,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = ColorPrimaryText
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = stringResource(Res.string.select_reasons),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = ColorPrimaryText
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = stringResource(Res.string.ik_se_ziada_reason_select_karskte_hain),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFF7B7B7B), // profile_text_gray equivalent
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Divider
                    Divider(
                        color = Color(0xFFE4E5ED),
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Reasons list
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.reasonsData ?: emptyList()) { reason ->
                            ReasonItem(
                                reason = reason,
                                onSelected = { isSelected ->
                                    viewModel.onReasonSelected(reason.id, isSelected)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Comment input
                    var commentText by remember { mutableStateOf(state.reasonComment) }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        BasicTextField(
                            value = commentText,
                            onValueChange = {
                                commentText = it
                                viewModel.onReasonCommentChanged(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(16.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontFamily = PoppinsFontFamily(),
                                fontSize = 12.sp,
                                color = Color.Black
                            ),
                            decorationBox = { innerTextField ->
                                if (commentText.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string._5_star_rate_na_kernay_ki_waja),
                                        fontFamily = PoppinsFontFamily(),
                                        fontSize = 12.sp,
                                        color = TextSubdued
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Continue button
                    Button(
                        onClick = { viewModel.onActionContinue(navigator, bottomSheetNavigator) },
                        enabled = state.isContinueButtonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorPrimary,
                            disabledContainerColor = SurfaceBackground
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "AGAY CHALAIN",
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: OrderReviewCategoryUIData,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            loadImagePainterWithValidation(
                imageUrl = if (category.image.isEmpty()
                        .not()
                ) category.image else EMPTY_STRING,
                fallbackRes = Res.drawable.ic_dealcart_placeholder,
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        BorderStroke(1.dp, transparent),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = category.name,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = ColorPrimaryText,
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalDivider(
            color = Color(0xFFE4E5ED),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ReasonItem(
    reason: OrderReviewCategoryReasonUIData,
    onSelected: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCheckbox(
            checked = reason.isSelected,
            onCheckedChange = onSelected,
            size = 20.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = reason.name,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = ColorPrimaryText,
            modifier = Modifier.weight(1f)
        )
    }
} 