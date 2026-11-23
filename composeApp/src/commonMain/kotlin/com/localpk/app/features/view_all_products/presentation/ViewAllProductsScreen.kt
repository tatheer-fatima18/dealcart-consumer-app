package com.localpk.app.features.view_all_products.presentation

import androidx.compose.foundation.background
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
import com.localpk.app.features.productlist.components.ProductGridShimmerPlaceholder
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.search.presentation.SearchScreen
import com.localpk.app.features.swimlane_engine.data.SwimlaneType
import com.localpk.app.features.view_all_products.data.ViewAllProductArgs
import com.localpk.app.features.view_all_products.viewModel.ViewAllProductViewModel
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.StatusBarIconColor
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.navbutton_black
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class ViewAllProductsScreen(
    val isPromotional: Boolean,
    val categoryId: Long? = null,
    val categoryName: String,
    val swimlaneId: Int? = null,
    val swimlaneType: String?,
    val isDiscount: Boolean
) : Screen {

    @Composable
    override fun Content() {
        val args = ViewAllProductArgs(
            isPromotional, categoryId, categoryName, swimlaneId, swimlaneType, isDiscount
        )

        val viewAllProductViewModel =
            koinViewModel<ViewAllProductViewModel>(key = "ProductListViewModel_${args.swimlaneId}") {
                parametersOf(args)
            }
        val cartViewModel = koinViewModel<CartViewModel>()
//        private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

        val cartState by cartViewModel.cartState.collectAsState()
        val viewAllProductList by viewAllProductViewModel.viewAllProductList.collectAsState()
        val eventLogger: EventLogger = koinInject()
        // val showDialog by mainActivityViewModel.showFoodDialog

        LaunchedEffect(Unit) {
            viewAllProductViewModel.getViewALlProductItems(
                limit = viewAllProductViewModel.pageSize,
                page = viewAllProductViewModel.currentPage,
                cart = cartState
            )
            viewAllProductViewModel.currentPage++
        }
        fun onLoadMore() {
            viewAllProductViewModel.getViewALlProductItems(
                limit = viewAllProductViewModel.pageSize,
                page = viewAllProductViewModel.currentPage,
                cart = cartState
            )
            viewAllProductViewModel.currentPage++
        }
        StatusBarIconColor(backgroundColor = Color.White)
        Surface {
            ViewAllListingScreen(
                viewAllProductList,
                onLoadMore = { onLoadMore() },
                args,
                eventLogger
            )
        }

    }

    @Composable
    fun ViewAllListingScreen(
        viewAllProductList: List<ProductUiData>,
        onLoadMore: () -> Unit,
        args: ViewAllProductArgs,
        eventLogger: EventLogger
    ) {
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White).statusBarsPadding()
        ) {
            TopLayer(
                title = args.categoryName,
                onBackClick = { navigator.pop() },
                onSearchClick = {
                    navigator.push(SearchScreen(false))
                },
                backButton = Res.drawable.navbutton_black
            )

            if (viewAllProductList.isEmpty())
                ProductGridShimmerPlaceholder()
            else {
                FrequentlyOrderedSwimlaneComponent(
                    viewAllProductList = viewAllProductList,
                    onCardClick = { productData ->
                        if (args.isPromotional) {
                            eventLogger.productClicked(
                                productData,
                                args.categoryName,
                                EMPTY_STRING,
                                "PromotionalCategory- ${args.categoryName}"
                            )
                        } else if (args.isDiscount) {
                            eventLogger.productClicked(
                                productData,
                                EMPTY_STRING,
                                EMPTY_STRING,
                                "DiscountSwimlaneMore_[${args.swimlaneId}]"
                            )
                        }
                    },
                    onAddClick = {

                    },
                    swimlaneType = args.swimlaneType?.let { typeString ->
                        SwimlaneType.entries.find { it.type == typeString }
                    }


                ) {
                    onLoadMore()
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

    }
}