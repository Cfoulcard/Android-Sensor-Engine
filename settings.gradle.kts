pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// Resolves the JVM toolchain (kotlin { jvmToolchain(21) } in app/build.gradle.kts) by
// auto-downloading a matching JDK when one isn't already installed locally. Settings
// plugins can't use the version catalog, so the version is a literal here (1.0.0, the
// latest stable release).
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Android-Sensor-Engine"
include(":app")
