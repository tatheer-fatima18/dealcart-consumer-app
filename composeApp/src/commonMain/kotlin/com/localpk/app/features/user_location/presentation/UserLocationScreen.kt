package com.localpk.app.features.user_location.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.address.presentation.AddressListViewModel
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.user_location.components.GetCityListView
import com.localpk.app.features.user_location.components.GetCurrentLocationView
import com.localpk.app.features.user_location.components.SearchCity
import com.localpk.app.theming.ColorPrimary
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


class UserLocationScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<UserLocationViewModel>()
        val addressesViewModel = koinViewModel<AddressListViewModel>()
        val searchText by addressesViewModel.searchText.collectAsState()
        val isSearching by addressesViewModel.isSearching.collectAsState()
        val cities by addressesViewModel.cities.collectAsState()
        val isGettingCurrentLocation by viewModel.isGettingCurrentLocation.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val coroutine = rememberCoroutineScope()

        if (isSearching) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.statusBarsPadding()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(50.dp),
                    color = ColorPrimary,
                    strokeCap = StrokeCap.Round
                )
            }
        }

        LaunchedEffect(Unit) {
            addressesViewModel.getOperationalCities()
            coroutine.launch {
             //   viewModel.getCurrentLocation { navigator.replaceAll(HomeScreen()) }
            }
        }

        // Auto-retry when permission is granted
        // Show error if location permission is denied

        Column(
            modifier = Modifier
                .fillMaxSize().statusBarsPadding()
        ) {
            SearchCity(searchText) {
                addressesViewModel.onSearchTextChange(it)
            }
            Spacer(modifier = Modifier.height(8.dp))
            GetCurrentLocationView(
                onGetLocationClicked = {
                    coroutine.launch {
                        viewModel.getCurrentLocation(
                            listenerCallback = { navigator.replaceAll(HomeScreen()) },
                            backToUserLocationScreen = { navigator.replaceAll(UserLocationScreen()) }
                        )
                    }
                },
                isLoading = isGettingCurrentLocation,
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            GetCityListView(viewModel, cities)
        }
    }
} 