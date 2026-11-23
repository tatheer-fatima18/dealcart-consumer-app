package com.localpk.app.features.address.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.localpk.app.features.address.components.AddPhoneNumber
import com.localpk.app.features.address.data.AddAddressRequest
import com.localpk.app.features.address.data.AdditionalAddressDetails
import com.localpk.app.features.checkout.view.CartCheckoutViewModel
import com.localpk.app.utils.EMPTY_STRING
import org.koin.compose.koinInject

class AddPhoneNumberBottomSheetScreen(
    private val lng: Double = 0.0,
    private val lat: Double = 0.0,
    private val city: String = EMPTY_STRING,
    private val addressType: String = EMPTY_STRING,
    private val country: String = "Pakistan",
    private val isDefault: Boolean = true,
    private val status: Boolean = true,
    private val id: Long = 0,
    private val area: String = EMPTY_STRING,
    private val streetName: String = EMPTY_STRING,
    private val nearestLandmark: String = EMPTY_STRING,
    private val phoneNumber: String? = null,
    private val navigationFrom: String? = null,
    // Additional address details
    private val flatNo: String? = null,
    private val floorNo: Int? = null,
    private val buildingName: String? = null,
    private val houseNumber: String? = null,
    private val shopName: String? = null,
    private val shopNumber: String? = null
) : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinInject<AddPhoneNumberBottomSheetViewModel>()
        val cartCheckoutViewModel = koinInject<CartCheckoutViewModel>()
        val addButton by viewModel.addButton.collectAsState()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        // Create AddAddressRequest from primitive parameters
        val argsData = AddAddressRequest(
            lng = lng,
            lat = lat,
            city = city,
            addressType = addressType,
            country = country,
            isDefault = isDefault,
            status = status,
            id = id,
            area = area,
            streetName = streetName,
            additionalAddressDetails = AdditionalAddressDetails(
                flatNo = flatNo,
                floorNo = floorNo,
                buildingName = buildingName,
                houseNumber = houseNumber,
                shopName = shopName,
                shopNumber = shopNumber
            ),
            nearestLandmark = nearestLandmark,
            phoneNumber = phoneNumber,
            navigationFrom = navigationFrom
        )

        LaunchedEffect(Unit) {
            viewModel.updateArgsData(argsData)
        }

        Surface(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
        ) {
            AddPhoneNumber(
                args = argsData,
                onPhoneNumberChange = { viewModel.onAddressTextChange(it) },
                addButton = addButton,
                onConfirmClick = {
                    viewModel.onActionClickConfirm(
                        it
                    ) {
                        cartCheckoutViewModel.getAddresses(true)
                        bottomSheetNavigator.hide() }
                },
                onSkipClick = { viewModel.onActionClickSkip({ bottomSheetNavigator.hide() }) }
            )
        }
    }
}