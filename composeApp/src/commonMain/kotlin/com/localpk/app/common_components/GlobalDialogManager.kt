package com.localpk.app.common_components

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.DrawableResource

object GlobalDialogManager {

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _dialogTitle = MutableStateFlow("")
    val dialogTitle: StateFlow<String> = _dialogTitle.asStateFlow()

    private val _dialogMessage = MutableStateFlow("")
    val dialogMessage: StateFlow<String> = _dialogMessage.asStateFlow()

    private val _confirmButtonText = MutableStateFlow("Yes")
    val confirmButtonText: StateFlow<String> = _confirmButtonText.asStateFlow()

    private val _cancelButtonText = MutableStateFlow("No")
    val cancelButtonText: StateFlow<String> = _cancelButtonText.asStateFlow()

    private val _onConfirm = MutableStateFlow<(() -> Unit)?>(null)
    val onConfirm: StateFlow<(() -> Unit)?> = _onConfirm.asStateFlow()

    private val _onCancel = MutableStateFlow<(() -> Unit)?>(null)
    val onCancel: StateFlow<(() -> Unit)?> = _onCancel.asStateFlow()

    private val _dialogType = MutableStateFlow(AlertDialogType.DEFAULT)
    val dialogType: StateFlow<AlertDialogType> = _dialogType.asStateFlow()

    private val _dialogIcon = MutableStateFlow<DrawableResource?>(null)
    val dialogIcon: StateFlow<DrawableResource?> = _dialogIcon.asStateFlow()

    private val _isAddressDialog = MutableStateFlow(false)
    val isAddressDialog: StateFlow<Boolean> = _isAddressDialog.asStateFlow()

    private val _showCancelButton = MutableStateFlow(true)
    val showCancelButton: StateFlow<Boolean> = _showCancelButton.asStateFlow()

    private val _isDismissible = MutableStateFlow(true)
    val isDismissible: StateFlow<Boolean> = _isDismissible.asStateFlow()

    /**
     * Show dialog with custom parameters
     */
    fun showDialog(
        title: String,
        message: String,
        confirmButton: String = "Yes",
        cancelButton: String = "No",
        onConfirm: () -> Unit = {},
        onCancel: () -> Unit = {},
        dialogType: AlertDialogType = AlertDialogType.DEFAULT,
        icon: DrawableResource? = null,
        isAddressDialog: Boolean? = false,
        showCancelButton: Boolean = true,
        isDismissible: Boolean = true
    ) {
        _dialogTitle.value = title
        _dialogMessage.value = message
        _confirmButtonText.value = confirmButton
        _cancelButtonText.value = cancelButton
        _onConfirm.value = onConfirm
        _onCancel.value = onCancel
        _dialogType.value = dialogType
        _showDialog.value = true
        _dialogIcon.value = icon
        _isAddressDialog.value = isAddressDialog ?: false
        _showCancelButton.value = showCancelButton
        _isDismissible.value = isDismissible
    }

    /**
     * Hide the dialog
     */
    fun hideDialog() {
        _showDialog.value = false
        _dialogTitle.value = ""
        _dialogMessage.value = ""
        _confirmButtonText.value = "Yes"
        _cancelButtonText.value = "No"
        _onConfirm.value = null
        _onCancel.value = null
        _dialogType.value = AlertDialogType.DEFAULT
        _showCancelButton.value = true
        _isDismissible.value = true
    }

    /**
     * Execute confirm action and hide dialog
     */
    fun executeConfirm() {
        _onConfirm.value?.invoke()
        hideDialog()
    }

    /**
     * Execute cancel action and hide dialog
     */
    fun executeCancel() {
        _onCancel.value?.invoke()
        hideDialog()
    }
}
