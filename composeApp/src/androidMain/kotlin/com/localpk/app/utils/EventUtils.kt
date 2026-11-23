package com.localpk.app.utils

import com.localpk.app.MainActivity
import com.singular.sdk.*

actual object EventUtils {
    actual fun trackEvent(eventName: String, properties: Map<String, String>) {
        MainActivity.sendEvent(eventName, properties)
    }
    
    actual fun sendAttribute(attributeName: String, attributeValue: String) {
        MainActivity.sendAttribute(attributeName, attributeValue)
    }
    
    actual fun postIdentification(identity: String) {
        MainActivity.postIdentification(identity)
    }
    
    actual fun setSingularCustomUserId(userId: String) {
        Singular.setCustomUserId(userId)
    }
    
    actual fun unsetSingularCustomUserId() {
        Singular.unsetCustomUserId()
    }
    
    actual fun trackSingularEvent(eventName: String, properties: Map<String, Any>) {
        Singular.event(eventName, properties)
    }
    
    actual fun trackSingularCustomRevenue(
        eventName: String, 
        currency: String, 
        amount: Double, 
        properties: Map<String, Any>
    ) {
        Singular.customRevenue(eventName, currency, amount, properties)
    }
} 