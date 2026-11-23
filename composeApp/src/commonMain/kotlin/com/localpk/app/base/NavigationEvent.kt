package com.localpk.app.base

import cafe.adriel.voyager.core.screen.Screen

sealed class NavigationEvent {
    data class Push(val screen: Screen) : NavigationEvent()
    data object Pop : NavigationEvent()
    data object PopToRoot : NavigationEvent()
    data class PopToUntil(val screen: Any) : NavigationEvent()
    data class ReplaceAll(val screen: Screen) : NavigationEvent()
    data class PushBottomSheet(val screen: Screen) : NavigationEvent()
    data class ShowBottomSheet(val screen: Screen) : NavigationEvent()
}