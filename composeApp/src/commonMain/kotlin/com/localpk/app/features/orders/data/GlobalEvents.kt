package com.localpk.app.features.orders.data

import com.localpk.app.features.bottomsheet.data.GeneralBottomSheetUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object GlobalEvents {
    private val _isOrderCancelled = MutableStateFlow(false)
    val isOrderCancelled: StateFlow<Boolean> = _isOrderCancelled

    private val _isCartFetchNeeded = MutableStateFlow(true)
    val isCartFetchNeeded: StateFlow<Boolean> = _isCartFetchNeeded

    private val _argsGeneralBottomSheetScreen = MutableStateFlow<GeneralBottomSheetUiData?>(null)
    val argsGeneralBottomSheetScreen: StateFlow<GeneralBottomSheetUiData?> = _argsGeneralBottomSheetScreen

    fun updateIsOrderCancelled(args: Boolean) {
        _isOrderCancelled.value = args
    }

    fun updateIsCartFetchNeeded(args: Boolean) {
        _isCartFetchNeeded.value = args
    }

    fun updateGeneralBottomSheetScreen(args: GeneralBottomSheetUiData?) {
        _argsGeneralBottomSheetScreen.value = args
    }
}
