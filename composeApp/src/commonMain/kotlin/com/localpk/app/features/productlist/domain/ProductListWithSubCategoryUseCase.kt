package com.localpk.app.features.productlist.domain

import com.localpk.app.api.AppRepository
import com.localpk.app.base.FlowUseCase
import com.localpk.app.base.ViewState
import com.localpk.app.base.domain.ErrorResponseViewStateMapper
import com.localpk.app.features.banner.data.Banner
import com.localpk.app.features.category.data.CategoryUiModel
import com.localpk.app.features.productlist.data.HeaderRecyclerAdapterConstants
import com.localpk.app.features.productlist.data.Product
import com.localpk.app.features.productlist.data.ProductFragmentArgs
import com.localpk.app.features.productlist.data.ProductHeaderAdapterUiData
import com.localpk.app.features.productlist.data.ProductList
import com.localpk.app.network.ResponseResult
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.getValueOrDefault
import com.localpk.app.utils.isNotNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductListWithSubCategoryUseCase(
    private val appRepository: AppRepository,
    private val errorResponseViewStateMapper: ErrorResponseViewStateMapper,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<ProductFragmentArgs, ViewState<List<ProductHeaderAdapterUiData>>>(dispatcher) {

    override fun execute(parameters: ProductFragmentArgs): Flow<ViewState<List<ProductHeaderAdapterUiData>>> =
        flow {
            emit(ViewState.Loading)

            appRepository.getProducts(parameters.parentId, FIRST_PAGE).run {
                when (this) {
                    is ResponseResult.Success -> {
                        emit(
                            ViewState.Success(
                                getSortedProductsWithSubcategoryTitle(
                                    parameters.subCategories,
                                    data.body,
                                    if (parameters.categoryBanner.isNullOrEmpty()) null
                                    else parameters.categoryBanner
                                )
                            )
                        )
                    }

                    else -> errorResponseViewStateMapper.map(this)
                }
            }
        }

    private fun getSortedProductsWithSubcategoryTitle(
        subcategoryList: List<CategoryUiModel>,
        body: ProductList,
        banner: List<Banner>?
    ): List<ProductHeaderAdapterUiData> {
        if (body.results.isNullOrEmpty()) return emptyList()

        val productListWithHeaders = mutableListOf<ProductHeaderAdapterUiData>()

        val oosProductsList = mutableListOf<ProductHeaderAdapterUiData>()

        val productResponseList = body.results.orEmpty().toMutableList()

        if (banner.isNotNull()) {
            productListWithHeaders.add(
                ProductHeaderAdapterUiData(
                    type = HeaderRecyclerAdapterConstants.BANNER,
                    parentData = null,
                    parentPosition = null,
                    childData = null,
                    bannerData = banner?.first()
                )
            )
        }

        oosProductsList.addAll(
            productResponseList.filter {
                (it.inventories?.firstOrNull()?.dcImsQuantityAvailable.getValueOrDefault()) <= 0
            }.map {
                ProductHeaderAdapterUiData(
                    type = HeaderRecyclerAdapterConstants.ITEM,
                    parentData = null,
                    parentPosition = null,
                    childData = it.map(),
                    bannerData = null
                )
            }
        )

        //Remove oos products
        productResponseList.removeAll {
            (it.inventories?.firstOrNull()?.dcImsQuantityAvailable.getValueOrDefault()) <= 0
        }

        //If there are no subcategories
        if (subcategoryList.isEmpty()) {
            productListWithHeaders.addAll(productResponseList.map { it.mapToProductHeaderAdapterUiData() })
            productListWithHeaders.addAll(oosProductsList)
        } else {
            subcategoryList.forEach { subcategory ->
                // Add header subcategory
                productListWithHeaders.add(
                    ProductHeaderAdapterUiData(
                        type = HeaderRecyclerAdapterConstants.HEADER,
                        parentData = subcategory,
                        parentPosition = productListWithHeaders.size,
                        childData = null,
                        bannerData = null
                    )
                )

                val preferredProductList = mutableListOf<Pair<Product, Int>>()
                val nonPreferredProductList = mutableListOf<Product>()

                productResponseList.forEach { product ->
                    product.productCategory.find { it.categoryId == subcategory.id }?.let {

                        if (it.isPreferred) {
                            preferredProductList.add(Pair(product, it.preferredOrder ?: 0))
                        } else {
                            nonPreferredProductList.add(product)
                        }

                    }
                }


                //Sort preferredProductList by preferred order
                preferredProductList.sortBy { it.second }

                productListWithHeaders.addAll(
                    preferredProductList.map {
                        ProductHeaderAdapterUiData(
                            type = HeaderRecyclerAdapterConstants.ITEM,
                            parentData = null,
                            parentPosition = null,
                            childData = it.first.map(),
                            bannerData = null
                        )
                    }
                )

                //Sort nonPreferredProductList by name
                nonPreferredProductList.sortBy { it.name }

                productListWithHeaders.addAll(
                    nonPreferredProductList.map {
                        ProductHeaderAdapterUiData(
                            type = HeaderRecyclerAdapterConstants.ITEM,
                            parentData = null,
                            parentPosition = null,
                            childData = it.map(),
                            bannerData = null
                        )
                    }
                )

            }
            productListWithHeaders.addAll(oosProductsList)
        }
        return productListWithHeaders
    }
}