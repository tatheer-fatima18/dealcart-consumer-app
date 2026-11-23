package com.localpk.app.features.address.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.address.data.AddAddressUiData
import com.localpk.app.features.address.data.AdditionalAddressDetails
import com.localpk.app.features.cart.view.CartScreen
import com.localpk.app.features.login.data.ScreenSource
import com.localpk.app.features.user_location.presentation.UserLocationViewModel
import com.localpk.app.utils.BIT_DELAY
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.ColorDivider
import com.localpk.app.theming.ColorPrimary
import com.localpk.app.theming.ColorPrimaryText
import com.localpk.app.theming.TextPrimary
import com.localpk.app.theming.TextSubduedHalf
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.orEmptyArrayList
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.address_type
import dealcart.composeapp.generated.resources.area
import dealcart.composeapp.generated.resources.building
import dealcart.composeapp.generated.resources.city
import dealcart.composeapp.generated.resources.complete_address
import dealcart.composeapp.generated.resources.flat_number
import dealcart.composeapp.generated.resources.floor
import dealcart.composeapp.generated.resources.landmark
import dealcart.composeapp.generated.resources.street
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.localpk.app.theming.PoppinsFontFamily

class AddAddressBottomSheetScreen(
    private val id: Long = 0L,
    private val address: String = EMPTY_STRING,
    private val lng: Double = 0.0,
    private val lat: Double = 0.0,
    private val street: String = EMPTY_STRING,
    private val city: String = EMPTY_STRING,
    private val area: String = EMPTY_STRING,
    private val addressType: String? = null,
    private val navigationFrom: String = EMPTY_STRING,
    private val nearestLandmark: String = EMPTY_STRING,
    private val phoneNumber: String? = null,
    // Additional address details
    private val flatNo: String? = null,
    private val floorNo: Int? = null,
    private val buildingName: String? = null,
    private val houseNumber: String? = null,
    private val shopName: String? = null,
    private val shopNumber: String? = null,
    private val warehouseId: Long
) : Screen {

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        val viewModel = koinInject<AddAddressBottomSheetViewModel>()
        val mapViewModel = koinInject<MapViewModel>()
        val userLocationViewModel = koinInject<UserLocationViewModel>()
        val addressesViewModel = koinViewModel<AddressListViewModel>()
        val searchText by addressesViewModel.searchText.collectAsState()
        val isSearching by addressesViewModel.isSearching.collectAsState()
        val cities by addressesViewModel.cities.collectAsState()
        val serviceAreaList = mapViewModel.serviceAreaList.collectAsState()
        val addressDataListener = addressesViewModel.addressDataListener.collectAsState()

        // Create AddressUiData from primitive parameters
        val argsData = AddAddressUiData(
            id = id,
            address = address,
            lng = lng,
            lat = lat,
            street = street,
            city = city,
            cities = cities.orEmptyArrayList(),
            state = serviceAreaList.value.orEmptyArrayList(),
            area = area,
            addressType = addressType,
            additionalAddressDetails = AdditionalAddressDetails(
                flatNo = flatNo,
                floorNo = floorNo,
                buildingName = buildingName,
                houseNumber = houseNumber,
                shopName = shopName,
                shopNumber = shopNumber
            ),
            navigationFrom = navigationFrom,
            nearestLandmark = nearestLandmark,
            phoneNumber = phoneNumber
        )

        LaunchedEffect(Unit) {
            viewModel.updateAddress(argsData)
        }

        var city by remember { mutableStateOf(argsData.city) }
        val addAddressButton by viewModel.addAddressButton.collectAsState()
        val addressType by viewModel.addressType.collectAsState()
        var area by remember { mutableStateOf(argsData.area) }
        var street by remember { mutableStateOf(argsData.street) }
        var flat by remember { mutableStateOf(argsData.additionalAddressDetails?.flatNo.default()) }
        var building by remember { mutableStateOf(argsData.additionalAddressDetails?.buildingName.default()) }
        var floor by remember {
            mutableStateOf(
                argsData.additionalAddressDetails?.floorNo.getValueOrDefault().toString()
            )
        }
        var house by remember { mutableStateOf(argsData.additionalAddressDetails?.houseNumber.default()) }
        var shopName by remember { mutableStateOf(argsData.additionalAddressDetails?.shopName.default()) }
        var shopNumber by remember { mutableStateOf(argsData.additionalAddressDetails?.shopNumber.default()) }
        var landmark by remember { mutableStateOf(argsData.nearestLandmark) }
        val scrollState = rememberScrollState()
        val focusManager = LocalFocusManager.current
        val keyBoardController = LocalSoftwareKeyboardController.current
        var cityDropdownExpanded by remember { mutableStateOf(false) }

        fun updateFields() {
            viewModel.onAddressTextChange(
                city, area, street, building, floor, flat, house, shopName, shopNumber
            )
        }

        LaunchedEffect(viewModel) {
            viewModel.actionDismissBottomSheet.events.collect {
                bottomSheetNavigator.hide()
                viewModel.addressDetails.value?.let { address ->
                    addressDataListener.value?.listener?.invoke()
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
                .imePadding()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                text = stringResource(Res.string.complete_address),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = ColorPrimaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            )

            Column(
                modifier = Modifier
                    .padding(
                        start = 13.dp,
                        end = 13.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // --- FORM FIELDS START ---
                // City Dropdown
                Text(
                    text = stringResource(Res.string.city),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ColorPrimaryText,
                        fontSize = 12.sp,
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            spotColor = Color(0x30576AFF),
                            ambientColor = Color(0x30576AFF)
                        )
                        .background(Color.White)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = BorderPrimaryDefault,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            value = searchText,
                            maxLines = 1,
                            onValueChange = {
                                addressesViewModel.onSearchTextChange(it)
                                cityDropdownExpanded = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { focusState ->
                                    val expanded = focusState.isFocused
                                    cityDropdownExpanded = expanded
                                    if (expanded) {
                                        addressesViewModel.onSearchTextChange(EMPTY_STRING)
                                    }
                                },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Search,
                                keyboardType = KeyboardType.Text
                            ),
                            placeholder = {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 2.dp),
                                    text = stringResource(Res.string.city),
                                    color = TextSubduedHalf,
                                    fontSize = 12.sp,
                                    fontFamily = PoppinsFontFamily(),
                                )
                            },
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    keyBoardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                            textStyle = TextStyle(
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily(),
                            )
                        )
                        // Dropdown list (only when expanded)
                        if (cityDropdownExpanded) {
                            if (isSearching) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = ColorPrimary
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 200.dp)
                                        .background(
                                            Color.White,
                                        )
                                ) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 4.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        if (cities.isEmpty()) {
                                            item {
                                                Text(
                                                    "No cities found",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = TextSubduedHalf
                                                    ),
                                                    modifier = Modifier.padding(vertical = 8.dp)
                                                )
                                            }
                                        } else {
                                            items(cities) { cityOption ->
                                                Text(
                                                    text = cityOption,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = TextPrimary
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            city = cityOption
                                                            addressesViewModel.onSearchTextChange(
                                                                cityOption
                                                            )
                                                            cityDropdownExpanded = false
                                                            keyBoardController?.hide()
                                                            focusManager.clearFocus()
                                                            updateFields()
                                                        }
                                                        .padding(vertical = 10.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // Area
                Text(
                    text = stringResource(Res.string.area),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ColorPrimaryText,
                        fontSize = 12.sp,
                    )
                )
                OutlinedTextField(
                    value = area,
                    onValueChange = {
                        area = it
                        updateFields()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            stringResource(Res.string.area),
                            color = TextSubduedHalf
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderPrimaryDefault,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                    )
                )
                // Street
                Text(
                    text = stringResource(Res.string.street),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ColorPrimaryText,
                        fontSize = 12.sp,
                    )
                )
                OutlinedTextField(
                    value = street,
                    onValueChange = {
                        street = it
                        updateFields()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            stringResource(Res.string.street),
                            color = TextSubduedHalf
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderPrimaryDefault,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                    )
                )
                // Address Type
                Text(
                    text = stringResource(Res.string.address_type),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ColorPrimaryText,
                        fontSize = 12.sp,
                    )
                )
                Row(

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val types = listOf(
                        "flat" to stringResource(Res.string.flat_number),
                        "house" to "House",
                        "shop" to "Shop",
                        "office" to "Office"
                    )
                    types.forEachIndexed { idx, (type, label) ->
                        Row(
                            modifier = Modifier
                                .height(40.dp)
                                .border(
                                    BorderStroke(1.dp, BorderPrimaryDefault),
                                    RoundedCornerShape(5.2.dp)
                                )
                                .selectable(
                                    selected = addressType == type,
                                    onClick = {
                                        viewModel.onActionCheckedChange(idx + 1)
                                        updateFields()
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = addressType == type,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = ColorPrimary,
                                    unselectedColor = ColorDivider
                                )
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = ColorPrimaryText,
                                    fontSize = 12.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
                // Conditional fields
                when (addressType) {
                    "flat", "office" -> {
                        // Flat Number
                        Text(
                            text = stringResource(Res.string.flat_number),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ColorPrimaryText,
                                fontSize = 12.sp,
                            )
                        )
                        OutlinedTextField(
                            value = flat,
                            onValueChange = {
                                flat = it
                                updateFields()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    stringResource(Res.string.flat_number),
                                    color = TextSubduedHalf
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = BorderPrimaryDefault,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                            )
                        )
                        // Building
                        Text(
                            text = stringResource(Res.string.building),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ColorPrimaryText,
                                fontSize = 12.sp,
                            )
                        )
                        OutlinedTextField(
                            value = building,
                            onValueChange = {
                                building = it
                                updateFields()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    stringResource(Res.string.building),
                                    color = TextSubduedHalf
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = BorderPrimaryDefault,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                            )
                        )
                        // Floor
                        Text(
                            text = stringResource(Res.string.floor),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ColorPrimaryText,
                                fontSize = 12.sp,
                            )
                        )
                        OutlinedTextField(
                            value = floor,
                            onValueChange = {
                                floor = it
                                updateFields()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    stringResource(Res.string.floor),
                                    color = TextSubduedHalf
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = BorderPrimaryDefault,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                            )
                        )
                    }

                    "house" -> {
                        // House Number
                        Text(
                            text = "House Number",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ColorPrimaryText,
                                fontSize = 12.sp,
                            )
                        )
                        OutlinedTextField(
                            value = house,
                            onValueChange = {
                                house = it
                                updateFields()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("House Number", color = TextSubduedHalf) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = BorderPrimaryDefault,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                            )
                        )
                    }

                    "shop" -> {
                        // Shop Name
                        Text(
                            text = "Shop Name",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ColorPrimaryText,
                                fontSize = 12.sp,
                            )
                        )
                        OutlinedTextField(
                            value = shopName,
                            onValueChange = {
                                shopName = it
                                updateFields()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Shop Name", color = TextSubduedHalf) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = BorderPrimaryDefault,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                            )
                        )
                        // Shop Number
                        Text(
                            text = "Shop Number",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ColorPrimaryText,
                                fontSize = 12.sp,
                            )
                        )
                        OutlinedTextField(
                            value = shopNumber,
                            onValueChange = {
                                shopNumber = it
                                updateFields()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Shop Number", color = TextSubduedHalf) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = BorderPrimaryDefault,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                            )
                        )
                    }
                }
                // Landmark
                Text(
                    text = stringResource(Res.string.landmark),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ColorPrimaryText,
                        fontSize = 12.sp,
                    )
                )
                OutlinedTextField(
                    value = landmark,
                    onValueChange = {
                        landmark = it
                        // Not required for button enable, so not in updateFields
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            stringResource(Res.string.landmark),
                            color = TextSubduedHalf
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BorderPrimaryDefault,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                    )
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 13.dp))
            Button(
                onClick = {
                    viewModel.onActionClickConfirm(
                        city,
                        area,
                        street,
                        building,
                        floor,
                        flat,
                        house,
                        shopName,
                        shopNumber,
                        landmark,
                        successCallback = { lat, lng, city ->
                            userLocationViewModel.getUserLocationWarehouse(
                                lat,
                                lng,
                                city,
                                isEditAddressRequest = argsData.id != 0L,
                                isAddAddressRequest = argsData.id == 0L,
                                warehouseId = warehouseId
                            )
                            addressesViewModel.onSearchTextChange(EMPTY_STRING)
                        }
                    )
                },
                enabled = addAddressButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(bottom = 16.dp, start = 13.dp, end = 13.dp),
                shape = RoundedCornerShape(7.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorPrimary,
                    disabledContainerColor = ColorDivider
                )
            ) {
                Text(
                    text = "Save Address",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}