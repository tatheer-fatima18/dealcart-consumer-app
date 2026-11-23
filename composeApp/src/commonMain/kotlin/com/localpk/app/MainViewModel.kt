package com.localpk.app

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.user_location.presentation.UserLocationScreen

class MainViewModel : BaseViewModel<Any>() {
    fun navigateToUserLocation() {
        push(
            UserLocationScreen()
        )
    }

}