plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.apollographql.apollo3") version "4.0.0-beta.1"
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.christianfoulcard.android.androidsensorengine"
    compileSdkVersion(34)
    defaultConfig {
        applicationId = "com.christianfoulcard.android.androidsensorengine"
        minSdkVersion(21) // Lollipop
        targetSdkVersion(34)
        versionCode = 12
        versionName = "7"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
//    signingConfigs {
//        release {
//
//        }
//    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
        }
        debug {
            isMinifyEnabled = false
            multiDexEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
//        useIR = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
        kotlinCompilerVersion = "1.7.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
    kapt {
        correctErrorTypes = true
    }
}

apollo {
    service("service") {
        packageName.set("com.androidsensorengine")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:+")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    val appCompatVersion = "1.5.1"
    val constraintLayoutVersion = "2.1.4"
    val materialVersion = "1.6.1"
    val ktxPreferenceVersion = "1.2.0"
    val lifecycleRuntimeKtxVersion = "2.5.1"
    val coreKtxVersion = "1.9.0"
    val fragmentKtxVersion = "1.5.2"
    val lifecycleExtensionsVersion = "2.2.0"
    val lifecycleViewModelKtxVersion = "2.5.1"
    val lifecycleVersion = "2.5.1"
    val timberVersion = "5.0.1"
    val activityComposeVersion = "1.5.1"
    val animationComposeVersion = "1.2.1"
    val lifecycleViewModelComposeVersion = "2.5.1"
    val hiltVersion = "2.48.1"
    val composeVersion = "1.3.1"



    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("com.google.android.material:material:$materialVersion")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleRuntimeKtxVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    implementation("com.apollographql.apollo3:apollo-runtime:4.0.0-beta.1")


    // Compose
    implementation("androidx.activity:activity-compose:$activityComposeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.animation:animation:$animationComposeVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleViewModelComposeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")



// Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

// Required for local unit tests (JUnit 4 framework)
    testImplementation("junit:junit:4.13.2")

// Required for instrumented tests
    androidTestImplementation("com.android.support:support-annotations:28.0.0")
    androidTestImplementation("com.android.support.test:runner:1.0.2")


// Ktx android
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentKtxVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleExtensionsVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleViewModelKtxVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("androidx.preference:preference-ktx:$ktxPreferenceVersion")

// Timber Logging Library
    implementation("com.jakewharton.timber:timber:$timberVersion")

// Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

}

//repositories {
//    mavenCentral()
//}
