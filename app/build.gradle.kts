import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp)
}

// Function to load API keys from local.properties
// Adapted from the guide for Kotlin DSL and multiple keys
fun getApiKey(propertyKey: String): String {
    val propFile = rootProject.file("./local.properties")
    val properties = Properties()
    
    // Only load if file exists (avoids crash during sync if file is missing)
    if (propFile.exists()) {
        FileInputStream(propFile).use { inputStream ->
            properties.load(inputStream)
        }
    }

    var value = properties.getProperty(propertyKey) ?: ""

    // Fallback to Environment Variables (Standard CI practice)
    if (value.isEmpty()) {
        value = System.getenv(propertyKey) ?: ""
    }

    // SANITIZATION: The blog post example and CI often add quotes (e.g., KEY="123")
    // We must remove them so we don't end up with "\"123\"" in Java code.
    if (value.length >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
        value = value.substring(1, value.length - 1)
    }

    return value
}

android {
    namespace = "com.example.cookingbuddy"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.cookingbuddy"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Inject keys into BuildConfig using the helper function
        // Note: We explicitly wrap the value in quotes for Java String literal
        buildConfigField("String", "GEMINI_API_KEY", "\"${getApiKey("GEMINI_API_KEY")}\"")
        buildConfigField("String", "FAL_API_KEY", "\"${getApiKey("FAL_API_KEY")}\"")
        buildConfigField("String", "FAL_API_BASE_URL", "\"${getApiKey("FAL_API_BASE_URL")}\"")
        buildConfigField("String", "LOREM_FLICKR_URL", "\"${getApiKey("LOREM_FLICKR_URL")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    // Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Common
    implementation(libs.common)
    implementation(libs.gson)

    // Network & AI
    implementation(libs.ai.fal.client)
    implementation(libs.google.generativeai)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Orbit MVI
    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)
    testImplementation(libs.orbit.test)

    // Koin DI
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coil Image Loading
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}