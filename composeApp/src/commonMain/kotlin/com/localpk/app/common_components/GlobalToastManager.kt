package com.localpk.app.common_components

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GlobalToastManager {

    private val _showToast = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast.asStateFlow()

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage.asStateFlow()

    private val _toastType = MutableStateFlow(SnackbarType.DEFAULT)
    val toastType: StateFlow<SnackbarType> = _toastType.asStateFlow()

    private val _toastDuration = MutableStateFlow(4000L)
    val toastDuration: StateFlow<Long> = _toastDuration.asStateFlow()

    /**
     * Show toast with custom parameters
     */
    fun showToast(
        message: String,
        type: SnackbarType = SnackbarType.DEFAULT,
        duration: Long = 4000L
    ) {
        _toastMessage.value = message
        _toastType.value = type
        _toastDuration.value = duration
        _showToast.value = true
    }

    /**
     * Show success toast
     */
    fun showSuccessToast(message: String, duration: Long = 4000L) {
        showToast(message, SnackbarType.SUCCESS, duration)
    }

    /**
     * Show error toast
     */
    fun showErrorToast(message: String, duration: Long = 4000L) {
        showToast(message, SnackbarType.ERROR, duration)
    }

    /**
     * Show default toast
     */
    fun showDefaultToast(message: String, duration: Long = 4000L) {
        showToast(message, SnackbarType.DEFAULT, duration)
    }

    /**
     * Hide the toast
     */
    fun hideToast() {
        _showToast.value = false
        _toastMessage.value = ""
        _toastType.value = SnackbarType.DEFAULT
        _toastDuration.value = 4000L
    }

    /**
     * Execute toast dismiss action and hide toast
     */
    fun executeToastDismiss() {
        hideToast()
    }
}
