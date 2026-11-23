package com.localpk.app.features.promo.view

import com.localpk.app.base.BaseViewModel
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.promo.domain.GetPromosUseCase
import com.localpk.app.features.promo.domain.Promo
import com.localpk.app.features.promo.domain.PromoResponse
import com.localpk.app.features.promo.domain.PromoType
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.orEmpty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PromoListingViewModel(
    private val getPromosUseCase: GetPromosUseCase,
    sharedPreferencesManager: SharedPreferencesManager,
) : BaseViewModel<Any>() {

    // Promo listings
    private val _promosListing = MutableStateFlow<List<Promo>>(emptyList())
    val promosListing: StateFlow<List<Promo>> = _promosListing.asStateFlow()

    private val _foodPromosListing = MutableStateFlow<List<Promo>>(emptyList())
    val foodPromosListing: StateFlow<List<Promo>> = _foodPromosListing.asStateFlow()

    private val _groceryPromosListing = MutableStateFlow<List<Promo>>(emptyList())
    val groceryPromosListing: StateFlow<List<Promo>> = _groceryPromosListing.asStateFlow()

    // Promo status
    val isPromosLoaded = MutableStateFlow(false)
    val promoCode = MutableStateFlow(EMPTY_STRING)

    init {
        if (sharedPreferencesManager.isLoggedIn()) {
            getUserPromos()
        }
    }


    private fun getUserPromos() {
        unZipViewStateFlow(
            getPromosUseCase(Unit),
            onSuccess = {
                val promo: PromoResponse = it as PromoResponse
                val promos = promo.body.orEmpty()
                val groceryPromos = promos.filter { it.promoFor == PromoType.GROCERY.key }
                val foodPromos = promos.filter { it.promoFor == PromoType.FOOD.key }

                _promosListing.value = promos
                _foodPromosListing.value = foodPromos
                _groceryPromosListing.value = groceryPromos

                isPromosLoaded.value = true
            },
            onError = {
                isPromosLoaded.value = true
                GlobalToastManager.showToast("Unable to fetch promos. Please try again", type = SnackbarType.ERROR)
            }
        )
    }

    fun navigateToPromoDetail(promo: Promo, ticketNum: Int) {
//        navigate(
//            navigationProvider.getPushNavigator(
//                PromoCodesFragmentDirections.actionPromoCodesFragmentToPromoCodesDetailFragment(
//                    promo,
//                    ticketNum
//                )
//            )
//        )
    }
}



