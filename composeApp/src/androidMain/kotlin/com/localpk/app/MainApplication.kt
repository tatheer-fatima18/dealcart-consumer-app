package com.localpk.app

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import com.localpk.app.base.PlatformContext
import com.localpk.app.di.initKoin
import com.localpk.app.di.platformModule
import org.koin.android.ext.koin.androidContext
import com.google.android.libraries.places.api.Places
import com.localpk.app.base.BaseUrls
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainApplication : Application() {

    // Application-scoped coroutine scope for background work
    // Uses SupervisorJob so one failure doesn't cancel other operations
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // Set app context immediately (lightweight operation)
        PlatformContext.appContext = applicationContext

        // Setup Koin (must be done synchronously for dependency injection)
        // Note: Koin initialization should be fast, but if platformModule creates
        // heavy objects, they should be lazy
        setupKoin()

        // Defer heavy initialization operations to prevent ANR
        // These operations are deferred so onCreate() can return quickly
        deferHeavyInitialization()
    }

    private fun setupKoin() {
        initKoin(additionalModules = listOf(platformModule(applicationContext))) {
            androidContext(applicationContext)
        }
    }

    /**
     * Defer heavy initialization operations to prevent ANR.
     * 
     * Strategy:
     * 1. Use coroutines with Dispatchers.IO to run heavy operations on background thread
     * 2. MapsInitializer.initialize() and Places.initialize() can be called from any thread
     * 3. This prevents blocking the main thread and eliminates ANR risk
     * 
     * Benefits:
     * - onCreate() returns immediately, making app responsive
     * - Heavy operations run on background thread (IO dispatcher)
     * - No risk of ANR since main thread is never blocked
     * - SupervisorJob ensures one failure doesn't cancel other operations
     */
    private fun deferHeavyInitialization() {
        // Launch heavy initialization on IO dispatcher (background thread)
        // This prevents any blocking on the main thread, eliminating ANR risk
        applicationScope.launch {
            try {
                // Initialize Google Maps on background thread
                // MapsInitializer.initialize() can be called from any thread
                MapsInitializer.initialize(applicationContext)
            } catch (e: Exception) {
                Firebase.crashlytics.log("Failed to initialize Maps: ${e.message}")
                Firebase.crashlytics.recordException(e)
            }
        }
    }

}