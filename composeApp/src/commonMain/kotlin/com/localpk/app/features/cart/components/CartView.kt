package com.localpk.app.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.localpk.app.features.cart.data.BundleDetails
import com.localpk.app.features.cart.data.CartPromo
import com.localpk.app.features.cart.data.CartUiModel
import com.localpk.app.features.cart.data.ProductInfo
import com.localpk.app.features.cart.data.DeliveryType
import com.localpk.app.features.cart.viewModel.CartViewModel
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.SurfaceBackgroundSuccess
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotZero
import org.koin.compose.koinInject

@Composable
fun CartView(
    singleProductItems: List<CartUiModel>,
    flashDealProductItems: List<CartUiModel>,
    promoDetails: CartPromo?,
    isPromoCodeValidated: Boolean,
    isPostexDelivery: Boolean,
    isBucketChecked: Boolean,
    storeName: String,
    toggleBucketCheck: (check: Boolean) -> Unit,
    formattedDeliveryDate: String,
    getMinOrderVal: Int,
    cartTotal: Double,
    amountNeededForFreeDelivery: Int,
    formattedDeliveryFee: String,
    isBundle: Boolean,
    onAddPromoCode: () -> Unit,
    onRemovePromoCode: () -> Unit,
    showBundleDialog: Boolean? = null,
    bundleQuantity: Int? = null,
    dcCartBundle: BundleDetails? = null,
    onClickDeliveryFee: () -> Unit,
    deliveryType: DeliveryType,
    isSameDeliveryEligible: Boolean
) {
    val cartViewModel: CartViewModel = koinInject()

    val localCartItems by cartViewModel.localCartItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isPostexDelivery.not() && RemoteConfigs.isBundleEnabled()
        ) {
            BundleToggleSwitch(
                isFlashDealList = flashDealProductItems.isNotEmpty(),
                isBundle = isBundle
            )
        }
        CartDCCityDeliveryInfo(
            isBucketChecked = isBucketChecked,
            storeName = storeName,
            isPostexDelivery = isPostexDelivery,
            toggleBucketCheck = toggleBucketCheck,
            formattedDeliveryDate = formattedDeliveryDate,
            getMinOrderVal = getMinOrderVal,
            cartTotal = cartTotal,
            amountNeededForFreeDelivery = amountNeededForFreeDelivery,
            formattedDeliveryFee = formattedDeliveryFee,
            onClickDeliveryFee = onClickDeliveryFee,
            deliveryType = deliveryType,
            isSameDeliveryEligible = isSameDeliveryEligible
        )
        if (isPostexDelivery.not() && isBundle) {
            BundleCard(
                bundle = dcCartBundle,
                cartProductSingleItems = singleProductItems,
                quantity = bundleQuantity.getValueOrDefault(),
                showBundleDialog = showBundleDialog == true
            )
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                singleProductItems.forEach { singleProduct ->
                    SwipeToDeleteContainer(
                        item = singleProduct,
                        onDelete = {
                            val cartProductId =
                                localCartItems[singleProduct.productId]?.cartProductId.getValueOrDefault()
                            if (cartProductId.isNotZero())
                                cartViewModel.removeCartItem(
                                    isCart = true,
                                    productData = ProductInfo(
                                        productId = singleProduct.productId,
                                        cartProductId = cartProductId,
                                        quantity = localCartItems[singleProduct.productId]?.quantity
                                            .getValueOrDefault(),
                                        price = singleProduct.discountedPrice,
                                        productName = singleProduct.productName
                                    ),
                                    successCallback = { cartViewModel.fetchUserCart() }
                                )
                        }
                    ) {
                        SingleCartProductItem(
                            singleProduct = singleProduct
                        )
                    }
                }
                flashDealProductItems.forEach { flashDealProduct ->
                    SwipeToDeleteContainer(
                        item = flashDealProduct,
                        onDelete = {
                            val cartProductId =
                                localCartItems[flashDealProduct.productId]?.cartProductId.getValueOrDefault()
                            if (cartProductId.isNotZero())
                                cartViewModel.removeCartItem(
                                    isCart = true,
                                    productData = ProductInfo(
                                        productId = flashDealProduct.flashDealId.getValueOrDefault(),
                                        cartProductId = cartProductId,
                                        quantity = localCartItems[flashDealProduct.productId]?.quantity
                                            .getValueOrDefault(),
                                        price = flashDealProduct.discountedPrice,
                                        productName = flashDealProduct.productName
                                    ),
                                    successCallback = { cartViewModel.fetchUserCart() }
                                )
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = SurfaceBackgroundSuccess,
                                    shape = RoundedCornerShape(size = 8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            SingleCartProductItem(
                                flashDealProduct
                            )
                        }
                    }
                }
            }
            CartPromoCodeView(
                promoDetails,
                isPromoCodeValidated,
                onAddPromoCode,
                onRemovePromoCode
            )
        }
    }
}