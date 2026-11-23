package com.localpk.app.features.promo.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.promo.components.PromoCodeItem
import com.localpk.app.features.promo.domain.Promo
import com.localpk.app.features.rewards.components.TopBar
import com.localpk.app.features.share_to_friend.presentation.ShareToFriendScreen
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.how_it_works_label
import dealcart.composeapp.generated.resources.okay_got_it
import dealcart.composeapp.generated.resources.promo_codes
import dealcart.composeapp.generated.resources.terms_and_symbol_conditions
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class PromoDetailsScreen(
    private val promoId: Long = 0L,
    private val promoName: String = EMPTY_STRING,
    private val promoCode: String = EMPTY_STRING,
    private val startDate: String = EMPTY_STRING,
    private val expiryDate: String = EMPTY_STRING,
    private val minOrderValue: Long = 0L,
    private val discountType: String = EMPTY_STRING,
    private val discountValue: Long = 0L,
    private val isActive: Boolean = false,
    private val promoDescription: String = EMPTY_STRING,
    private val termsAndConditions: String = EMPTY_STRING

) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val promo = Promo(
            id = promoId,
            promoName = promoName,
            promoCode = promoCode,
            startDate = startDate,
            expiryDate = expiryDate,
            minOrderValue = minOrderValue,
            discountType = discountType,
            discountValue = discountValue,
            isActive = isActive,
            promoDescription = promoDescription,
            termsAndConditions = termsAndConditions
        )
        Column(modifier = Modifier.fillMaxSize()) {

            TopBar(
                onBackClick = { navigator?.pop() },
                title = stringResource(Res.string.promo_codes),
                onShareClick = {
                    if (sharedPreferencesManager.isLoggedIn()) {
                        navigator?.push(ShareToFriendScreen())
                    } else {
                        navigator?.push(
                            PhoneInputScreen(
                                ScreenSource.SHARE_TO_FRIEND.key
                            )
                        )
                    }
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        PromoCodeItem(
                            promo = promo,
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 2.dp,
                        color = Color(0xFFBDBDBD)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.how_it_works_label),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                letterSpacing = 0.01.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = promo.promoDescription,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF8A8A8A),
                                letterSpacing = 0.06.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = stringResource(Res.string.terms_and_symbol_conditions),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                letterSpacing = 0.01.sp,
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = promo.termsAndConditions,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF8A8A8A),
                                letterSpacing = 0.06.sp
                            )
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .width(330.dp)
                        .height(51.dp)
                        .background(
                            color = Color(0xFF5E6EFF),
                            shape = RoundedCornerShape(size = 98.3.dp)
                        )
                        .clickable {
                            navigator?.pop()
                        }
                        .padding(
                            start = 19.7.dp,
                            top = 11.8.dp,
                            end = 19.7.dp,
                            bottom = 11.8.dp
                        )
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(Res.string.okay_got_it),
                    style = TextStyle(
                        fontSize = 15.7.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.1.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
} 