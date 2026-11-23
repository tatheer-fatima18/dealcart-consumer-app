@file:OptIn(ExperimentalMaterial3Api::class)

package com.localpk.app.features.personal.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.base.BaseScreen
import com.localpk.app.features.address.presentation.AddressListViewModel
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import com.localpk.app.features.frequently_ordered.viewModel.FrequentlyOrderedViewModel
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.personal.components.DobField
import com.localpk.app.features.personal.components.EmailField
import com.localpk.app.features.personal.components.EmailVerificationBottomSheetContent
import com.localpk.app.features.personal.components.FormField
import com.localpk.app.features.personal.components.GenderSelection
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.GreyLight
import com.localpk.app.theming.Orange
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.Yellow
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.isNotZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.date_of_birth
import dealcart.composeapp.generated.resources.delete_account_button
import dealcart.composeapp.generated.resources.ic_back_arrow_black
import dealcart.composeapp.generated.resources.ic_edit_profile
import dealcart.composeapp.generated.resources.ic_logout_profile
import dealcart.composeapp.generated.resources.ic_profile_avatar_female
import dealcart.composeapp.generated.resources.ic_profile_avatar_profile
import dealcart.composeapp.generated.resources.logout
import dealcart.composeapp.generated.resources.name
import dealcart.composeapp.generated.resources.phone_label
import dealcart.composeapp.generated.resources.profile
import dealcart.composeapp.generated.resources.save
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class PersonalScreen(private val userID: Long = ZERO_LONG) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val cartViewModel: CartViewModel = koinInject()
        val foodCartViewModel: FoodCartViewModel = koinInject()
        val viewModel = koinViewModel<PersonalViewModel>()
        val addressesViewModel = koinViewModel<AddressListViewModel>()
        val frequentlyOrderedViewModel = koinViewModel<FrequentlyOrderedViewModel>()
        val name by viewModel.nameLiveData.collectAsState()
        val phone by viewModel.phoneLiveData.collectAsState()
        val email by viewModel.emailLiveData.collectAsState()
        val dob by viewModel.dobLiveData.collectAsState()
        val isEdit by viewModel.isEdit.collectAsState()
        val isEmailVerified by viewModel.isEmailVerified.collectAsState()
        val gender by viewModel.genderLiveData.collectAsState()
        val showBottomSheet by viewModel.showBottomSheet.collectAsState()

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        if (userID.isNotZero()) {
            viewModel.handleVerifyEmail()
        }

        val hasError = remember {
            mutableStateOf(
                !viewModel.isValidEmail(email.default()) && email.default().isNotEmpty()
            )
        }

        LaunchedEffect(Unit) {
            viewModel.getPersonalDetails()
            viewModel.setPhoneNumber()
        }
        StatusBarIconColor(backgroundColor = Color.White)
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (showBottomSheet) {
                ModalBottomSheet(
                    containerColor = Color.White,
                    onDismissRequest = {
                        viewModel.showBottomSheet.value = false
                    },
                    sheetState = sheetState,
                    dragHandle = {},
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .wrapContentHeight()
                    ) {
                        EmailVerificationBottomSheetContent(
                            onClick = {
                                if (isEmailVerified.not()) viewModel.handleSendEmail()
                                viewModel.showBottomSheet.value = false
                            }, isEmailVerified = isEmailVerified
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp).statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { navigator.pop() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_back_arrow_black),
                            contentDescription = null
                        )
                    }

                    Text(
                        text = stringResource(Res.string.profile),
                        modifier = Modifier.align(Alignment.Center),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        fontSize = 16.sp
                    )

                    if (isEdit.not()) {
                        IconButton(
                            onClick = { viewModel.clickEdit() },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_edit_profile),
                                contentDescription = null
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .width(88.dp)
                        .height(88.dp)
                ) {
                    Image(
                        painter = painterResource(
                            if (gender == "female") Res.drawable.ic_profile_avatar_female
                            else Res.drawable.ic_profile_avatar_profile
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    FormField(
                        label = stringResource(Res.string.name),
                        value = name.default(),
                        enabled = isEdit.default(),
                        onValueChange = { viewModel.setName(it) },
                        modifier = Modifier
                            .padding(horizontal = 60.dp)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    FormField(
                        label = stringResource(Res.string.phone_label),
                        value = phone.default(),
                        enabled = false,
                        modifier = Modifier
                            .padding(horizontal = 60.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    GenderSelection(
                        selectedGender = gender.default(),
                        enabled = isEdit.default(),
                        onGenderSelected = { viewModel.putValueGender(it) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    EmailField(
                        value = email.default(),
                        enabled = isEdit.default(),
                        isVerified = isEmailVerified.default(),
                        hasError = hasError.value,
                        onValueChange = { newValue ->
                            viewModel.emailLiveData.value = newValue
                            hasError.value = !viewModel.isValidEmail(newValue)
                        }
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    DobField(
                        label = stringResource(Res.string.date_of_birth),
                        value = dob.default(),
                        enabled = isEdit.default(),
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .padding(end = 50.dp),
                        personalViewModel = viewModel
                    )

                    if (RemoteConfigs.isAccountDeletionEnabled()) {
                        Spacer(modifier = Modifier.height(20.dp))

                        // Delete Account Option
                        TextButton(
                            onClick = {
                                navigator.push(AccountDeletionScreen())
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 60.dp)
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
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isEdit) {
                        Button(
                            onClick = { viewModel.updatePersonalDetails() },
                            enabled = hasError.value.not(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (hasError.value) GreyLight else Yellow,
                                disabledContainerColor = GreyLight
                            )
                        ) {
                            Text(
                                text = stringResource(Res.string.save).uppercase(),
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontFamily = PoppinsFontFamily(),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Button(
                        onClick = {
                            viewModel.onActionLogout {
                                addressesViewModel.setAddressesEmpty()
                                frequentlyOrderedViewModel.emptyFrequentlyOrderedList()
                                navigator.replaceAll(HomeScreen())
                            }
                            cartViewModel.clearLocalCartItems()
                            foodCartViewModel.clearFoodCartState()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Orange)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_logout_profile),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = stringResource(Res.string.logout).uppercase(),
                            style = TextStyle(
                                letterSpacing = 0.5.sp,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontFamily = PoppinsFontFamily(),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}
