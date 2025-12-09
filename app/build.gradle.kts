import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp)
}

// Helper function to read from local.properties
fun getPropValueFromLocalProperties(key: String): String {
    val propertiesFile = rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        val properties = Properties()
        FileInputStream(propertiesFile).use { inputStream ->
            properties.load(inputStream)
        }
        val value = properties.getProperty(key)
        if (value != null) {
            return value
        }
    }
    return ""
}

// Robust Key Retrieval Function
fun getApiKey(keyName: String): String {
    var value = ""
    
    // 1. Try local.properties
    val localVal = getPropValueFromLocalProperties(keyName)
    if (localVal.isNotEmpty()) {
        println("✅ [Config] Found $keyName in local.properties")
        value = localVal
    } else {
        // 2. Fallback: Environment Variables
        val envVar = System.getenv(keyName)
        if (!envVar.isNullOrEmpty()) {
            println("✅ [Config] Found $keyName in Environment Variables")
            value = envVar
        }
    }

    if (value.isEmpty()) {
        println("⚠️ [Config] Could NOT find $keyName")
        return ""
    }

    // SANITIZATION: Remove surrounding quotes if present
    // This fixes issues where secrets are defined as "VALUE" in GitHub or properties
    if (value.length >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
        println("⚠️ [Config] Removing surrounding quotes from $keyName")
        value = value.substring(1, value.length - 1)
    }
    return value
}

println("--- Configuring Build Config Keys ---")
val geminiApiKey = getApiKey("GEMINI_API_KEY")
val falApiKey = getApiKey("FAL_API_KEY")
val falApiBaseUrl = getApiKey("FAL_API_BASE_URL")
val loremFlickrUrl = getApiKey("LOREM_FLICKR_URL")
println("-------------------------------------")

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

        // Inject keys into BuildConfig
        // We use string interpolation with escaped quotes
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")
        buildConfigField("String", "FAL_API_KEY", "\"$falApiKey\"")
        buildConfigField("String", "FAL_API_BASE_URL", "\"$falApiBaseUrl\"")
        buildConfigField("String", "LOREM_FLICKR_URL", "\"$loremFlickrUrl\"")
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
task("testMethod")  {
    doLast {
        println("${getApiKey("GEMINI_API_KEY")}")
    }
}