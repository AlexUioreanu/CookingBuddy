import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp)
}

// 1. Load local.properties (Standard Android file)
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

// 2. Load secrets.properties (Custom file for local secrets)
val secretsProperties = Properties()
val secretsFile = rootProject.file("secrets.properties")
if (secretsFile.exists()) {
    secretsProperties.load(FileInputStream(secretsFile))
}

// 3. Helper function to extract API keys safely
fun getApiKey(keyName: String): String {
    // Priority 1: System Environment (GitHub Actions / CI)
    val envVar = System.getenv(keyName)
    if (!envVar.isNullOrEmpty()) return envVar
    
    // Priority 2: secrets.properties (Local Development)
    val propVar = secretsProperties.getProperty(keyName)
    if (!propVar.isNullOrEmpty()) return propVar
    
    // Priority 3: Fallback
    return ""
}

// 4. Retrieve keys (Top-level scope to ensure availability)
val geminiApiKey = getApiKey("GEMINI_API_KEY").ifEmpty { getApiKey("Gemini_api_key") }
val falApiKey = getApiKey("FAL_API_KEY")
val falApiBaseUrl = getApiKey("FAL_API_BASE_URL")
val loremFlickrUrl = getApiKey("LOREM_FLICKR_URL")

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
            // Log warning if key is missing (do not log actual key)
            if (geminiApiKey.isEmpty()) {
                println("⚠️ WARNING: GEMINI_API_KEY is empty!")
            }
        }
        debug {
            if (geminiApiKey.isEmpty()) {
                println("⚠️ WARNING: GEMINI_API_KEY is empty!")
            }
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