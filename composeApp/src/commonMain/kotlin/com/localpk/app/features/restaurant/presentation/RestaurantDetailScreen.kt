package com.localpk.app.features.restaurant.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.common_components.CustomAlertDialog
import com.localpk.app.common_components.GlobalFoodDetailStateManager
import com.localpk.app.features.restaurant.components.RestaurantInfoBottomSheet
import com.localpk.app.features.restaurant.components.RestaurantScreen
import com.localpk.app.features.restaurant.components.RestaurantScreenSkeleton
import com.localpk.app.features.restaurant.data.CategoryItemUIData
import com.localpk.app.features.restaurant.data.RestaurantDetailArgsData
import com.localpk.app.features.restaurant.viewModels.RestaurantDetailViewModel
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.not_now
import dealcart.composeapp.generated.resources.remove_items
import dealcart.composeapp.generated.resources.start_new_order
import dealcart.composeapp.generated.resources.you_have_grocery_order
import dealcart.composeapp.generated.resources.you_have_new_order
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class RestaurantDetailScreen(
    private val id: Long,
    private val name: String,
    private val logoImage: String? = EMPTY_STRING,
    private val cuisines: String? = EMPTY_STRING,
    private val deliveryFee: Long,
    private val prepTime: String,
    private val minimumOrderValue: Int
) : Screen {

    @Composable
    override fun Content() {

        val cuisinesList = cuisines?.split(",")?.filter { it.isNotEmpty() }

        val args =
            RestaurantDetailArgsData(
                id, name, logoImage, cuisinesList, deliveryFee, prepTime, minimumOrderValue
            )


        val restaurantDetailViewModel = koinViewModel<RestaurantDetailViewModel>()

        LaunchedEffect(Unit) {
            restaurantDetailViewModel.setArgsData(args)
        }


        val eventLogger: EventLogger = koinInject()


        val restaurantsDetails by restaurantDetailViewModel.restaurantsDetails.collectAsState()
        val openSheet = remember { mutableStateOf(false) }
        val shouldShowFoodDialog = remember { mutableStateOf(false) }
        val itemToAdd = remember { mutableStateOf<CategoryItemUIData?>(null) }
        val shouldShowGroceryDialog = remember { mutableStateOf(false) }

        val isLoading = restaurantsDetails == null

        val navigator = LocalNavigator.currentOrThrow

        StatusBarIconColor(backgroundColor = Color.White)
        Surface(color = if (isLoading) Color.White else SurfaceBackground) {
            when {
                isLoading -> {
                    RestaurantScreenSkeleton()
                }

                restaurantsDetails.isNotNull() -> {
                    RestaurantScreen(
                        onBackClick = { navigator.pop() },
                        onSearchClick = { onActionSearch() },
                        restaurantMenuDetails = restaurantsDetails,
                        restaurantDetails = args,
                        onInfoClick = {
                            openSheet.value = true
                        },
                        onFoodAddClick = { item, categoryName ->
                            if (item.hasVariationsOrChoiceGroups()) {
                                eventLogger.clickFoodProduct(
                                    id = item.id,
                                    name = item.name.default(),
                                    image = item.image?.medium.default(),
                                    listPrice = item.prices?.listPrice.getValueOrDefault(),
                                    discountPrice = item.prices?.discountPrice.getValueOrDefault(),
                                    restaurantName = name,
                                    variations = item.variations
                                        ?.takeIf { it.isNotEmpty() }
                                        ?.map { it.name },
                                    categoryName = categoryName
                                )
                            } else {
                                eventLogger.addFoodToCart(
                                    id = item.id,
                                    name = item.name.default(),
                                    quantity = item.itemQuantity,
                                    discountPrice = item.prices?.discountPrice.getValueOrDefault(),
                                    source = "quick buy",
                                    categoryName = categoryName,
                                    restaurantName = name,
                                    variations = null
                                )
                            }

                        },
                        onFoodCardClick = { item, _ ->
                            val pricesJson =
                                item.prices?.let { kotlinx.serialization.json.Json.encodeToString(it) }
                            val variationsJson = item.variations?.let {
                                kotlinx.serialization.json.Json.encodeToString(it)
                            }
                            val choiceGroupsJson = item.choiceGroups?.let {
                                kotlinx.serialization.json.Json.encodeToString(it)
                            }
                            val cuisinesCsv = args.cuisines?.joinToString(",")
                            GlobalFoodDetailStateManager.clearState()
                            navigator.push(
                                FoodDetailScreen(
                                    id = item.id,
                                    name = item.name,
                                    description = item.description,
                                    imageUrl = item.image?.medium,
                                    pricesJson = pricesJson,
                                    variationsJson = variationsJson,
                                    choiceGroupsJson = choiceGroupsJson,
                                    itemQuantity = item.itemQuantity,
                                    restaurantId = args.id,
                                    restaurantName = args.name,
                                    logoImage = args.logoImage,
                                    cuisinesCsv = cuisinesCsv,
                                    deliveryFee = args.deliveryFee,
                                    prepTime = args.prepTime,
                                    minimumOrderValue = args.minimumOrderValue
                                )
                            )
                        },
                        onIncrementClick = {
                        },
                        onDecrementClick = {
                        },
                        restaurantLogoImage = logoImage.default(),
                        onRestaurantCategoryClick = { restaurantName, categoryName ->
                            eventLogger.clickRestaurantCategory(
                                restaurantName = restaurantName,
                                categoryName = categoryName
                            )
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = openSheet.value && restaurantsDetails?.additionalDetails != null,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )

            ) {
                RestaurantInfoBottomSheet(
                    onClose = { openSheet.value = false },
                    additionalDetails = restaurantsDetails?.additionalDetails!!,
                    minimumOrderValue = args.minimumOrderValue,
                    deliveryFee = args.deliveryFee,
                )
            }

            if (shouldShowFoodDialog.value && itemToAdd.value != null
            // && MainActivity.foodCart.value.isNotNull()
            ) {
                CustomAlertDialog(
                    isAddressDialog = false,
                    shouldShowDialog = shouldShowFoodDialog,
                    icon = null,
                    title = stringResource(Res.string.start_new_order),
                    message = stringResource(Res.string.you_have_new_order),
                    confirmButton = stringResource(Res.string.remove_items),
                    cancelButton = stringResource(Res.string.not_now)
                ) {
//                    mainActivityViewModel.clearCart(isFood = true) {
//                        shouldShowFoodDialog.value = false
//                        itemToAdd.value?.let { item ->
//                            mainActivityViewModel.addFoodItemToCart(
//                                itemId = item.id.getValueOrDefault(),
//                                successCallback = {
//                                    item.itemQuantity.value++
//                                    itemToAdd.value = null
//                                }, failureCallback = {
//                                    shouldShowFoodDialog.value = true
//                                }
//                            )
//                        }
//                    }
                }
            }

            if (shouldShowGroceryDialog.value && itemToAdd.value.isNotNull()
            //  && MainActivity.viewCart.value.isNotNull()
            ) {
                CustomAlertDialog(
                    isAddressDialog = false,
                    shouldShowDialog = shouldShowGroceryDialog,
                    icon = null,
                    title = stringResource(Res.string.start_new_order),
                    message = stringResource(Res.string.you_have_grocery_order),
                    confirmButton = stringResource(Res.string.remove_items),
                    cancelButton = stringResource(Res.string.not_now)
                ) {
//                    mainActivityViewModel.clearCart(isFood = false) {
//                        shouldShowGroceryDialog.value = false
//                        itemToAdd.value?.let { item ->
//                            mainActivityViewModel.addFoodItemToCart(
//                                itemId = item.id.getValueOrDefault(),
//                                successCallback = {
//                                    item.itemQuantity.value++
//                                    itemToAdd.value = null
//                                }, failureCallback = {
//                                    shouldShowFoodDialog.value = true
//                                }
//                            )
//                        }
//                    }
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    //  mainActivityViewModel.setFood(data = false)
                }
            }

        }
    }

    private fun onActionSearch() {
//        navigate(
//            RestaurantDetailFragmentDirections.actionRestaurantDetailToNavigationSearch(
//                argsData = true
//            )
//        )
    }
}