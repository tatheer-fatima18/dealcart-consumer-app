import java.io.FileInputStream
import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.google.playServices)
    alias(libs.plugins.firebase.crashlytics)
    id("app.cash.sqldelight") version "2.0.2"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }

        // Add iOS-specific optimizations to prevent priority inversions
        iosTarget.compilations["main"].kotlinOptions {
            freeCompilerArgs += listOf(
                "-Xexport-kdoc",
                "-Xskip-prerelease-check",
                "-opt-in=kotlin.RequiresOptIn"
            )
        }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "ComposeApp"
            isStatic = true
        }

        // Map Xcode configurations to Kotlin build types
        xcodeConfigurationToNativeBuildType["devDebug"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["devRelease"] = NativeBuildType.RELEASE
        xcodeConfigurationToNativeBuildType["stagingDebug"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["stagingRelease"] = NativeBuildType.RELEASE
        xcodeConfigurationToNativeBuildType["prodDebug"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["prodRelease"] = NativeBuildType.RELEASE
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.ktor.client.android)
                implementation(libs.android.driver)
                implementation(libs.androidx.core.splashscreen)
                implementation(libs.maps.compose)
                implementation(libs.maps.compose.utils)
                implementation(libs.play.services.maps)
                implementation(libs.play.services.auth.api.phone)
                implementation(libs.core)
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.android.crashlytics.ktx)
                implementation(libs.firebase.android.analytics.ktx)
                implementation(libs.firebase.android.config.ktx)
                implementation(libs.firebase.android.messaging.ktx)
                implementation(libs.xpay.element.android.native.sdk)
                implementation(libs.okhttp)
                implementation(libs.androidx.room.runtime)
                implementation(libs.retrofit)
                implementation(libs.converter.gson)
                implementation(libs.androidx.work.runtime)
                implementation("com.google.android.libraries.places:places:3.4.0")

                // MERGN SDK
                implementation(files("src/androidMain/libs/mergn_sdk_kotlin_5.1.1.aar"))
                implementation("com.android.installreferrer:installreferrer:2.2")
                
                // Singular SDK - Download AAR file from Singular dashboard
                implementation("com.singular.sdk:singular_sdk:12.10.0")
            }
        }
        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.native.driver)
            }
        }
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)

                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)

                //voyager
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.tabNavigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.bottomSheetNavigator)

                implementation(libs.kotlinx.coroutines.core)
                // Kotlinx Serialization
                implementation(libs.kotlinx.serialization.json)

                //Coil
                implementation(libs.bundles.coil)
                implementation(libs.coil.network.ktor3)

                //Koin
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(libs.multiplatform.settings)
                implementation(libs.kotlinx.datetime)
                implementation(libs.primitive.adapters)

                // Geolocation
                implementation(libs.compass.geolocation)
                implementation(libs.compass.geolocation.mobile)

                // Geocoding
                implementation(libs.compass.geocoder)
                implementation(libs.compass.geocoder.mobile)

                // Autocomplete
                implementation(libs.compass.autocomplete)
                implementation(libs.compass.autocomplete.mobile)

                // Location permissions for mobile
                implementation(libs.compass.permissions.mobile)

                implementation(libs.compottie)
                implementation(libs.compottie.network)
                implementation(libs.compottie.dot)

                // GitLive Firebase SDK dependencies
                api(libs.gitlive.firebase.kotlin.crashlytics)
                api(libs.gitlive.firebase.kotlin.remote.config)
                api(libs.gitlive.firebase.kotlin.messaging)
                api(libs.gitlive.firebase.kotlin.analytics)

            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "com.localpk.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val keystoreProperties = Properties().apply {
        val keystorePropertiesFile = file("keystore.properties")
        if (keystorePropertiesFile.exists()) {
            load(FileInputStream(keystorePropertiesFile))
        }
    }

    val secretsProperties = Properties().apply {
        listOf(
            rootProject.file("secrets.properties"),
            file("secrets.properties")
        ).firstOrNull { it.exists() }?.let { load(FileInputStream(it)) }
    }

    fun secretOrNull(envName: String, propName: String): String? =
        System.getenv(envName)?.takeIf { it.isNotBlank() }
            ?: secretsProperties.getProperty(propName)?.takeIf { it.isNotBlank() }
            ?: keystoreProperties.getProperty(propName)?.takeIf { it.isNotBlank() }

    fun secret(envName: String, propName: String, default: String = ""): String =
        secretOrNull(envName, propName) ?: default

    fun quotedSecret(envName: String, propName: String, default: String = ""): String =
        "\"${secret(envName, propName, default).replace("\\", "\\\\").replace("\"", "\\\"")}\""

    defaultConfig {
        applicationId = "com.localpk.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 201
        versionName = "1.1.16"
        resValue(
            "string",
            "google_maps_key",
            secret("GOOGLE_MAPS_KEY", "google.maps.key")
        )
        buildConfigField(
            "String",
            "LOCATIONIQ_API_KEY",
            quotedSecret("LOCATIONIQ_API_KEY", "locationiq.apiKey")
        )
        buildConfigField(
            "String",
            "SINGULAR_API_KEY",
            quotedSecret("SINGULAR_API_KEY", "singular.apiKey")
        )
        buildConfigField(
            "String",
            "SINGULAR_SECRET",
            quotedSecret("SINGULAR_SECRET", "singular.secret")
        )
        buildConfigField(
            "String",
            "FACEBOOK_APP_ID",
            quotedSecret("FACEBOOK_APP_ID", "facebook.appId")
        )
        buildConfigField(
            "String",
            "FACEBOOK_CLIENT_TOKEN",
            quotedSecret("FACEBOOK_CLIENT_TOKEN", "facebook.clientToken")
        )
    }

    val releaseStoreFile = secretOrNull("KEYSTORE_FILE", "storeFile")
    val releaseStorePassword = secretOrNull("KEYSTORE_STORE_PASSWORD", "storePassword")
    val releaseKeyAlias = secretOrNull("KEYSTORE_KEY_ALIAS", "keyAlias")
    val releaseKeyPassword = secretOrNull("KEYSTORE_KEY_PASSWORD", "keyPassword")
    val hasReleaseSigning =
        releaseStoreFile != null &&
            releaseStorePassword != null &&
            releaseKeyAlias != null &&
            releaseKeyPassword != null

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = file(releaseStoreFile!!)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // Exclude baseline profiles to prevent installation failures
            excludes += "**/baseline.prof"
            excludes += "**/*.prof"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            // Disable baseline profile to prevent installation failures
            enableUnitTestCoverage = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    lint {
        abortOnError = false
    }
    externalNativeBuild {

        // Encapsulates your CMake build configurations.
        cmake {

            // Provides a relative path to your CMake build script.
            path = file("CMakeLists.txt")
        }
    }
    flavorDimensions("env")
    productFlavors {
        create("dev") {
            dimension = "env"
            versionNameSuffix = "-dev"
            applicationIdSuffix = ".dev"
            buildConfigField("String", "BASE_URL", "\"https://api-dev.dealcart.io\"")
            buildConfigField("String", "BASE_URL_AGENT", "\"https://agent-api-dev.dealcart.io\"")
            buildConfigField("String", "BASE_URL_DOLAT", "\"https://dolat-api-dev.dealcart.io\"")
            buildConfigField("String", "BASE_URL_SPORTS", "\"https://sports-dev.dealcart.io\"")
            buildConfigField("String", "BASE_URL_LINKS", "\"https://links-dev.dealcart.io\"")
            buildConfigField("String", "BASE_URL_FOOD", "\"https://food-api-dev.dealcart.io\"")
            buildConfigField(
                "String",
                "CARD_ACCOUNT_ID",
                quotedSecret("DEV_CARD_ACCOUNT_ID", "dev.card.accountId")
            )
            buildConfigField(
                "String",
                "CARD_HMAC_SECRET_KEY",
                quotedSecret("DEV_CARD_HMAC_SECRET_KEY", "dev.card.hmacSecretKey")
            )
            buildConfigField(
                "String",
                "CARD_PUBLIC_KEY",
                quotedSecret("DEV_CARD_PUBLIC_KEY", "dev.card.publicKey")
            )
            buildConfigField(
                "String",
                "MERGN_API_KEY",
                quotedSecret("DEV_MERGN_API_KEY", "dev.mergn.apiKey")
            )
        }
        create("staging") {
            dimension = "env"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("String", "BASE_URL", "\"https://api-staging.dealcart.io\"")
            buildConfigField(
                "String",
                "BASE_URL_AGENT",
                "\"https://agent-api-staging.dealcart.io\""
            )
            buildConfigField(
                "String",
                "BASE_URL_DOLAT",
                "\"https://dolat-api-staging.dealcart.io\""
            )
            buildConfigField("String", "BASE_URL_SPORTS", "\"https://sports-staging.dealcart.io\"")
            buildConfigField("String", "BASE_URL_LINKS", "\"https://links-staging.dealcart.io\"")
            buildConfigField("String", "BASE_URL_FOOD", "\"https://food-api-staging.dealcart.io\"")
            buildConfigField(
                "String",
                "CARD_ACCOUNT_ID",
                quotedSecret("STAGING_CARD_ACCOUNT_ID", "staging.card.accountId")
            )
            buildConfigField(
                "String",
                "CARD_HMAC_SECRET_KEY",
                quotedSecret("STAGING_CARD_HMAC_SECRET_KEY", "staging.card.hmacSecretKey")
            )
            buildConfigField(
                "String",
                "CARD_PUBLIC_KEY",
                quotedSecret("STAGING_CARD_PUBLIC_KEY", "staging.card.publicKey")
            )
            buildConfigField(
                "String",
                "MERGN_API_KEY",
                quotedSecret("STAGING_MERGN_API_KEY", "staging.mergn.apiKey")
            )
        }

        create("prod") {
            dimension = "env"
            buildConfigField("String", "BASE_URL", "\"https://api.dealcart.io\"")
            buildConfigField("String", "BASE_URL_AGENT", "\"https://agent-api.dealcart.io\"")
            buildConfigField("String", "BASE_URL_DOLAT", "\"https://dolat-api.dealcart.io\"")
            buildConfigField("String", "BASE_URL_SPORTS", "\"https://sports.dealcart.io\"")
            buildConfigField("String", "BASE_URL_LINKS", "\"https://links.dealcart.io\"")
            buildConfigField("String", "BASE_URL_FOOD", "\"https://food-api.dealcart.io\"")
            buildConfigField(
                "String",
                "CARD_ACCOUNT_ID",
                quotedSecret("PROD_CARD_ACCOUNT_ID", "prod.card.accountId")
            )
            buildConfigField(
                "String",
                "CARD_HMAC_SECRET_KEY",
                quotedSecret("PROD_CARD_HMAC_SECRET_KEY", "prod.card.hmacSecretKey")
            )
            buildConfigField(
                "String",
                "CARD_PUBLIC_KEY",
                quotedSecret("PROD_CARD_PUBLIC_KEY", "prod.card.publicKey")
            )
            buildConfigField(
                "String",
                "MERGN_API_KEY",
                quotedSecret("PROD_MERGN_API_KEY", "prod.mergn.apiKey")
            )
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    // Desugaring support for java.time APIs on Android API < 26
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.singular.net/")  }
}

sqldelight {
    databases {
        create("DealCartDb") {
            packageName.set("com.localpk.app")
            generateAsync.set(true)
        }
    }
}