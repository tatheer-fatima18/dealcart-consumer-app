package com.localpk.app.utils

import com.localpk.app.eventBridge
import com.localpk.app.attributeBridge
import com.localpk.app.identificationBridge
import com.localpk.app.singularCustomUserIdBridge
import com.localpk.app.singularEventBridge
import com.localpk.app.singularUnsetCustomUserIdBridge
import com.localpk.app.singularCustomRevenueBridge
import platform.Foundation.NSLog

actual object EventUtils {
    actual fun trackEvent(eventName: String, properties: Map<String, String>) {
        try {
            eventBridge(eventName, properties)
        } catch (e: Exception) {
            NSLog("🔴 [Kotlin EventUtils] Error in trackEvent: ${e.message}")
        }
    }
    
    actual fun sendAttribute(attributeName: String, attributeValue: String) {
        try {
            attributeBridge(attributeName, attributeValue)
        } catch (e: Exception) {
            NSLog("🔴 [Kotlin EventUtils] Error in sendAttribute: ${e.message}")
        }
    }
    
    actual fun postIdentification(identity: String) {
        try {
            identificationBridge(identity)
        } catch (e: Exception) {
            NSLog("🔴 [Kotlin EventUtils] Error in postIdentification: ${e.message}")
        }
    }
    
    actual fun setSingularCustomUserId(userId: String) {
        try {
            singularCustomUserIdBridge(userId)
        } catch (e: Exception) {
            NSLog("🔴 [Kotlin EventUtils] Error in setSingularCustomUserId: ${e.message}")
        }
    }
    
    actual fun unsetSingularCustomUserId() {
        try {
            singularUnsetCustomUserIdBridge()
        } catch (e: Exception) {
            NSLog("🔴 [Kotlin EventUtils] Error in unsetSingularCustomUserId: ${e.message}")
        }
    }
    
    actual fun trackSingularEvent(eventName: String, properties: Map<String, Any>) {
        try {
            singularEventBridge(eventName, properties)
        } catch (e: Exception) {
            NSLog("🔴 [Kotlin EventUtils] Error in trackSingularEvent: ${e.message}")
        }
    }
    
    actual fun trackSingularCustomRevenue(
        eventName: String, 
        currency: String, 
        amount: Double, 
        properties: Map<String, Any>
    ) {
        try {
            singularCustomRevenueBridge(eventName, currency, amount, properties)
        } catch (e: Exception) {
            NSLog("🔴 [Kotlin EventUtils] Error in trackSingularCustomRevenue: ${e.message}")
        }
    }
}