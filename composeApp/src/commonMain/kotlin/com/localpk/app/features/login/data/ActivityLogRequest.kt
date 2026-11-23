package com.localpk.app.features.login.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class ActivityLogRequest(
    val referredById: Long,
    val referredUserUniqueIdentifier: String,
    val appSignupStatus: Boolean,
    val isNewUser: Boolean? = null,
    val productId: Long? = null,
    val groupId: Long? = null,
    val referredUserId: Long? = null,
)

@Serializable
data class SignupStatusRequest(
    val referredById: Long,
    val referredUserId: Long,
    val referredUserUniqueIdentifier: String,
    val isNewUser: Boolean?
)

@Serializable
data class CheckIfCustomerExistsRequest(
    val phoneNumber: String
)

@Serializable
data class CheckIfCustomerExistsResponse(
    val success: Boolean,
    val body: Boolean
) : JavaSerializable