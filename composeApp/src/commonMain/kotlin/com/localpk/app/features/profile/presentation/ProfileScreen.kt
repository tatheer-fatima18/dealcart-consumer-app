package com.localpk.app.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.localpk.app.features.login.presentation.PhoneInputScreen
import com.localpk.app.features.profile.components.DealCartImage
import com.localpk.app.features.profile.components.PersonalSupportSections
import com.localpk.app.features.profile.components.SocialSection
import com.localpk.app.features.profile.components.UserInfoSection
import com.localpk.app.utils.SharedPreferencesManager
import com.localpk.app.utils.StatusBarIconColor
import org.koin.compose.koinInject

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<ProfileViewModel>()
        val sharedPreferencesManager = koinInject<SharedPreferencesManager>()
        val lifecycleOwner = LocalLifecycleOwner.current
        val navigator = LocalNavigator.currentOrThrow

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.onResumeTasks()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        StatusBarIconColor(backgroundColor = Color.Blue)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item {
                UserInfoSection(
                    isLoggedIn = sharedPreferencesManager.isLoggedIn(),
                    userName = sharedPreferencesManager.getUserName(),
                    isFemale = false,
                    onBackClick = { navigator.pop() },
                    onLoginClick = { navigator.push(PhoneInputScreen()) }
                )
            }

            item {
                PersonalSupportSections(
                    onPersonalItemClick = {
                        viewModel.handlePersonalSectionNavigation(
                            it, navigator
                        )
                    },
                    onSupportItemClick = { viewModel.handleSupportClick(it, navigator) },
                    personalSectionItemList = viewModel.personalSectionItemList,
                    supportSectionItemList = viewModel.supportSectionItemList.value
                )
            }

            item {
                DealCartImage {
                    openUrl("http://dealcart.io")
                }
            }

            item {
                SocialSection {
                    openUrl(it)
                }
            }
        }
    }

    private fun openUrl(url: String) {
//        try {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
//        } catch (e: Exception) {
//        }
    }
}