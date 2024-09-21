// Apply necessary plugins for the Android application
plugins {
    id("com.android.application") // Android application plugin
    id("org.jetbrains.kotlin.android") // Official Kotlin Android plugin for Kotlin development
    id("com.google.gms.google-services") // Google services plugin for Firebase integration
}

android {
    // Application namespace that uniquely identifies your app
    namespace = "com.example.bot_lobby"
    // Compile SDK version specifies the Android SDK version the app will be compiled against
    compileSdk = 34

    defaultConfig {
        // Application ID (package name) for your app
        applicationId = "com.example.bot_lobby"
        // Minimum supported SDK version
        minSdk = 24
        // Target SDK version
        targetSdk = 34
        // Version code (increment for each release)
        versionCode = 1
        // Version name (human-readable version)
        versionName = "1.0"

        // Specifies the test runner used for instrumented tests
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            // Enable support for vector drawables in older Android versions
            useSupportLibrary = true
        }
    }

    buildTypes {
        // Configuration for the release build type
        release {
            // Disables code shrinking, obfuscation, and optimization in the release build
            isMinifyEnabled = false
            // ProGuard configuration files for code optimization and shrinking
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Default ProGuard file provided by Android
                "proguard-rules.pro" // Custom ProGuard rules
            )
        }
    }

    compileOptions {
        // Java compatibility options
        sourceCompatibility = JavaVersion.VERSION_1_8 // Use Java 1.8 syntax
        targetCompatibility = JavaVersion.VERSION_1_8 // Target Java 1.8 bytecode
    }

    kotlinOptions {
        // Set the JVM target version for Kotlin compilation
        jvmTarget = "1.8"
    }

    buildFeatures {
        // Enable Jetpack Compose for building UI components
        compose = true
    }

    composeOptions {
        // Specify the Kotlin compiler extension version for Jetpack Compose
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            // Exclude unnecessary files from being included in the APK
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core AndroidX and Jetpack Compose dependencies
    implementation(libs.androidx.core.ktx) // Android KTX core extensions for better Kotlin support
    implementation(libs.androidx.lifecycle.runtime.ktx) // Lifecycle runtime KTX for managing component lifecycle
    implementation(libs.androidx.activity.compose) // Jetpack Compose integration with Android activity
    implementation(platform(libs.androidx.compose.bom)) // BOM (Bill of Materials) for managing Compose versions
    implementation(libs.androidx.ui) // Jetpack Compose UI components
    implementation(libs.androidx.ui.graphics) // Jetpack Compose graphics
    implementation(libs.androidx.ui.tooling.preview) // Tooling support for Compose previews in Android Studio
    implementation(libs.androidx.material3) // Material Design 3 components for Jetpack Compose
    implementation("androidx.compose.material:material-icons-extended:1.7.2") // Extended Material icons for Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2") // ViewModel integration with Jetpack Compose
    implementation("androidx.compose.ui:ui:1.7.2")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.2")
    implementation("androidx.compose.foundation:foundation:1.7.2")
    implementation("androidx.compose.material:material-icons-extended:1.7.2")
    implementation("androidx.compose.runtime:runtime:1.7.2")
    implementation("androidx.compose.ui:ui-text:1.7.2")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.2")

    // Additional library for number picker in Jetpack Compose
    implementation("com.chargemap.compose:numberpicker:1.0.3")

    // Firebase dependencies (without using the version catalog)
    implementation(platform("com.google.firebase:firebase-bom:33.3.0")) // Firebase BOM to manage Firebase versions
    implementation("com.google.firebase:firebase-firestore-ktx") // Firebase Firestore KTX
    implementation("com.google.firebase:firebase-common-ktx") // Firebase common utilities KTX
    implementation("com.google.firebase:firebase-auth-ktx") // Firebase Authentication KTX
    implementation("com.google.firebase:firebase-analytics-ktx") // Firebase Analytics KTX
    implementation("com.google.firebase:firebase-database-ktx:20.1.0") // Firebase Realtime Database KTX

    // Voyager navigation library dependencies for Jetpack Compose navigation
    val voyagerVersion = "1.0.0"
    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion") // Voyager Navigator for Compose navigation
    implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion") // ScreenModel for managing state
    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion") // BottomSheetNavigator for bottom sheets
    implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion") // TabNavigator for tab navigation
    implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion") // Transitions for animated navigation
    implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion") // Koin integration for dependency injection
    implementation("cafe.adriel.voyager:voyager-hilt:$voyagerVersion") // Hilt integration for dependency injection
    implementation("cafe.adriel.voyager:voyager-livedata:$voyagerVersion") // LiveData integration with Voyager

    // Compose form library for managing forms in Jetpack Compose
    implementation("com.github.benjamin-luescher:compose-form:0.2.8")

    // Additional Google Play Services libraries
    implementation("com.google.android.gms:play-services-auth:21.1.0") // Google Play Services Authentication
    implementation("com.google.android.gms:play-services-identity:18.0.1") // Google Play Services Identity
    implementation("com.google.android.gms:play-services-safetynet:18.0.1") // Google Play Services SafetyNet
    implementation("com.google.android.material:material:1.11.0") // Material Design components for UI

    // Networking libraries
    implementation(libs.retrofit) // Retrofit for making HTTP requests
    implementation(libs.converter.gson) // Gson converter for parsing JSON in Retrofit
    implementation(libs.okhttp) // OkHttp for networking

    // Kotlin Coroutines for handling asynchronous tasks
    implementation(libs.kotlinx.coroutines.core) // Kotlin Coroutines Core
    implementation(libs.kotlinx.coroutines.android) // Kotlin Coroutines for Android

    // ViewModel integration for managing UI-related data in a lifecycle-conscious way
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel KTX for lifecycle management
    implementation(libs.androidx.lifecycle.viewmodel.compose) // ViewModel integration for Jetpack Compose

    // Unit and instrumentation testing dependencies
    testImplementation(libs.junit) // JUnit for unit testing
    androidTestImplementation(libs.androidx.junit) // AndroidX JUnit for instrumentation testing
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM for consistent Compose versioning in tests
    androidTestImplementation(libs.androidx.ui.test.junit4) // JUnit4 integration for Compose UI testing
    debugImplementation(libs.androidx.ui.tooling) // Tooling support for Compose in debug builds
    debugImplementation(libs.androidx.ui.test.manifest) // Manifest for Compose UI testing in debug builds
}

// Uncomment if using kapt for annotation processing (e.g., Dagger Hilt, Room)
// kapt {
//     correctErrorTypes = true
// }
