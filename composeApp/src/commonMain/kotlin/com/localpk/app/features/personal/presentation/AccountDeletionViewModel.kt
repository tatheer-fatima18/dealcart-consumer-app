package com.localpk.app.features.personal.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.common_components.GlobalToastManager
import com.localpk.app.common_components.SnackbarType
import com.localpk.app.features.personal.domain.DeleteCustomerUseCase

class AccountDeletionViewModel(
    private val deleteCustomerUseCase: DeleteCustomerUseCase
) : BaseViewModel<Any>() {

    fun deleteCustomer(successCallback: () -> Unit) {
        unZipViewStateFlow(
            deleteCustomerUseCase(Unit),
            onSuccess = {
                successCallback()
            },
            onError = {
                GlobalToastManager.showToast(
                    message = it.message,
                    type = SnackbarType.ERROR
                )
            }
        )
    }


}