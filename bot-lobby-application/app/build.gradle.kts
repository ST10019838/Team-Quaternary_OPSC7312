plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.7.20"  // Use the Kotlin serialization plugin
    //id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
}
//do not delete : C:\Users\Christiaan\AppData\Local\Android\Sdk
android {
    namespace = "com.example.bot_lobby"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bot_lobby"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Ktor Client and Engine (CIO or OkHttp)
//    implementation("io.ktor:ktor-client-core:2.3.0")
//    implementation("io.ktor:ktor-client-cio:2.3.0")
//
//// Ktor Content Negotiation and JSON serialization
//    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
//    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0") // Ensure this is included
//
//
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.ktor:ktor-client-core:2.3.12")
    //implementation("io.ktor:ktor-client-okhttp:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.0.0") // Replace with the latest version
    //implementation "io.ktor:ktor-serialization-kotlinx-json:2.0.0") // Replace with the latest version
    implementation("io.ktor:ktor-client-json:2.3.12")
    implementation("io.ktor:ktor-client-serialization:2.3.12")
    implementation("io.ktor:ktor-client-logging:2.3.12")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.12") // If you're using logging

// Ktor Content Negotiation and JSON serialization
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json) // Ensure this is included


    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Other necessary dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Material Icons
    implementation(libs.androidx.material.icons.extended)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Voyager Dependencies
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenmodel)
    implementation(libs.voyager.bottom.sheet.navigator)
    implementation(libs.voyager.tab.navigator)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.koin)
    implementation(libs.voyager.hilt)
    implementation(libs.voyager.livedata)
    implementation(libs.voyager.kodein)
    implementation(libs.voyager.rxjava)

    // Retrofit and OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx.v285)

//    //Google Auth Credential manager
//    implementation("androidx.credentials:credentials:1.2.2")
//    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.1")
//
//    // optional - needed for credentials support from play services, for devices running
//    // Android 13 and below.
//    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
//
//    // Google Play Services Auth
//    implementation("com.google.android.gms:play-services-auth:20.5.0")
    //implementation(libs.supabase.kt.android.debug)
    implementation("io.github.jan-tennert.supabase:supabase-kt-android:3.0.0-rc-1")
    //implementation(libs.supabase.postgrest.kt)
    //implementation(libs.gotrue.kt)
    //implementation(libs.realtime.kt)
    //Google Auth Credential manager
    //implementation(libs.androidx.credentials.v122)
    //implementation (libs.googleid)
    implementation(libs.androidx.credentials.v122)
    implementation(libs.googleid.v111)

    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    //implementation(libs.androidx.credentials.play.services.auth.v122)
    implementation(libs.androidx.credentials.play.services.auth.v122)
    // Google Play Services Auth
    implementation(libs.play.services.auth)
}