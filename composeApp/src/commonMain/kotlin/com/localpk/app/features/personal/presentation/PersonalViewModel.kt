package com.localpk.app.features.personal.presentation

import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.personal.data.ProfileData
import com.localpk.app.features.personal.data.ProfileDetails
import com.localpk.app.features.personal.domain.ProfileDetailUseCase
import com.localpk.app.features.personal.domain.SendEmailUseCase
import com.localpk.app.features.personal.domain.UpdateProfileDetailUseCase
import com.localpk.app.features.personal.domain.VerifyEmailUseCase
import com.localpk.app.firebase.pushNotification.PushNotificationService
import com.localpk.app.network.GeneralStringResponse
import com.localpk.app.utils.EMPTY_STRING
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.ZERO_INT
import com.localpk.app.utils.ZERO_LONG
import com.localpk.app.utils.default
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PersonalViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val personalDetailUseCase: ProfileDetailUseCase,
    private val updateProfileDetailUseCase: UpdateProfileDetailUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val sendEmailUseCase: SendEmailUseCase,
    private val eventLogger: EventLogger,
    private val pushNotificationService: PushNotificationService
) : BaseViewModel<Any>() {
    private val _nameLiveData = MutableStateFlow(EMPTY_STRING)
    val nameLiveData = _nameLiveData.asStateFlow()

    private val _phoneLiveData = MutableStateFlow(EMPTY_STRING)
    val phoneLiveData = _phoneLiveData.asStateFlow()

    val emailLiveData = MutableStateFlow(EMPTY_STRING)

    private val _dobLiveData = MutableStateFlow(EMPTY_STRING)
    val dobLiveData = _dobLiveData.asStateFlow()

    private val _genderLiveData = MutableStateFlow(EMPTY_STRING)
    val genderLiveData = _genderLiveData.asStateFlow()

    val isEdit = MutableStateFlow(false)
    val isMale = MutableStateFlow(false)
    val isFemale = MutableStateFlow(false)
    val isEmailVerified = MutableStateFlow(false)
    val showBottomSheet = MutableStateFlow(false)

    val count = MutableStateFlow(ZERO_INT)


    fun onActionLogout(successCallback: () -> Unit) {
        launch {
            // Handle FCM token for iOS only

                try {
                    // Delete current FCM token
                    pushNotificationService.deleteToken()
                    
                    // Generate new FCM token
                    val newToken = pushNotificationService.getToken()
                    if (newToken != null) {
                        sharedPreferencesManager.setFCMToken(newToken)
                    }
                } catch (e: Exception) { }

        }
        
        sharedPreferencesManager.logout()
        sharedPreferencesManager.setDefaultAddress(EMPTY_STRING)
        sharedPreferencesManager.setDefaultAddressId(ZERO_LONG)
        sharedPreferencesManager.setToken(EMPTY_STRING)
        setPersonalDetailsEmpty()
        eventLogger.LogoutButton()

        successCallback()
    }



    fun handleSendEmail() {
        unZipViewStateFlow(
            sendEmailUseCase(Unit),
        )
    }

    fun handleVerifyEmail() {
//        val context = App.application?.applicationContext
        unZipViewStateFlow(
            verifyEmailUseCase(Unit),
            onSuccess = { res ->
                val response = res as GeneralStringResponse
                if (response.success && count.value == 0) {
                    showBottomSheet.value = true
                    count.value = count.value.plus(1)
                    getPersonalDetails()
//                    if (context != null) {
//                        MoEAnalyticsHelper.setEmailId(context, emailLiveData.value.orEmpty())
//                    }
                }
            },
            onError = {

            }
        )
    }

    fun getPersonalDetails() {
        unZipViewStateFlow(personalDetailUseCase(Unit), onSuccess = { res ->
            val profileData = res as ProfileData
            profileData.profileDetails?.let { response ->
                emailLiveData.value = response.email.default()
                _dobLiveData.value = response.dateOfBirth.default().replace("-", "/")
                _genderLiveData.value = response.gender.default("male")
                isEmailVerified.value = response.isEmailVerified == true
                _nameLiveData.value = response.firstName + " " + response.lastName
                sharedPreferencesManager.setUserName(response.firstName + " " + response.lastName)
                if (response.gender == "male") {
                    isMale.value = true
                    isFemale.value = false
                } else {
                    isMale.value = false
                    isFemale.value = true
                }
                if (response.email.default()
                        .isNotEmpty() && response.isEmailVerified == false
                ) {
                    launch {
                        delay(2000)
                        showBottomSheet.value = true
                    }
                }
                sharedPreferencesManager.setGender(response.gender.default())
                sharedPreferencesManager.setEmail(email = response.email.default())
            }
        })
    }

    fun updatePersonalDetails() {
        if (dobLiveData.value.isEmpty().not()) {
            val request = ProfileDetails(
                dateOfBirth = dobLiveData.value.default().replace("/", "-"),
                gender = _genderLiveData.value.default("male"),
                email = emailLiveData.value.default(),
                isEmailVerified = isEmailVerified.value,
                name = _nameLiveData.value.default(),
            )
            unZipViewStateFlow(
                updateProfileDetailUseCase(request),
                onSuccess = {
                    isEdit.value = false
                    getPersonalDetails()
//                    showDcSuccess("Aap ki tafseelat update kar di gaye hain!")
                    eventLogger.updateDateOfBirth(
                        _dobLiveData.value.orEmpty(),
                        sharedPreferencesManager.getUserId()
                    )
                    eventLogger.updateEmailAddress(
                        emailLiveData.value.orEmpty(),
                        sharedPreferencesManager.getUserId()
                    )
                    eventLogger.updateName(_nameLiveData.value.default())
                }
            )
        } else {
//            showError("Date of Birth can't be empty!")
        }
    }

    fun clickEdit() {
        isEdit.value = true
    }

    fun setName(name: String) {
        _nameLiveData.value = name
    }

    fun setDoB(dob: String) {
        _dobLiveData.value = dob
    }

    fun putValueGender(gender: String) {
        _genderLiveData.value = gender
        if (gender == "male") {
            isMale.value = true
            isFemale.value = false
        } else {
            isMale.value = false
            isFemale.value = true
        }
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex =
            "^[a-zA-Z0-9+._%\\-]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+\$".toRegex()
        return emailRegex.matches(email)
    }

    fun deleteAccount() {
        launch {

        }
    }

    fun setPhoneNumber(){
        _phoneLiveData.value = sharedPreferencesManager.getPhoneNumber()
    }

    fun setPersonalDetailsEmpty() {
        _nameLiveData.value = EMPTY_STRING
        _phoneLiveData.value = EMPTY_STRING
        emailLiveData.value = EMPTY_STRING
        _dobLiveData.value = EMPTY_STRING
        _genderLiveData.value = EMPTY_STRING
        isEdit.value = false
        isMale.value = false
        isFemale.value = false
        isEmailVerified.value = false
    }
}