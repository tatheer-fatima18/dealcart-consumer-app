package com.localpk.app.features.profile.presentation

import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.localpk.app.base.BaseViewModel
import com.localpk.app.features.address.presentation.AddressListScreen
import com.localpk.app.features.personal.presentation.PersonalScreen
import com.localpk.app.features.profile.data.CustomerSavingDetailData
import com.localpk.app.features.profile.data.IconSource
import com.localpk.app.features.profile.data.PersonalSectionItem
import com.localpk.app.features.webview.WebViewScreen
import com.localpk.app.utils.EventLogger
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.navigateToWebChat
import dealcart.composeapp.generated.resources.Res
import dealcart.composeapp.generated.resources.addresses_icon
import dealcart.composeapp.generated.resources.dealcartdost
import dealcart.composeapp.generated.resources.privacy_icon
import dealcart.composeapp.generated.resources.profile_icon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val eventLogger: EventLogger
) : BaseViewModel<Any>() {
    val personalSectionItemList = arrayListOf<PersonalSectionItem>()

    var newItemAdded = false
    var newProfileItemAdded = false

    private val _supportSectionItemList = MutableStateFlow<List<PersonalSectionItem>>(emptyList())
    val supportSectionItemList = _supportSectionItemList.asStateFlow()

    private val _customerSavingDetailLiveData = MutableStateFlow<CustomerSavingDetailData?>(null)
    val customerSavingDetailLiveData = _customerSavingDetailLiveData.asStateFlow()

    init {
        loadSupportItems()
        // TODO: Use Remote Config for inbox feature
        // Example: if (remoteConfigs.isInboxFeatureEnabled()) { ... }
//        if (RemoteConfigs.isInboxEnabled()) {
//            val inboxCount = MoEInboxHelper.getInstance()
//                .getUnClickedMessagesCount(App.application?.applicationContext!!)
//            if (inboxCount?.count.getValueOrDefault().toInt() > 0) {
//                if (sharedPreferencesManager.isLoggedIn() && personalSectionItemList.size > 3) {
//                    personalSectionItemList[3].notificationIcon = true
//                } else if (sharedPreferencesManager.isLoggedIn().not()) {
//                    personalSectionItemList[1].notificationIcon = true
//                }
//            }
//        }
        if (sharedPreferencesManager.getIsCommunityLeader() && !newItemAdded) {
            val newItem = PersonalSectionItem(
                IconSource.Drawable(Res.drawable.dealcartdost),
                ProfileItemConstants.DEALCARTDOST.key
            )
            personalSectionItemList.add(newItem)
            newItemAdded = true
        }
    }

    private fun loadSupportItems() {
        viewModelScope.launch {
            val jsonBytes = Res.readBytes("files/chat.json")
            _supportSectionItemList.value = listOf(
//                PersonalSectionItem(
//                    IconSource.Drawable(Res.drawable.hemp_center_icon),
//                    SupportItemConstants.HELP_CENTER.key
//                ),
                PersonalSectionItem(
                    IconSource.Drawable(Res.drawable.privacy_icon),
                    SupportItemConstants.PRIVACY_POLICY.key
                ),
                PersonalSectionItem(
                    IconSource.Animation(jsonBytes),
                    SupportItemConstants.CONTACT_US.key,
                    isAnimation = true
                )
            )

        }
    }

    fun handlePersonalSectionNavigation(
        sectionItem: PersonalSectionItem,
        navigator: Navigator
    ) {
        when (sectionItem.label) {
            ProfileItemConstants.ADDRESSES.key -> {
                //onActionMyAddress()
                navigator.push(AddressListScreen())
            }

            ProfileItemConstants.PERSONAL_DETAILS.key -> {
                navigator.push(PersonalScreen())
            }

            ProfileItemConstants.INBOX.key -> {
                onActionInbox()
            }

            ProfileItemConstants.DEALCARTDOST.key -> {
                //  onActionRewards()
            }
        }
    }

    private fun handleSupportSectionNavigation(
        sectionItem: PersonalSectionItem,
        navigator: Navigator
    ) {
        when (sectionItem.label) {
            SupportItemConstants.HELP_CENTER.key -> {
                onActionFaq()
            }

            SupportItemConstants.PRIVACY_POLICY.key -> {
                onActionPrivacyPolicy(navigator)
            }
        }
    }

    fun handleSupportClick(item: PersonalSectionItem, navigator: Navigator) {
        if (item.label == SupportItemConstants.CONTACT_US.key) {
            onActionHelp(navigator)
        } else {
            handleSupportSectionNavigation(item, navigator)
        }
    }

    private fun onActionHelp(navigator: Navigator) {
        eventLogger.WhatsappHelpButton()
        navigateToWebChat(navigator)
    }

    private fun onActionFaq() {
//        navigate(navigationProvider.getPushNavigator(ProfileFragmentDirections.actionNavProfileToHelpCenterMainCategoryFragment()))
    }

    private fun onActionInbox() {
//        navigate(navigationProvider.getPushNavigator(ProfileFragmentDirections.actionNavProfileToInbox()))
    }

    private fun onActionRewards() {
//        navigate(
//            navigationProvider.getPushNavigator(
//                ProfileFragmentDirections.actionNavProfileToCommunityLeaderFragment(
//                    0,
//                    true
//                )
//            )
//        )
    }

    private fun onActionPrivacyPolicy(navigator: Navigator) {
        val webViewArgs = WebViewScreen(
            url = "https://dealcart.io/privacy-policy",
        )
        navigator.push(webViewArgs)

//        val args = WebViewFragmentArgs(
//            title = "Privacy Policy",
//            htmlPath = "https://dealcart.io/privacy-policy",
//            webViewType = WebViewType.PRIVACY_POLICY
//        )
//        navigate(
//            navigationProvider.getPushNavigator(
//                ProfileFragmentDirections.actionNavProfileToWebViewFragment(
//                    args
//                )
//            )
//        )
    }

    fun onResumeTasks() {
        if (sharedPreferencesManager.isLoggedIn() && !newProfileItemAdded) {
            personalSectionItemList.add(
                0,
                PersonalSectionItem(
                    IconSource.Drawable(Res.drawable.profile_icon),
                    ProfileItemConstants.PERSONAL_DETAILS.key
                ),
            )
            personalSectionItemList.add(
                1,
                PersonalSectionItem(
                    IconSource.Drawable(Res.drawable.addresses_icon),
                    ProfileItemConstants.ADDRESSES.key
                ),
            )
            newProfileItemAdded = true
        }
    }

    enum class ProfileItemConstants(val key: String) {
        PERSONAL_DETAILS("My Account"),
        INBOX("Inbox"),
        ADDRESSES("My Addresses"),
        DEALCARTDOST("DealCart Dost")
    }

    enum class SupportItemConstants(val key: String) {
        HELP_CENTER("Help Center"),
        PRIVACY_POLICY("Privacy Policy"),
        CONTACT_US("Contact Support")
    }
}