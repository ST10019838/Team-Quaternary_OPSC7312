plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.7.20"
}

android {
    namespace = "com.example.bot_lobby"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bot_lobby"
        minSdk = 24
        targetSdk = 35
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//    // Ktor Client and Engine (CIO or OkHttp)
//    implementation("io.ktor:ktor-client-core:2.3.0")
//    implementation("io.ktor:ktor-client-cio:2.3.0") // or ktor-client-okhttp if you prefer
//
//    // Ktor Content Negotiation
//    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
//    implementation("io.ktor:ktor-serialization-gson:2.3.0")

    // Ktor Client and Engine (CIO or OkHttp)
    implementation("io.ktor:ktor-client-core:2.3.0")
    implementation("io.ktor:ktor-client-cio:2.3.0") // or ktor-client-okhttp if you prefer

    // Ktor Content Negotiation
    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-serialization-gson:2.3.0")

    // Other necessary dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Material Icons
    implementation("androidx.compose.material:material-icons-extended")

    // Additional library for number picker in Jetpack Compose
    implementation("com.chargemap.compose:numberpicker:1.0.3")

    // Compose form library for managing forms in Jetpack Compose
    implementation("com.github.benjamin-luescher:compose-form:0.2.8")

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


    // Auth
    implementation("androidx.media3:media3-common-ktx:1.5.0-alpha01")

    // Supabase
    implementation("io.github.jan-tennert.supabase:gotrue-kt:1.3.2")
    implementation("io.github.jan-tennert.supabase:compose-auth:1.3.2")
    implementation("io.github.jan-tennert.supabase:compose-auth-ui:1.3.2")
    
    // Google Identity and Credentials
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("com.google.android.gms:play-services-auth:21.2.0") // Check for latest version
//    implementation("com.google.android.gms:play-services-auth:16.0.0")

    implementation("io.github.jan-tennert.supabase:postgrest-kt:1.3.2")
//    implementation("io.github.jan-tennert.supabase:auth-kt:1.3.2")
//    implementation("io.github.jan-tennert.supabase:gotrue-kt:1.3.2")

//    implementation("io.github.jan-tennert.supabase:gotrue-kt:1.3.2")
    implementation(libs.supabase.gotrue.kt)



//    implementation(libs.supabase.auth.kt)

//    implementation(libs.auth.kt)

//    implementation(platform("io.github.jan-tennert.supabase:bom:3.0"))
//    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.0")
//    implementation("io.github.jan-tennert.supabase:auth-kt:3.0")
//    implementation("io.github.jan-tennert.supabase:realtime-kt:3.0")

//    implementation(platform("io.github.jan-tennert.supabase:bom:3.0"))
//    implementation(libs.supabase.postgrest.kt)
//    implementation(libs.auth.kt.v30)
//    implementation(libs.realtime.kt)

//    implementation("io.ktor:ktor-client-[engine]:KTOR_VERSION")
//    implementation(libs.ktor.client.engine.z)


}