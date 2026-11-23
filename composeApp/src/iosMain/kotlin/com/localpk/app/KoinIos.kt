package com.localpk.app

import cocoapods.FirebaseCore.FIRApp
import com.localpk.app.di.initKoin
import com.localpk.app.di.platformModule
import kotlinx.cinterop.ExperimentalForeignApi

fun initKoinIOS() = initKoin(
    additionalModules = listOf(platformModule())
)

@OptIn(ExperimentalForeignApi::class)
fun onDidFinishLaunchingWithOptions() {
    initKoinIOS()
    FIRApp.configure()
}