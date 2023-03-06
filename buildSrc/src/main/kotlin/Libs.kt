object Libs {
    const val JUnit = "junit:junit:4.13.2"
    const val JAudioTagger = "net.jthink:jaudiotagger:3.0.1"
    const val Desugaring = "com.android.tools:desugar_jdk_libs:2.0.2"

    object Accompanist {
        private const val Version = "0.29.1-alpha"

        const val NavigationAnimation = "com.google.accompanist:accompanist-navigation-animation:$Version"
    }

    object AndroidX {
        const val Core = "androidx.core:core-ktx:1.9.0"

        const val AndroidJUnit = "androidx.test.ext:junit:1.1.5"
        const val Espresso = "androidx.test.espresso:espresso-core:3.5.1"

        const val Palette = "androidx.palette:palette:1.0.0"
        const val Media = "androidx.media:media:1.6.0"

        object Benchmark {
            const val UiAutomator = "androidx.test.uiautomator:uiautomator:2.2.0"
            const val Macrobenchmark = "androidx.benchmark:benchmark-macro-junit4:1.2.0-alpha10"
            const val ProfileInstaller = "androidx.profileinstaller:profileinstaller:1.3.0-beta01"
        }

        object Compose {
            const val Ui = "androidx.compose.ui:ui:${Versions.Compose}"
            const val Material3 = "androidx.compose.material3:material3:1.1.0-alpha07"
            const val Icons = "androidx.compose.material:material-icons-extended:${Versions.Compose}"
            const val Activity = "androidx.activity:activity-compose:1.6.1"

            const val UiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.Compose}"
            const val UiTooling = "androidx.compose.ui:ui-tooling:${Versions.Compose}"
            const val UiTestJUnit = "androidx.compose.ui:ui-test-junit4:${Versions.Compose}"
        }

        object Lifecycle {
            private const val Version = "2.5.1"

            const val Runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$Version"
            const val RuntimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:$Version"
            const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$Version"

        }

        object Navigation {
            private const val Version = "2.5.3"

            const val Compose = "androidx.navigation:navigation-compose:$Version"
        }
    }

    object Glide {
        private const val Version = "4.15.0"

        const val Glide = "com.github.bumptech.glide:glide:$Version"
        const val Compiler = "com.github.bumptech.glide:compiler:$Version"

        const val Compose = "com.github.bumptech.glide:compose:1.0.0-alpha.2"
    }

    object Hilt {
        private const val Version = "2.45"

        const val Plugin = "com.google.dagger:hilt-android-gradle-plugin:$Version"
        const val Android = "com.google.dagger:hilt-android:$Version"
        const val AndroidCompiler = "com.google.dagger:hilt-android-compiler:$Version"

        const val ComposeNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
    }

    object Kotlin {
        const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"
        const val SerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0"
    }

    object Room {
        private const val Version = "2.5.0"

        const val Runtime = "androidx.room:room-runtime:$Version"
        const val Compiler = "androidx.room:room-compiler:$Version"
        const val Coroutines = "androidx.room:room-ktx:$Version"
    }
}