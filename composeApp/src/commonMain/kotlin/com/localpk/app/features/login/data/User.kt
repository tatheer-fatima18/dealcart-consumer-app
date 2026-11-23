package com.localpk.app.features.login.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: Long = 0L,
    var firstName: String? = EMPTY_STRING,
    var lastName: String? = EMPTY_STRING,
    var phoneNumber: String = EMPTY_STRING,
    var email: String? = EMPTY_STRING,
    var isVerified: Boolean,
    var isCL: Boolean,
    val gender: String? = EMPTY_STRING,
    val organization: String? = EMPTY_STRING
) : JavaSerializable
