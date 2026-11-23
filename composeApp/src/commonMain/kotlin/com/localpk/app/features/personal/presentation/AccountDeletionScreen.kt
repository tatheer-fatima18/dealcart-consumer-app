package com.localpk.app.features.personal.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.address.presentation.AddressListViewModel
import com.localpk.app.features.frequently_ordered.viewModel.FrequentlyOrderedViewModel
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.personal.components.BulletPoint
import com.localpk.app.features.personal.components.DeletionPoint
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.StatusBarIconColor
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.after_deleting_point_1
import dealcart.composeapp.generated.resources.after_deleting_point_2
import dealcart.composeapp.generated.resources.after_deleting_point_3
import dealcart.composeapp.generated.resources.after_deleting_title
import dealcart.composeapp.generated.resources.delete_account
import dealcart.composeapp.generated.resources.delete_account_button
import dealcart.composeapp.generated.resources.delete_account_confirmation
import dealcart.composeapp.generated.resources.icon_delete_container
import dealcart.composeapp.generated.resources.navbutton_black
import dealcart.composeapp.generated.resources.reactivate_account_description
import dealcart.composeapp.generated.resources.reactivate_account_title
import dealcart.composeapp.generated.resources.terms_conditions_point_1
import dealcart.composeapp.generated.resources.terms_conditions_point_2
import dealcart.composeapp.generated.resources.terms_conditions_point_3
import dealcart.composeapp.generated.resources.terms_conditions_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

class AccountDeletionScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val personalViewModel = koinViewModel<PersonalViewModel>()
        val accountDeletionViewModel = koinViewModel<AccountDeletionViewModel>()
        val addressesViewModel = koinViewModel<AddressListViewModel>()
        val frequentlyOrderedViewModel = koinViewModel<FrequentlyOrderedViewModel>()
        StatusBarIconColor(backgroundColor = Color.White)
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .background(color = Color.White).statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterStart)
                            .clickable {
                                navigator.pop()
                            },
                        painter = painterResource(Res.drawable.navbutton_black),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(Res.string.delete_account),
                        color = TextPrimary,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = PoppinsFontFamily(),
                        letterSpacing = 0.03.sp
                    )
                }

                Image(
                    painter = painterResource(Res.drawable.icon_delete_container),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 28.8.dp)
                )

                Spacer(modifier = Modifier.height(13.dp))

                Text(
                    text = stringResource(Res.string.delete_account_confirmation),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 23.4.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                    )
                )

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = stringResource(Res.string.after_deleting_title),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 18.2.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DeletionPoint(
                        number = "1",
                        text = stringResource(Res.string.after_deleting_point_1)
                    )
                    DeletionPoint(
                        number = "2",
                        text = stringResource(Res.string.after_deleting_point_2)
                    )
                    DeletionPoint(
                        number = "3",
                        text = stringResource(Res.string.after_deleting_point_3)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(Res.string.terms_conditions_title),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 18.2.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    BulletPoint(text = stringResource(Res.string.terms_conditions_point_1))
                    BulletPoint(text = stringResource(Res.string.terms_conditions_point_2))
                    BulletPoint(text = stringResource(Res.string.terms_conditions_point_3))
                }

                Spacer(modifier = Modifier.weight(.1f))

                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(
                        onClick = {
                            accountDeletionViewModel.deleteCustomer {
                                personalViewModel.onActionLogout {
                                    addressesViewModel.setAddressesEmpty()
                                    frequentlyOrderedViewModel.emptyFrequentlyOrderedList()
                                    navigator.replaceAll(
                                        HomeScreen()
                                    )
                                }
                            }
                        },
                    ) {
                        Text(
                            text = stringResource(Res.string.delete_account_button),
                            style = TextStyle(
                                color = Color(0xFFE53E3E), // Red color
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = stringResource(Res.string.reactivate_account_title),
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 18.2.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(Res.string.reactivate_account_description),
                        style = TextStyle(
                            fontSize = 10.sp,
                            lineHeight = 15.sp,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            color = TextSubdued,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }
}