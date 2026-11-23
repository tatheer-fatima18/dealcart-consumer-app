package com.localpk.app.features.personal.data

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDetailsResponse(val success: Boolean, val body: ProfileData)

@Serializable
data class ProfileData(
    val profileDetails: ProfileDetails?
)

@Serializable
data class ProfileDetails(
    val dateOfBirth: String? = null,
    val email: String? = null,
    val gender: String? = null,
    val isEmailVerified: Boolean? = false,
    val name: String? = null,
    val firstName: String? = null,
    val lastName: String? = null
)


fun defaultProfile(name: String): ProfileDetails {
    return ProfileDetails(
        name = name,
    )
}

@Serializable
data class ProfileArgs(
    val customerId: Long?
)