package com.localpk.app.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator

abstract class BaseScreen<VM : BaseViewModel<*>>(
    private val viewModelProvider: @Composable () -> VM
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = viewModelProvider()
        val navigator = LocalNavigator.current
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        // Observe navigation events
        LaunchedEffect(viewModel) {
            viewModel.onInIt()
            viewModel.navigation.collect { event ->
                when (event) {
                    is NavigationEvent.Push -> navigator?.push(event.screen)
                    is NavigationEvent.Pop -> navigator?.pop()
                    is NavigationEvent.PopToRoot -> navigator?.popUntilRoot()
                    is NavigationEvent.ReplaceAll -> navigator?.replaceAll(event.screen)
                    is NavigationEvent.PopToUntil -> navigator?.popUntil { it == event.screen }
                    is NavigationEvent.PushBottomSheet -> bottomSheetNavigator.push(event.screen)
                    is NavigationEvent.ShowBottomSheet -> bottomSheetNavigator.show(event.screen)
                }
            }
        }

        ScreenContent(viewModel)
    }

    @Composable
    abstract fun ScreenContent(viewModel: VM)
} 