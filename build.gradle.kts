// Top-level build file where you can add configuration options common to all sub-projects/modules.
ext {
    val var1 = "C:\\Users\\Taterchip\\Desktop\\private_key.pepk"
    val var2 = "C:\\Users\\Taterchip\\Android Apps\\keystore\\keystore_android_sensor_engine.jks"
}

buildscript {
    val buildGradleVersion = "7.2.2"
    val composeVersion = "1.3.1"
    val kotlinVersion = "1.6.10"
    val googleServicesVersion = "4.3.13"

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$buildGradleVersion")
        classpath("com.google.gms:google-services:$googleServicesVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
    }
}

allprojects {
    repositories {
       // google()
       // mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
