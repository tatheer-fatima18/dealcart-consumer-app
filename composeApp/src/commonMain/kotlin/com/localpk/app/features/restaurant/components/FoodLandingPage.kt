package com.localpk.app.features.restaurant.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.localpk.app.common_components.CustomAlertDialog
import com.localpk.app.features.restaurant.data.CuisineUI
import com.localpk.app.features.restaurant.data.RestaurantDetailArgsData
import com.localpk.app.features.restaurant.data.RestaurantUI
import com.localpk.app.theming.PoppinsFontFamily
import com.localpk.app.theming.TextSubdued
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.orEmptyArrayList
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.cancel
import dealcart.composeapp.generated.resources.explore_cuisines
import dealcart.composeapp.generated.resources.login
import dealcart.composeapp.generated.resources.login_continue
import dealcart.composeapp.generated.resources.login_to_proceed
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodLandingPage(
    cuisines: List<CuisineUI>?,
    restaurants: List<RestaurantUI>?,
    selectedCuisines: List<CuisineUI>,
    onCuisineClick: (CuisineUI) -> Unit,
    onRestaurantCardClick: (RestaurantDetailArgsData) -> Unit,
    loggedIn: Boolean,
    onConfirm: () -> Unit
) {
    val shouldShowDialog = remember { mutableStateOf(false) }
    if (shouldShowDialog.value) {
        CustomAlertDialog(
            isAddressDialog = false,
            shouldShowDialog = shouldShowDialog,
            icon = null,
            title = stringResource(Res.string.login),
            message = stringResource(Res.string.login_to_proceed),
            confirmButton = stringResource(Res.string.login_continue),
            cancelButton = stringResource(Res.string.cancel)
        ) {
            onConfirm()
        }
    }
    if (cuisines.isNotNull()) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.explore_cuisines),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = TextSubdued,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                itemsIndexed(cuisines.orEmptyArrayList()) { _, cuisine ->
                    CuisineItem(
                        data = cuisine,
                        selectedCuisines = selectedCuisines,
                        onCuisineClick = onCuisineClick
                    )
                }
            }

            if (restaurants.isNotNull()) {
                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    restaurants?.forEach { restaurant ->
                        RestaurantCard(
                            restaurant,
                            onRestaurantCardClick = {
                                if (loggedIn)
                                    onRestaurantCardClick(
                                        RestaurantDetailArgsData(
                                            id = restaurant.id,
                                            deliveryFee = restaurant.deliveryFee,
                                            minimumOrderValue = restaurant.minimumOrderValue,
                                            name = restaurant.name,
                                            prepTime = restaurant.deliveryTime,
                                            cuisines = restaurant.cuisines,
                                            logoImage = restaurant.logoImage?.medium
                                        )
                                    )
                                else
                                    shouldShowDialog.value = true
                            })
                    }
                }
            }
        }
    }
}


