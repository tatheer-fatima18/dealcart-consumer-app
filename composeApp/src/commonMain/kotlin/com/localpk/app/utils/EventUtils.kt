package com.localpk.app.utils

expect object EventUtils {
    fun trackEvent(eventName: String, properties: Map<String, String> = emptyMap())
    fun sendAttribute(attributeName: String, attributeValue: String)
    fun postIdentification(identity: String)
    fun setSingularCustomUserId(userId: String)
    fun unsetSingularCustomUserId()
    fun trackSingularEvent(eventName: String, properties: Map<String, Any> = emptyMap())
    fun trackSingularCustomRevenue(
        eventName: String, 
        currency: String, 
        amount: Double, 
        properties: Map<String, Any> = emptyMap()
    )
} 