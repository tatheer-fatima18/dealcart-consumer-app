package com.localpk.app.features.product_detail.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.features.flash_deals.viewModel.FlashDealViewModel
import com.localpk.app.features.product_detail.components.DeliveryInfoComponent
import com.localpk.app.features.product_detail.components.FlashDealComponent
import com.localpk.app.features.product_detail.components.FrequentlyBoughtTogetherComponent
import com.localpk.app.features.product_detail.components.Header
import com.localpk.app.features.product_detail.components.ProductDescription
import com.localpk.app.features.product_detail.components.ProductDetailSkeleton
import com.localpk.app.features.product_detail.components.ProductImageComponent
import com.localpk.app.features.product_detail.components.ProductNameAndPriceComponent
import com.localpk.app.features.product_detail.components.ProductVariantComponent
import com.localpk.app.features.product_detail.components.StoreNameAndImageComponent
import com.localpk.app.features.product_detail.viewModel.ProductDetailViewModel
import com.localpk.app.features.productlist.data.ProductDetailUiData
import com.localpk.app.features.productlist.data.ProductUiData
import com.localpk.app.features.productlist.data.StoreType
import com.localpk.app.features.productlist.data.VariantDetail
import com.localpk.app.features.recommendation_engine.viewModel.RecommendationEngineViewModel
import com.localpk.app.features.search.presentation.SearchScreen
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.SurfaceBackground
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import com.localpk.app.utils.orEmptyArrayList
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class ProductDetailScreen(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Double,
    val finalPrice: Double,
    val purchasePrice: Double,
    val discountedPrice: Double,
    val inventoryQty: Int,
    val isSponsored: Boolean,
    var slot: Int,
    val isFromNotification: Boolean = false,
    val isFlashDeal: Boolean = false,
    val flashDealId: Long = ZERO_LONG,
    val isDeepLink: Boolean = false,
    var itemQuantity: Int,
    val hasVariant: Boolean? = false,
    val showMrp: Boolean?,
    val purchaseLimit: Int?,
    val storeName: String = EMPTY_STRING,
    val storeImage: String = EMPTY_STRING,
    val isDeal: Boolean = false,
    val hasVariants: Boolean = false
) : Screen {

    @Composable
    override fun Content() {

        val args = ProductUiData(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            price = price,
            finalPrice = finalPrice,
            discountedPrice = discountedPrice,
            inventoryQty = inventoryQty,
            purchasePrice = purchasePrice,
            isSponsored = isSponsored,
            slot = slot,
            showMrp = showMrp,
            purchaseLimit = purchaseLimit,
            itemQuantity = itemQuantity,
            productCategory = null,
            isFavourite = false,
            isBackInStockSubscribed = false,
            isFromNotification = isFromNotification,
            isFlashDeal = isFlashDeal,
            isDeal = isDeal,
            flashDealId = flashDealId,
            hasVariant = hasVariant,
            storeName = storeName,
            storeImage = storeImage,
            hasVariants = hasVariants,
            isDeepLink = isDeepLink
        )

        val productDetailViewModel = koinViewModel<ProductDetailViewModel>(
            key = "ProductDetailViewModel_$args"
        ) {
            parametersOf(args)
        }

        val recommendationEngineViewModel = koinViewModel<RecommendationEngineViewModel>()

        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val flashDealsViewModel = koinViewModel<FlashDealViewModel>()
        val cartViewModel = koinViewModel<CartViewModel>()

        val productDetail by productDetailViewModel.productDetailLiveData.collectAsState()
        val actionButtonVisibility by productDetailViewModel.actionButtonVisibility.collectAsState()
        val isLoggedIn by productDetailViewModel.isLoggedIn.collectAsState()
        val selectedVariantDetails by productDetailViewModel.selectedVariantDetails.collectAsState()
//        val actionError by flashDealsViewModel.actionError.observeAsState()
        val recommendationList by recommendationEngineViewModel.recommendationList.collectAsState()
        val flashDealsDetailLiveData by flashDealsViewModel.flashDealsDetail.collectAsState()
//        val dynamicLinkLiveData by flashDealsViewModel.dynamicLinkLiveData.observeAsState()
//        val showDialog by mainActivityViewModel.showFoodDialog
        val cartState by cartViewModel.cartState.collectAsState()


        if (args.isFlashDeal) {
            productDetailViewModel._actionButtonVisibility.value = true
            flashDealsViewModel.fetchFlashDealsDetail(args.flashDealId)
        }
        if (sharedPreferencesManager.isLoggedIn()) {
            recommendationEngineViewModel.getRecommendationItems(
                "bought-together/${args.id}",
                "product-details",
                args.id,
                cartState
            )
        }
        val isLoading = productDetail == null
        flashDealsDetailLiveData?.copy(flashDealId = args.flashDealId)
            ?.let { productDetailViewModel.updateProductDetail(it) }

        StatusBarIconColor(backgroundColor = Color.White)
        Surface(color = if (isLoading) Color.White else SurfaceBackground) {
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    isLoading -> {
                        ProductDetailSkeleton()
                    }

                    productDetail.isNotNull() -> {
                        ProductDetail(
                            productDetailLiveData = productDetail,
                            argsData = args,
                            isLoggedIn = isLoggedIn,
                            recommendationList = recommendationList,
                            selectedVariantDetailsLiveData = selectedVariantDetails,
                            flashDealsDetailLiveData = flashDealsDetailLiveData,
                            productDetailViewModel = productDetailViewModel
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ProductDetail(
        productDetailLiveData: ProductDetailUiData?,
        argsData: ProductUiData,
        isLoggedIn: Boolean,
        recommendationList: List<ProductUiData>,
        selectedVariantDetailsLiveData: List<VariantDetail>?,
        flashDealsDetailLiveData: ProductDetailUiData?,
        productDetailViewModel: ProductDetailViewModel
    ) {
        val navigator = LocalNavigator.currentOrThrow
        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
        ) {
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                    .border(
                        BorderStroke(width = 1.dp, color = BorderPrimaryDefault),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                onBackClick = { navigator.pop() },
                onSearchClick = {
                    navigator.push(SearchScreen(false))
                }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(0.dp))
                ProductImageComponent(productDetailImages = productDetailLiveData?.images)
                if (productDetailLiveData?.isFlashDeal == true) {
                    FlashDealComponent(
                        remainingTime = flashDealsDetailLiveData?.timeLeft.getValueOrDefault(),
                        remainingPersons = productDetailLiveData.remainingPersons.getValueOrDefault()
                    )
                }
                ProductNameAndPriceComponent(
                    showMrp = argsData.showMrp == true,
                    productName = productDetailLiveData?.name.orEmpty(),
                    productPrice = productDetailLiveData?.formattedGroupPrice().default(),
                    productDiscount = productDetailLiveData?.percentageOff().default(),
                    productActualPrice = productDetailLiveData?.formattedPrice().default()
                )
                if (productDetailLiveData?.isFlashDeal == false && selectedVariantDetailsLiveData.orEmptyArrayList()
                        .isNotEmpty()
                ) {
                    ProductVariantComponent(
                        productDetailLiveData.attributes.orEmptyArrayList(),
                        selectedVariantDetailsLiveData,
                        productDetailViewModel = productDetailViewModel
                    )
                }
                if (productDetailLiveData?.storeName.default()
                        .isNotEmpty() && productDetailLiveData?.storeName.default() != StoreType.DEAL_CART.storeName
                ) {
                    StoreNameAndImageComponent(
                        storeName = productDetailLiveData?.storeName.default(),
                        storeImageUrl = productDetailLiveData?.storeImage.default()
                    )
                }
                if (productDetailLiveData?.isProductToBeDeliveredByThirdParty == true)
                    DeliveryInfoComponent(
                        deliveryDateRange = productDetailLiveData.deliveryDateRange,
                    )
                if (productDetailLiveData?.description?.isNotEmpty() == true) {
                    ProductDescription(
                        productDescription = productDetailLiveData.description,
                        productShortDescription = productDetailLiveData.shortDescription
                    ) {}
                }
                if (isLoggedIn && recommendationList.isNotEmpty()) {
                    FrequentlyBoughtTogetherComponent(
                        recommendationList = recommendationList,
                        cardClick = {
//                            eventLogger.onClickProductProductDetails(it.name)
                        },
                        onAddClick = {
//                            if (it.hasVariants.not()) {
//                                mainActivityViewModel.addToCartWithConfirmation {
//                                    eventLogger.onClickPlusProductDetails(it.name)
//                                    eventLogger.addToCartMergn(
//                                        it.id,
//                                        it.name,
//                                        it.itemQuantity.value,
//                                        it.price,
//                                        "recommendation_product_detail"
//                                    )
//                                    eventLogger.clickQuickBuyREPdp(it.name, 1, it.id)
//                                    mainActivityViewModel.addToCart(
//                                        CartProductType.INDIVIDUAL,
//                                        it.id
//                                    ) { _ -> }
//                                }
//                            }
//                            else {
//                                navigate(
//                                    ProductDetailFragmentDirections.actionMainToProductDetail(
//                                        it
//                                    )
//                                )
//                            }
                        },
                    )
                }
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
}