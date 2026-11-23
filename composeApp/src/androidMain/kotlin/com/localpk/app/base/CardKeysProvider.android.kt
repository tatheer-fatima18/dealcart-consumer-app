package com.localpk.app.base

actual val CARD_ACCOUNT_ID: String
    get() = BaseUrls.cardAccountId()

actual val CARD_HMAC_SECRET_KEY: String
    get() = BaseUrls.cardHmacSecretKey()

actual val CARD_PUBLIC_KEY: String
    get() = BaseUrls.cardPublicKey()