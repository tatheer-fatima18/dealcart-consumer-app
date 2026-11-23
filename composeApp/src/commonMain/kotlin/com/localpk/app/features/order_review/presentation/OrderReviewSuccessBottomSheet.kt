package com.localpk.app.features.order_review.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.localpk.app.base.BaseScreen
import com.localpk.app.common_components.AnimatedLottie
import com.localpk.app.features.order_review.data.OrderReviewCategoryArgs
import com.localpk.app.features.order_review.data.OrderReviewSuccessArgs
import com.localpk.app.features.order_review.viewModel.OrderReviewSuccessViewModel
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.PoppinsFontFamily
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.get_support_roman_urdu
import dealcart.composeapp.generated.resources.ic_bad_review
import dealcart.composeapp.generated.resources.ic_bag_order_review_success
import dealcart.composeapp.generated.resources.ic_baseline_close_24
import dealcart.composeapp.generated.resources.message_support
import dealcart.composeapp.generated.resources.okay
import dealcart.composeapp.generated.resources.thank_you_for_your_feedback
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class OrderReviewSuccessBottomSheet(
    private val orderId: Long,
    private val rating: Int,
) : Screen {

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val argsData = OrderReviewSuccessArgs(
            orderId = orderId,
            rating = rating.toLong()
        )

        var jsonString by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            jsonString = Res.readBytes("files/confetti.json").decodeToString()
        }

        // Helper function to handle dismiss action
        val handleDismiss = {
            argsData.continueListener.invoke()
            bottomSheetNavigator.hide()
        }

        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
                .padding(bottom = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close button
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = handleDismiss,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(Res.drawable.ic_baseline_close_24),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (argsData.rating.toInt() == 5) {
                    Box(
                        modifier = Modifier.size(96.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_bag_order_review_success),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )

                        jsonString?.let {
                            AnimatedLottie(
                                lottieSpec = LottieCompositionSpec.JsonString(it),
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    Image(
                        modifier = Modifier.size(96.dp),
                        painter = painterResource(Res.drawable.ic_bad_review),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Main heading based on rating
                if (argsData.rating.toInt() == 5) {
                    Text(
                        text = stringResource(Res.string.thank_you_for_your_feedback),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = ColorPrimaryText,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.message_support),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = ColorPrimaryText,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons based on rating
                if (argsData.rating.toInt() == 5) {
                    // Only continue button for 5-star rating
                    Button(
                        onClick = handleDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorPrimary
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.okay),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                } else {
                    // Support and continue buttons for non-5-star rating
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                argsData.chatListener.invoke()

                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = ColorPrimary
                            ),
                            border = BorderStroke(1.dp, ColorPrimary),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.get_support_roman_urdu),
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = ColorPrimary
                            )
                        }

                        Button(
                            onClick = { handleDismiss() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorPrimary
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.okay),
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
}