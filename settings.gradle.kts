pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // Plugins
            version("kotlin", "2.1.21")
            plugin("multiplatform", "org.jetbrains.kotlin.multiplatform").versionRef("kotlin")
            plugin("android", "org.jetbrains.kotlin.android").versionRef("kotlin")
            plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
            plugin("compose-compiler", "org.jetbrains.kotlin.plugin.compose").versionRef("kotlin")
            plugin("compose-hot-reload", "org.jetbrains.compose.hot-reload").version("1.0.0-beta04")
            plugin("compose-multiplatform", "org.jetbrains.compose").version("1.8.1")
            plugin("android-library", "com.android.library").version("8.8.2")
            plugin("android-application", "com.android.application").version("8.8.2")
            plugin("storytale", "org.jetbrains.compose.storytale").version("0.0.4-alpha01+dev18")
            plugin("kover", "org.jetbrains.kotlinx.kover").version("0.8.3")
            plugin("detekt", "io.gitlab.arturbosch.detekt").version("1.23.8")
            plugin("ksp", "com.google.devtools.ksp").version("2.1.21-2.0.2")
            plugin("maven", "com.vanniktech.maven.publish").version("0.30.0")

            // AndroidX
            library("androidx-appcompat", "androidx.appcompat:appcompat:1.7.1")
            library("androidx-activity-compose", "androidx.activity:activity-compose:1.10.1")
            library("compose-uitooling", "androidx.compose.ui:ui-tooling:1.9.0")

            // Other libraries
            library("phosphoricon", "com.adamglin:phosphor-icon:1.0.0")
        }
    }
}

rootProject.name = "ant-design-kmp"
include(":ui")
