package com.localpk.app.features.rewards.viewModel

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.restaurant.domain.RequestData
import com.localpk.app.features.rewards.data.CoinsHistory
import com.localpk.app.features.rewards.data.CoinsHistoryListUiData
import com.localpk.app.features.rewards.domain.CoinsHistoryUseCase
import com.localpk.app.utils.FIRST_PAGE
import com.localpk.app.utils.WALLET_HISTORY_LIMIT
import com.localpk.app.utils.orEmptyArrayList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CoinsHistoryViewModel(
    private val coinsHistoryUseCase: CoinsHistoryUseCase
) : BaseViewModel<Any>() {

    private val _coinsHistory = MutableStateFlow<List<CoinsHistoryListUiData>>(emptyList())
    val coinsHistory: StateFlow<List<CoinsHistoryListUiData>> = _coinsHistory.asStateFlow()

    private val _totalCoins = MutableStateFlow("")
    val totalCoins: StateFlow<String> = _totalCoins.asStateFlow()

    val isCoinsHistoryLoaded = MutableStateFlow(false)


    var currentPage = FIRST_PAGE

    fun fetchCoinsHistory(
        source: String,
        limit: Int,
        page: Int,
    ) {
        unZipViewStateFlow(
            coinsHistoryUseCase(
                RequestData(
                    limit = limit,
                    page = page,
                    source = source
                )
            ),
            onSuccess = { it ->
                val response = it as CoinsHistory
                if (limit < WALLET_HISTORY_LIMIT) {
                    _coinsHistory.value =
                        response.transactionHistory.map { it.toMapCoinsHistoryListUiData() }
                            .orEmptyArrayList()
                    _totalCoins.value = it.formattedTotalCoinsBalance()
                    isCoinsHistoryLoaded.value = true
                } else {
                    val currentProducts = _coinsHistory.value
                    val currentProductIds = currentProducts.map { it.transactionId }

                    val uniqueFrequentProducts =
                        response.transactionHistory.map { it.toMapCoinsHistoryListUiData() }
                            .orEmptyArrayList()
                            .filterNot { currentProductIds.contains(it.transactionId) }

                    if (uniqueFrequentProducts.isNotEmpty()) {
                        _coinsHistory.value = (currentProducts + uniqueFrequentProducts)
                    }
                    _totalCoins.value = it.formattedTotalCoinsBalance()
                    isCoinsHistoryLoaded.value = true
                }
            }
        )
    }
}