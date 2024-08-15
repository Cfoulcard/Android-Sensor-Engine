// Top-level build file where you can add configuration options common to all sub-projects/modules.
ext {
    val var1 = "C:\\Users\\Taterchip\\Desktop\\private_key.pepk"
    val var2 = "C:\\Users\\Taterchip\\Android Apps\\keystore\\keystore_android_sensor_engine.jks"
}

buildscript {
    val buildGradleVersion = "8.4.2"
    val composeVersion = "1.3.1"
    val kotlinVersion = "1.9.25"
    val googleServicesVersion = "4.4.2"


    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$buildGradleVersion")
        classpath("com.google.gms:google-services:$googleServicesVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }


}

plugins {
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.android.application") version "8.1.4" apply false

}

allprojects {
    repositories {
     //  mavenCentral()
        // google()
       // mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
