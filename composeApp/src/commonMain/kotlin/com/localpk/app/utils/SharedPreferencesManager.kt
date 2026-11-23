package com.localpk.app.utils

import com.localpk.app.utils.PreferenceKeys.EMAIL
import com.localpk.app.utils.PreferenceKeys.LATITUDE
import com.localpk.app.utils.PreferenceKeys.LONGITUDE
import com.localpk.app.utils.PreferenceKeys.SELECTED_CITY
import com.russhwolf.settings.Settings

interface SharedPreferencesManager {

    fun clear()

    fun getInt(key: String, defaultValue: Int): Int

    fun getIntOrNull(key: String): Int?

    fun getLong(key: String, defaultValue: Long): Long

    fun getLongOrNull(key: String): Long?

    fun getString(key: String, defaultValue: String): String

    fun getStringOrNull(key: String): String?

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun getBooleanOrNull(key: String): Boolean?

    fun putInt(key: String, value: Int)

    fun putString(key: String, value: String)

    fun putLong(key: String, value: Long)

    fun putBoolean(key: String, value: Boolean)

    fun hasKey(key: String): Boolean

    fun remove(key: String)

    fun getToken(): String
    fun setToken(value: String)

    fun getUserId(): Long
    fun setUserId(value: Long)

    fun setLogin(success: Boolean)
    fun isLoggedIn(): Boolean

    fun logout()

    fun getUserName(): String
    fun setUserName(name: String)

    fun getMyReferralCode(): String
    fun setMyReferralCode(referralCode: String)

    fun getPhoneNumber(): String
    fun setPhoneNumber(phoneNumber: String)

    fun getGender(): String
    fun setGender(gender: String)

    fun getEmail(): String
    fun setEmail(email: String)

    fun getFCMToken(): String
    fun setFCMToken(fcmToken: String)

    fun getReferralUserCode(): String
    fun setReferralUserCode(referralCode: String)

    fun getIsCommunityLeader(): Boolean
    fun setIsCommunityLeader(flag: Boolean)

    fun getWarehouseId(): Long
    fun setWarehouseId(warehouseId: Long)

    fun getSelectedWarehouseId(): Long
    fun setSelectedWarehouseId(warehouseId: Long)

    fun getSelectedCity(): String
    fun setSelectedCity(city: String)

    fun setLongitude(longitude: String)
    fun getLongitude(): String

    fun setLatitude(latitude: String)
    fun getLatitude(): String

    fun getProductPastSearch(): List<String>
    fun removeProductPastSearch(text: String)
    fun removeAllProductPastSearch()
    fun setProductPastSearch(text: String)
    fun getRestaurantPastSearch(): List<String>
    fun removeRestaurantPastSearch(text: String)
    fun removeAllRestaurantPastSearch()
    fun setRestaurantPastSearch(text: String)

    fun setFlashDeal(key: String)
    fun getFlashDeal(): String

    fun getDefaultAddressId(): Long
    fun setDefaultAddressId(id: Long)

    fun getReferralLink(): String
    fun setReferralLink(link: String)

    fun getContributionValue(): Int
    fun setContributionValue(value: Int)

    fun getSaveContributionValue(): Boolean
    fun setSaveContributionValue(value: Boolean)

    fun getCustomContributionValue(): Int
    fun setCustomContributionValue(value: Int)

    fun getDefaultAddress(): String
    fun setDefaultAddress(address: String)

    fun setJazzPhoneNumber(phoneNumber: String)
    fun getJazzPhoneNumber(): String

    fun setJazzId(id: String)
    fun getJazzId(): String

}

class SharedPreferencesManagerImpl(
    private val settingsFactory: MultiplatformSettingsFactory
) : SharedPreferencesManager {
    private val settings: Settings by lazy {
        settingsFactory.getSettings()
    }

    override fun clear() {
        settings.clear()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return settings.getInt(key, defaultValue)
    }

    override fun getIntOrNull(key: String): Int? {
        return settings.getIntOrNull(key)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return settings.getLong(key, defaultValue)
    }

    override fun getLongOrNull(key: String): Long? {
        return settings.getLongOrNull(key)
    }

    override fun getString(key: String, defaultValue: String): String {
        return settings.getString(key, defaultValue)
    }

    override fun getStringOrNull(key: String): String? {
        return settings.getStringOrNull(key)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return settings.getBoolean(key, defaultValue)
    }

    override fun getBooleanOrNull(key: String): Boolean? {
        return settings.getBooleanOrNull(key)
    }

    override fun putInt(key: String, value: Int) {
        settings.putInt(key, value)
    }

    override fun putString(key: String, value: String) {
        settings.putString(key, value)
    }

    override fun putLong(key: String, value: Long) {
        settings.putLong(key, value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
    }

    override fun hasKey(key: String): Boolean {
        return settings.hasKey(key)
    }

    override fun remove(key: String) {
        settings.remove(key)
    }

    override fun logout() {
        setLogin(false)
        setUserId(0)
        setToken(EMPTY_STRING)
        setUserName(EMPTY_STRING)
        setPhoneNumber(EMPTY_STRING)
        setMyReferralCode(EMPTY_STRING)
//        setReferralGroupCode(EMPTY_STRING)
        setReferralUserCode(EMPTY_STRING)
        setReferralLink(EMPTY_STRING)
//        setGroupReferralLink(EMPTY_STRING)
        setIsCommunityLeader(false)
//        setBestScore(ZERO_LONG)
        setContributionValue(ZERO_INT)
        setCustomContributionValue(ZERO_INT)
        setSaveContributionValue(false)
    }

    override fun getToken() = getString(AUTH_TOKEN_KEY, EMPTY_STRING).default()
    override fun setToken(value: String) = putString(AUTH_TOKEN_KEY, value)

    override fun getUserId() = getLong(USER_ID, ZERO_LONG).getValueOrDefault()
    override fun setUserId(value: Long) = putLong(USER_ID, value)

    override fun getFCMToken() = getString(KEY_FCM_TOKEN, EMPTY_STRING).default()
    override fun setFCMToken(fcmToken: String) = putString(KEY_FCM_TOKEN, fcmToken)

    override fun getReferralUserCode() = getString(REFERRAL_USER_CODE, EMPTY_STRING).default()
    override fun setReferralUserCode(referralCode: String) =
        putString(REFERRAL_USER_CODE, referralCode)

    override fun getIsCommunityLeader() = getBoolean(FLAG_COMMUNITY_LEADER, false)
    override fun setIsCommunityLeader(flag: Boolean) = putBoolean(FLAG_COMMUNITY_LEADER, flag)

    override fun getUserName() = getString(KEY_USER_NAME, EMPTY_STRING).default()
    override fun setUserName(name: String) = putString(KEY_USER_NAME, name)

    override fun getPhoneNumber() = getString(PHONE_NUMBER, EMPTY_STRING).default()
    override fun setPhoneNumber(phoneNumber: String) = putString(PHONE_NUMBER, phoneNumber)

    override fun isLoggedIn() = getBoolean(KEY_LOGIN_STATUS, false)
    override fun setLogin(success: Boolean) = putBoolean(KEY_LOGIN_STATUS, success)

    override fun getGender() = getString(GENDER, EMPTY_STRING).default()
    override fun setGender(gender: String) = putString(GENDER, gender)

    override fun getEmail() = getString(EMAIL, EMPTY_STRING).default()
    override fun setEmail(email: String) = putString(GENDER, email)

    override fun getMyReferralCode() = getString(REFERRAL_CODE, EMPTY_STRING).default()
    override fun setMyReferralCode(referralCode: String) = putString(REFERRAL_CODE, referralCode)

    override fun getWarehouseId() = getLong(WAREHOUSE_ID, 1)
    override fun setWarehouseId(warehouseId: Long) = putLong(WAREHOUSE_ID, warehouseId)

    override fun getSelectedWarehouseId() = getLong(SELECTED_WAREHOUSE_ID, 1)
    override fun setSelectedWarehouseId(warehouseId: Long) =
        putLong(SELECTED_WAREHOUSE_ID, warehouseId)

    override fun getSelectedCity() = getString(SELECTED_CITY, EMPTY_STRING).default()
    override fun setSelectedCity(city: String) = putString(SELECTED_CITY, city)

    override fun getLongitude() = getString(LONGITUDE, ZERO_STRING).default(ZERO_STRING)
    override fun setLongitude(longitude: String) = putString(LONGITUDE, longitude)

    override fun setLatitude(latitude: String) = putString(LATITUDE, latitude)
    override fun getLatitude() = getString(LATITUDE, ZERO_STRING).default(ZERO_STRING)

    override fun setFlashDeal(key: String) =
        putString(FLASH_DEAL, key)

    override fun getFlashDeal() = getString(FLASH_DEAL, EMPTY_STRING).default()

    override fun getDefaultAddressId() =
        getLong(DEFAULT_ADDRESS_ID, 0)


    override fun setDefaultAddressId(id: Long) =
        putLong(DEFAULT_ADDRESS_ID, id)


    override fun getProductPastSearch(): List<String> {
        val joined = getString(PRODUCT_PAST_SEARCH, EMPTY_STRING)
        return if (joined.isBlank()) emptyList() else joined.split(DELIMITER)
    }

    override fun setProductPastSearch(text: String) {
        val myStringList = getProductPastSearch().toMutableList()
        if (!myStringList.contains(text))
            myStringList.add(text)
        if (myStringList.size > 5)
            myStringList.removeAt(0)
        putString(PRODUCT_PAST_SEARCH, myStringList.joinToString(DELIMITER))
    }

    override fun removeProductPastSearch(text: String) {
        val myStringList = getProductPastSearch().toMutableList()
        myStringList.remove(text)
        putString(PRODUCT_PAST_SEARCH, myStringList.joinToString(DELIMITER))
    }

    override fun removeAllProductPastSearch() {
        putString(PRODUCT_PAST_SEARCH, EMPTY_STRING)
    }

    override fun getRestaurantPastSearch(): List<String> {
        val joined = getString(RESTAURANT_PAST_SEARCH, EMPTY_STRING)
        return if (joined.isBlank()) emptyList() else joined.split(DELIMITER)
    }

    override fun setRestaurantPastSearch(text: String) {
        val myStringList = getRestaurantPastSearch().toMutableList()
        if (!myStringList.contains(text))
            myStringList.add(text)
        if (myStringList.size > 5)
            myStringList.removeAt(0)
        putString(RESTAURANT_PAST_SEARCH, myStringList.joinToString(DELIMITER))
    }

    override fun removeRestaurantPastSearch(text: String) {
        val myStringList = getRestaurantPastSearch().toMutableList()
        myStringList.remove(text)
        putString(RESTAURANT_PAST_SEARCH, myStringList.joinToString(DELIMITER))
    }

    override fun removeAllRestaurantPastSearch() {
        putString(RESTAURANT_PAST_SEARCH, EMPTY_STRING)
    }


    override fun setReferralLink(link: String) = putString(REFERRAL_LINK, link)

    override fun getReferralLink(): String = getString(REFERRAL_LINK, EMPTY_STRING).default()

    override fun setContributionValue(value: Int) {
        putInt(CONTRIBUTION_VALUE, value)
    }

    override fun setCustomContributionValue(value: Int) {
        putInt(CUSTOM_CONTRIBUTION_VALUE, value)
    }

    override fun setSaveContributionValue(value: Boolean) {
        putBoolean(SAVE_CONTRIBUTION_VALUE, value)
    }

    override fun getContributionValue() = getInt(CONTRIBUTION_VALUE, ZERO_INT).getValueOrDefault()


    override fun getCustomContributionValue() =
        getInt(CUSTOM_CONTRIBUTION_VALUE, ZERO_INT).getValueOrDefault()

    override fun getSaveContributionValue() = getBoolean(SAVE_CONTRIBUTION_VALUE, false).default()

    override fun setDefaultAddress(address: String) {
        putString(ADDRESS, address)
    }

    override fun getDefaultAddress() = getString(ADDRESS, EMPTY_STRING).default()

    override fun setJazzId(id: String) {
        putString(JAZZ_ID, id)
    }

    override fun setJazzPhoneNumber(phoneNumber: String) {
        putString(JAZZ_PHONE, phoneNumber)
    }

    override fun getJazzId() = getString(JAZZ_ID, EMPTY_STRING).default()

    override fun getJazzPhoneNumber() = getString(JAZZ_PHONE, EMPTY_STRING).default()

    private companion object {
        private const val KEY_FCM_TOKEN = "FCM_TOKEN"
        private const val REFERRAL_USER_CODE = "referral_user_code"
        private const val USER_ID = "user_id"
        private const val AUTH_TOKEN_KEY = "auth_key"
        private const val KEY_USER_NAME = "login_user_name"
        private const val GENDER = "gender"
        private const val KEY_LOGIN_STATUS = "KEY_LOGIN_STATUS"
        private const val PHONE_NUMBER = "phone_number"
        private const val REFERRAL_CODE = "referral_code"
        private const val FLAG_COMMUNITY_LEADER = "flag_community_leader"
        private val PRODUCT_PAST_SEARCH = "product_past_search"
        private val RESTAURANT_PAST_SEARCH = "restaurant_past_search"
        private val DELIMITER = "‚‗‚"
        private const val DEFAULT_ADDRESS_ID = "default_address_id"
        private const val REFERRAL_LINK = "referral_link"
        private const val CONTRIBUTION_VALUE = "contribution_value"
        private const val SAVE_CONTRIBUTION_VALUE = "save_contribution_value"
        private const val CUSTOM_CONTRIBUTION_VALUE = "custom_contribution_value"
        private const val ADDRESS = "address"
        private const val JAZZ_PHONE = "jazz_phone"
        private const val JAZZ_ID = "jazz_id"
    }
}