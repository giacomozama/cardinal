@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
    id("dagger.hilt.android.plugin")
    id("com.github.ben-manes.versions") version "0.46.0"
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.Kotlin
    kotlin("kapt")
}

android {
    namespace = "me.zama.cardinal"
    compileSdk = 33

    defaultConfig {
        applicationId = "me.zama.cardinal"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.ComposeCompiler
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    coreLibraryDesugaring(Libs.Desugaring)
    
    implementation(Libs.AndroidX.Benchmark.ProfileInstaller)

    implementation(Libs.AndroidX.Compose.Activity)
    implementation(Libs.AndroidX.Compose.Icons)
    implementation(Libs.AndroidX.Compose.Material3)
    implementation(Libs.AndroidX.Compose.Ui)
    implementation(Libs.AndroidX.Compose.UiToolingPreview)

    implementation(Libs.AndroidX.Core)
    implementation(Libs.AndroidX.Lifecycle.Runtime)
    implementation(Libs.AndroidX.Lifecycle.RuntimeCompose)

    implementation(Libs.AndroidX.Media)
    implementation(Libs.AndroidX.Palette)
    implementation(Libs.AndroidX.Navigation.Compose)

    kapt(Libs.Glide.Compiler)
    implementation(Libs.Glide.Compose)
    implementation(Libs.Glide.Glide)

    kapt(Libs.Hilt.AndroidCompiler)
    implementation(Libs.Hilt.Android)
    implementation(Libs.Hilt.ComposeNavigation)

    implementation(Libs.JAudioTagger)

    implementation(Libs.Kotlin.SerializationJson)

    kapt(Libs.Room.Compiler)
    implementation(Libs.Room.Coroutines)
    implementation(Libs.Room.Runtime)

    testImplementation(Libs.JUnit)
    androidTestImplementation(Libs.AndroidX.AndroidJUnit)
    androidTestImplementation(Libs.AndroidX.Espresso)
    androidTestImplementation(Libs.AndroidX.Compose.UiTestJUnit)
    debugImplementation(Libs.AndroidX.Compose.UiTooling)
}