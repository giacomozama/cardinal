buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Libs.Hilt.Plugin)
    }
}

plugins {
    id("com.android.application") version Versions.AndroidGradlePlugin apply false
    id("com.android.library") version Versions.AndroidGradlePlugin apply false
    kotlin("android") version Versions.Kotlin apply false
    id("com.android.test") version Versions.AndroidGradlePlugin apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}