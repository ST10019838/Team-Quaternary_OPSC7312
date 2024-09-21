// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        // Add Google Services plugin classpath
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.android.tools.build:gradle:8.0.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Apply the Google services plugin
    id("com.google.gms.google-services") version "4.4.2" apply false
}

// Remove the allprojects block as it's not necessary if repositories are declared in settings.gradle.kts

// The commented-out plugins block at the bottom can be removed as it's redundant