package com.localpk.app.features.restaurant.presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.common_components.GlobalFoodDetailStateManager
import com.localpk.app.features.restaurant.components.FoodDetail
import com.localpk.app.features.restaurant.data.CategoryItemUIDataAndRestaurantDetails
import com.localpk.app.features.restaurant.data.Image
import com.localpk.app.features.restaurant.data.ItemChoiceGroup
import com.localpk.app.features.restaurant.data.ItemPrice
import com.localpk.app.features.restaurant.data.ItemVariation
import com.localpk.app.features.restaurant.viewModels.FoodDetailViewModel
import com.localpk.app.features.search.presentation.SearchScreen
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.utils.StatusBarIconColor
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel

class FoodDetailScreen(
    val id: Long,
    val name: String?,
    val description: String?,
    val imageUrl: String? = null,
    val pricesJson: String?,
    val variationsJson: String?,
    val choiceGroupsJson: String?,
    var itemQuantity: Int,
    val restaurantId: Long,
    val restaurantName: String,
    val logoImage: String? = null,
    val cuisinesCsv: String? = null,
    val deliveryFee: Long,
    val prepTime: String,
    val minimumOrderValue: Int
) : Screen {

    @Composable
    override fun Content() {
        val json = Json { ignoreUnknownKeys = true }
        val prices = pricesJson?.let { json.decodeFromString<ItemPrice>(it) }
        val variations = variationsJson?.let { json.decodeFromString<List<ItemVariation>>(it) }
        val choiceGroups =
            choiceGroupsJson?.let { json.decodeFromString<List<ItemChoiceGroup>>(it) }
        val cuisines = cuisinesCsv?.split(",")?.filter { it.isNotEmpty() }

        val args = CategoryItemUIDataAndRestaurantDetails(
            id = id,
            name = name,
            description = description,
            image = imageUrl?.let {
                Image(
                    id = 1,
                    full = it,
                    medium = it,
                    path = it,
                    thumbnail = it
                )
            },
            prices = prices,
            variations = variations,
            choiceGroups = choiceGroups,
            itemQuantity = itemQuantity,
            quantityListener = {},
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            logoImage = null,
            cuisines = cuisines,
            deliveryFee = deliveryFee,
            prepTime = prepTime,
            minimumOrderValue = minimumOrderValue
        )

        val foodDetailViewModel = koinViewModel<FoodDetailViewModel>()

        LaunchedEffect(Unit) {
            foodDetailViewModel.init(args)
            GlobalFoodDetailStateManager.initializeWithArgs(args)
        }

        val selectedVariationId by foodDetailViewModel.selectedVariationId.collectAsState()
        val selectedChoicesIds by foodDetailViewModel.selectedChoicesIds.collectAsState()
        val specialInstructions by foodDetailViewModel.specialInstructions.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        StatusBarIconColor(backgroundColor = SurfaceBackground)
        Surface(color = SurfaceBackground) {
            FoodDetail(
                args,
                onBack = { navigator.pop() },
                onSearch = { navigator.push(SearchScreen(args = true)) },
                selectedVariationId = selectedVariationId,
                onVariationSelected = { foodDetailViewModel.setSelectedVariationId(it) },
                selectedChoicesIds = selectedChoicesIds,
                onChoicesSelected = { foodDetailViewModel.setSelectedChoicesIds(it) },
                specialInstructions = specialInstructions,
                onInstructionsChanged = { foodDetailViewModel.setSpecialInstructions(it) }
            )
        }
    }
}