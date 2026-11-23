package com.localpk.app.base.data

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import com.localpk.app.utils.EMPTY_STRING
import kotlinx.serialization.Serializable

interface UserSettingsFlowData {
    fun getUserSettings(): UserSettings
    fun addUserSettings(userSettings: UserSettings)
}

class DefaultUserSettingsFlowData : UserSettingsFlowData {
    private val userSettings: UserSettings =
        UserSettings(bannerVersion = EMPTY_STRING, categoryVersion = EMPTY_STRING)

    override fun getUserSettings(): UserSettings = userSettings
    override fun addUserSettings(userSettings: UserSettings) {
        userSettings.bannerVersion = userSettings.bannerVersion
        userSettings.categoryVersion = userSettings.categoryVersion
    }

}

@Serializable
data class UserSettings(
    var bannerVersion: String?,
    var categoryVersion: String?
) : JavaSerializable