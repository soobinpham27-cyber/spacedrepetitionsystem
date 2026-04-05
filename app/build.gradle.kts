// File: app/build.gradle.kts (Module level)
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)          // Đổi id("...") thành alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android) // Tương tự với hilt
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.spacedrepetitionsystem"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.spacedrepetitionsystem"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room [cite: 25, 121]
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Retrofit [cite: 25, 123]
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // WorkManager [cite: 122]
    implementation(libs.androidx.work.runtime.ktx)
}