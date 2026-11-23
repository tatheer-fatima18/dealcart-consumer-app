package com.localpk.app.features.user_location.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.home.presentation.HomeScreen
import com.localpk.app.features.user_location.presentation.UserLocationScreen
import com.localpk.app.features.user_location.presentation.UserLocationViewModel
import com.localpk.app.theming.BlackSecondary
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.utils.DEFAULT_COORDINATES
import com.localpk.app.utils.ZERO
import com.localpk.app.utils.toTitleCase

@Composable
fun GetCityListView(viewModel: UserLocationViewModel, cityList: List<String>) {
    val navigator = LocalNavigator.currentOrThrow
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            text = "Most Searched Cities",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            var karachiLocation: Pair<Double, Double>

            items(cityList) {
                Text(
                    text = it.toTitleCase(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            karachiLocation = when (it.lowercase()) {
                                "karachi" -> {
                                    Pair(
                                        DEFAULT_COORDINATES.latitude,
                                        DEFAULT_COORDINATES.longitude
                                    )
                                }

                                else -> Pair(ZERO, ZERO)
                            }
                            viewModel.getUserLocationWarehouse(
                                karachiLocation.first,
                                karachiLocation.second,
                                it,
                                successCallback =  {
                                    navigator.replaceAll(HomeScreen())
                                },
                                backToUserLocationScreen = {
                                    navigator.replaceAll(UserLocationScreen())
                                }
                            )
                        }
                        .padding(vertical = 4.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 12.sp,
                        fontFamily = PoppinsFontFamily(),
                        color = BlackSecondary,
                    )
                )
            }
        }
    }
}