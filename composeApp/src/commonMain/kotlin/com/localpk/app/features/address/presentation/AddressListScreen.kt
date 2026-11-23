package com.localpk.app.features.address.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.common_components.CustomAlertDialog
import com.localpk.app.common_components.NewAddressButton
import com.localpk.app.features.address.components.AddressCardList
import com.localpk.app.features.address.components.AddressNotFoundScreen
import com.localpk.app.features.address.components.TopBar
import com.localpk.app.features.address.data.toMapScreen
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.cart.viewModel.FoodCartViewModel
import com.localpk.app.features.user_location.presentation.UserLocationViewModel
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.isNotZero
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.changing_location
import dealcart.composeapp.generated.resources.location_pin
import dealcart.composeapp.generated.resources.not_now
import dealcart.composeapp.generated.resources.updating_address
import dealcart.composeapp.generated.resources.updating_location
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class AddressListScreen : Screen {
    @Composable
    override fun Content() {
        val userLocationViewModel = koinViewModel<UserLocationViewModel>()
        val viewModel = koinViewModel<AddressListViewModel>()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val addresses by viewModel.addresses.collectAsState(initial = emptyList())
        val isAddressesLoaded by viewModel.isAddressesLoaded.collectAsState(initial = false)
        val cartViewModel = koinViewModel<CartViewModel>()
        val foodCartViewModel = koinViewModel<FoodCartViewModel>()
        val shouldShowAddressChangeDialog = remember { mutableStateOf(false) }
        val localCartItems by cartViewModel.localCartItems.collectAsState()
        val localFoodCartItems by foodCartViewModel.localFoodCartItems.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.loadSavedAddresses()
        }

        StatusBarIconColor(backgroundColor = Color.White)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(16.dp).statusBarsPadding()
        ) {
            if (!isAddressesLoaded) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = ColorPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            if (addresses.isNotEmpty() || !isAddressesLoaded) {
                TopBar(onClick = { navigator.pop() })
                Column(modifier = Modifier.weight(1f)) {
                    AddressCardList(
                        addressList = addresses,
                        selectedAddressId = sharedPreferencesManager.getDefaultAddressId(),
                        onDeleteClick = { addressId ->
                            viewModel.removeAddress(
                                addressId
                            )
                            sharedPreferencesManager.setDefaultAddressId(ZERO_LONG)
                            sharedPreferencesManager.setDefaultAddress(EMPTY_STRING)
                        },
                        onEditClick = { addressUiData ->
                            navigator.push(addressUiData.toMapScreen())
                        },
                        onConfirmAddress = { address ->
                            userLocationViewModel.getUserLocationWarehouse(
                                address.lat,
                                address.lng,
                                address.city,
                                addressDialogSuccessCallback = {
                                    viewModel.selectDefaultAddress(
                                        address
                                    )
                                    when {
                                        sharedPreferencesManager.getSelectedWarehouseId() != 1L -> {
                                            cartViewModel.fetchUserCart {
                                                cartViewModel.removeGroceryItems()
                                            }
                                        }
                                    }
                                }
                            )
                        },
                        onAddressClick = { address ->
                            cartViewModel.clearCart(isFoodCart = true) {
                                userLocationViewModel.getUserLocationWarehouse(
                                    address.lat,
                                    address.lng,
                                    address.city,
                                    successCallback = {
                                        viewModel.selectDefaultAddress(
                                            address
                                        )
                                        when {
                                            sharedPreferencesManager.getSelectedWarehouseId() != 1L -> {
                                                cartViewModel.fetchUserCart {
                                                    cartViewModel.removeGroceryItems()
                                                }
                                            }
                                        }
                                        foodCartViewModel.clearFoodCartState()
                                    }
                                )
                            }
                        },
                        cart = localCartItems.size,
                        foodCart = localFoodCartItems.size
                    )
                }
            } else {
                AddressNotFoundScreen(onClick = { navigator.pop() })
            }
            NewAddressButton(
                onClick = {
                    if (localFoodCartItems.size.isNotZero()) {
                        shouldShowAddressChangeDialog.value = true
                    } else {
                        cartViewModel.clearCart(isFoodCart = true)
                        {
                            navigator.push(MapScreen())
                        }
                    }
                }
            )
            if (shouldShowAddressChangeDialog.value) {
                CustomAlertDialog(
                    isAddressDialog = false,
                    shouldShowDialog = shouldShowAddressChangeDialog,
                    icon = Res.drawable.location_pin,
                    title = stringResource(Res.string.updating_location),
                    message = stringResource(Res.string.changing_location),
                    confirmButton = stringResource(Res.string.updating_address),
                    cancelButton = stringResource(Res.string.not_now)
                ) {
                    cartViewModel.clearCart(isFoodCart = true) {
                        navigator.push(MapScreen())
                    }
                }
            }
        }

    }
}