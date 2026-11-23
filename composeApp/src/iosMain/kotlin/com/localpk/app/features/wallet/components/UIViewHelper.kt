package com.localpk.app.features.wallet.components

import platform.UIKit.UIView

object UIViewHelper {
    private var paymentElementView: UIView? = null
    private var easyPaisaElementView: UIView? = null
    
    fun setPaymentElementView(view: UIView?) {
        paymentElementView = view
    }
    
    fun getPaymentElementView(): UIView? {
        return paymentElementView
    }
    
    fun setEasyPaisaElementView(view: UIView?) {
        easyPaisaElementView = view
    }
    
    fun getEasyPaisaElementView(): UIView? {
        return easyPaisaElementView
    }
} 