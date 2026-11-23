package com.localpk.app.features.login.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.features.login.presentation.OTPScreen
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AuthUiModel(
    val phone: String,
    var hash: String,
    var user: User? = null,
    var fullName: String = EMPTY_STRING,
    var otp: String = EMPTY_STRING,
    val source: String = EMPTY_STRING
) : JavaSerializable {
    fun phoneNumberWithCountryCodeToPhone(): String {
        return if (phone.startsWith("0")) {
            "+92${phone.substring(1)}"
        } else {
            phone
        }
    }

    @Transient
    var listener: () -> Unit = {}
}

enum class ScreenSource(val key: String) {
    CART("cart"),
    PROMO_CART("promo_cart"),
    ORDERS_LISTING("orders_listing"),
    SHARE_TO_FRIEND("share_to_friend"),
    WEB_VIEW("web_view"),
    PROMO_LISTING("promo_listing"),
    SHARE_AND_EARN("share_and_earn"),
    SIGNUP_SCREEN("signup_screen"),
    WALLET("wallet"),
    MATCH_DETAIL_SCREEN("match_details_screen"),

}

fun AuthUiModel.toOTPScreen(): OTPScreen {
    return OTPScreen(
        phone = phone,
        hash = hash,
        fullName = fullName,
        otp = otp,
        source = source,
        id = user?.id ?: 0L,
        firstName = user?.firstName,
        lastName = user?.lastName,
        phoneNumber = user?.phoneNumber ?: EMPTY_STRING,
        email = user?.email ?: EMPTY_STRING,
        isVerified = user?.isVerified ?: false,
        isCL = user?.isCL ?: false,
        gender = user?.gender,
        organization = user?.organization
    )
} 