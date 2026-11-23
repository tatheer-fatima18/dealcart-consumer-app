package com.localpk.app.features.frequently_ordered.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.frequently_ordered.components.FrequentlyOrderedSwimlaneComponent
import com.localpk.app.features.frequently_ordered.components.TopLayer
import com.localpk.app.features.frequently_ordered.viewModel.FrequentlyOrderedViewModel
import com.localpk.app.features.productlist.components.ProductGridShimmerPlaceholder
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.search.presentation.SearchScreen
import com.localpk.app.utils.EventLogger
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.buy_again
import dealcart.composeapp.generated.resources.navbutton_black
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class FrequentlyOrderedScreen : Screen {

    @Composable
    override fun Content() {
        val frequentlyOrderedViewModel = koinViewModel<FrequentlyOrderedViewModel> {
            parametersOf()
        }

        val cartViewModel = koinViewModel<CartViewModel>()

        val frequentItems by frequentlyOrderedViewModel.frequentlyOrderedList.collectAsState()
        val cartState by cartViewModel.cartState.collectAsState()

        fun onLoadMore() {
            frequentlyOrderedViewModel.getFrequentlyOrderedItems(
                cart = cartState,
                limit = frequentlyOrderedViewModel.pageSize,
                page = frequentlyOrderedViewModel.currentPage
            )
            frequentlyOrderedViewModel.currentPage++
        }

        LaunchedEffect(Unit) {
            frequentlyOrderedViewModel.getFrequentlyOrderedItems(
                cart = cartState,
                limit = frequentlyOrderedViewModel.pageSize,
                page = frequentlyOrderedViewModel.currentPage
            )
            frequentlyOrderedViewModel.currentPage++
        }

        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                FrequentItemsListingScreen(frequentItems, onLoadMore = { onLoadMore() })
            }
        }
    }

    @Composable
    fun FrequentItemsListingScreen(
        frequentItems: List<ProductUiData>,
        onLoadMore: () -> Unit
    ) {
        val eventLogger: EventLogger = koinInject()
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier
                .fillMaxSize().statusBarsPadding()
                .background(color = Color.White)
        ) {
            TopLayer(
                title = stringResource(Res.string.buy_again),
                onBackClick = { navigator.pop() },
                onSearchClick = {
                    navigator.push(SearchScreen(false))
                },
                backButton = Res.drawable.navbutton_black
            )
            if (frequentItems.isEmpty())
                ProductGridShimmerPlaceholder()
            else {
                FrequentlyOrderedSwimlaneComponent(
                    viewAllProductList = frequentItems,
                    onAddClick = { productData ->
                        if (productData.hasVariants.not()) {
                            eventLogger.addToCartMergn(
                                productData.id,
                                productData.name,
                                1,
                                productData.price,
                                "reorder_view_all",
                                type = "quick_buy"
                            )
                        }
                    },
                    onCardClick = { productData ->
                        eventLogger.productClicked(
                            productData,
                            "",
                            "",
                            "Reorder ViewAll Page"
                        )
                    }
                ) {
                    onLoadMore()
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}