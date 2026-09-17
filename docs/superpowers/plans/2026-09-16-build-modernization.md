# Build Foundation Modernization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Move Android Sensor Engine to Gradle 9.7.0, AGP 9.3.1 (built-in Kotlin), Kotlin 2.4.20, KSP, Hilt 2.60.1, current AndroidX/Compose/Firebase, compileSdk/targetSdk 37 and minSdk 26 — with app behavior unchanged apart from Android 16/17 requirements.

**Architecture:** Five sequential commits on branch `modernize-build`, each leaving the app buildable and runnable: (1) restructure into a version catalog + `plugins {}` DSL at current versions, (2) Kotlin 2 / KSP bridge on the last AGP 8 line, (3) AGP 9 + Gradle 9, (4) library upgrades, (5) SDK 37 behavior changes. Every task ends with the same gate: clean build of debug + release + lint, then a launch check.

**Tech Stack:** Gradle Kotlin DSL, version catalogs, Android Gradle Plugin, Kotlin, KSP, Hilt, Jetpack Compose (Material 2), Firebase Crashlytics/Analytics.

**Spec:** `docs/superpowers/specs/2026-09-16-build-modernization-design.md` — read the **Amendments** section first; it overrides the step order and some versions in the body.

## Global Constraints

- All work on branch `modernize-build`. One commit per task. Never `git add -A` or `git add .` — the working tree has unrelated uncommitted `.idea/` changes that must never be staged.
- No version string may appear in any `build.gradle.kts`; every version lives in `gradle/libs.versions.toml`.
- Never use a pre-release (alpha/beta/rc/milestone) of any dependency, plugin, or Gradle.
- Compatibility fallback: if a plugin/library at the version given here is incompatible, use the newest *stable* release that works, and add a `#` comment on that line in `libs.versions.toml` stating why. Never downgrade AGP or Kotlin below the task's target, and never move them to a pre-release.
- Do not touch: signing keys, `app/google-services.json`, `app/release/`, `.idea/`, `app/src/main/AndroidManifest.xml` (except where a task says so — none do).
- No UI redesign, no Material 3, no LiveData→Flow, no permission cleanup (spec out-of-scope list).
- Final targets: Gradle 9.7.0, AGP 9.3.1, Kotlin 2.4.20, KSP 2.3.12, Hilt 2.60.1, Compose BOM 2026.09.00, Firebase BOM 34.19.0, compileSdk 37, targetSdk 37, minSdk 26, JDK toolchain 21, bytecode target 17.
- Shell: commands below are for **Git Bash** from the repo root `C:/Users/Taterchip/StudioProjects/Android-Sensor-Engine`. Every Gradle invocation must run with `JAVA_HOME` set to JDK 21:
  `export JAVA_HOME="/c/Users/Taterchip/.jdks/jbr-21.0.11"`

### The step gate (run at the end of every task)

```bash
export JAVA_HOME="/c/Users/Taterchip/.jdks/jbr-21.0.11"
./gradlew clean assembleDebug assembleRelease lint --stacktrace
grep -cE 'severity="(Error|Fatal)"' app/build/reports/lint-results-debug.xml
```

Pass criteria: Gradle prints `BUILD SUCCESSFUL`; the `grep -c` prints `0` (issues already in `app/lint-baseline.xml` are filtered out of the report, so any error here is new). If the report file name differs, list `app/build/reports/` and use the `lint-results-*.xml` file for the debug variant.

Then the **launch check** on a running emulator/device (`adb devices` shows one):

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.christianfoulcard.android.androidsensorengine/com.ui.HomeScreenActivity
sleep 5
adb shell pidof com.christianfoulcard.android.androidsensorengine
adb logcat -d -b crash
```

Pass criteria: `pidof` prints a PID and the crash buffer contains no entry for `com.christianfoulcard.android.androidsensorengine`. Clear the crash buffer before each check with `adb logcat -b crash -c`.

---

### Task 0: Prerequisites and baseline (no commit)

**Files:** none modified.

**Interfaces:**
- Consumes: nothing.
- Produces: confirmation that JDK 21, SDK components, and an emulator are available; a record of whether the untouched project builds today.

- [ ] **Step 1: Confirm branch and clean staging area**

```bash
git branch --show-current
git diff --cached --name-only
```

Expected: `modernize-build`; second command prints nothing.

- [ ] **Step 2: Confirm JDK 21**

```bash
"/c/Users/Taterchip/.jdks/jbr-21.0.11/bin/java" -version
```

Expected: `openjdk version "21.0.11"`. If missing, stop and ask the user for a JDK 21 path.

- [ ] **Step 3: Confirm SDK components**

```bash
ls /c/Users/Taterchip/AppData/Local/Android/Sdk/platforms /c/Users/Taterchip/AppData/Local/Android/Sdk/build-tools
```

Required before Task 3: `platforms/android-37` and `build-tools/36.0.0` (or newer 36.x). They are currently **not installed** and there is no `cmdline-tools/` (no `sdkmanager`). Ask the user to install, in Android Studio → Settings → Languages & Frameworks → Android SDK: *SDK Platforms* → Android 17 (API 37); *SDK Tools* → Android SDK Build-Tools 36, Android SDK Command-line Tools (latest), Android Emulator. Also ask for emulators (Device Manager): an API 37 phone, an API 26 phone, and an API 37 tablet. Continue with Tasks 1–2 while waiting; they need neither.

- [ ] **Step 4: Baseline build of the untouched project**

```bash
export JAVA_HOME="/c/Users/Taterchip/.jdks/jbr-21.0.11"
./gradlew clean assembleDebug assembleRelease --stacktrace
```

Record the result. If it fails, capture the first error: Task 1's gate is then "the restructured build succeeds", and report the pre-existing failure to the user before starting Task 1.

---

### Task 1: Restructure into version catalog + plugins DSL at current versions; remove Apollo and dead deps

**Files:**
- Create: `gradle/libs.versions.toml`
- Create (generated): `app/lint-baseline.xml`
- Modify: `build.gradle.kts` (full rewrite)
- Modify: `app/build.gradle.kts` (full rewrite)
- Modify: `gradle.properties` (remove one line)
- Modify: `app/src/main/java/com/application/AndroidSensorEngine.kt`
- Delete: `app/src/main/java/com/application/ApolloClient.kt`, `app/src/main/graphql/` (directory)

**Interfaces:**
- Consumes: nothing.
- Produces: catalog aliases used by all later tasks — plugins `libs.plugins.android.application`, `libs.plugins.kotlin.android`, `libs.plugins.kotlin.kapt`, `libs.plugins.hilt`, `libs.plugins.google.services`, `libs.plugins.firebase.crashlytics`; libraries as listed in the catalog below; `app/lint-baseline.xml`.

- [ ] **Step 1: Create `gradle/libs.versions.toml`** (versions exactly as used today; AGP conflict resolved to 8.4.2)

```toml
[versions]
agp = "8.4.2"
kotlin = "1.9.25"
composeCompiler = "1.5.15"
hilt = "2.48.1"
androidxHiltCompiler = "1.0.0"
googleServices = "4.4.2"
firebaseCrashlyticsPlugin = "3.0.2"
firebaseBom = "33.1.2"
coreKtx = "1.9.0"
appcompat = "1.5.1"
constraintlayout = "2.1.4"
material = "1.12.0"
lifecycle = "2.5.1"
fragmentKtx = "1.5.2"
preferenceKtx = "1.2.0"
activityCompose = "1.5.1"
compose = "1.6.8"
composeAnimation = "1.2.1"
timber = "5.0.1"
junit = "4.13.2"
androidxTestExtJunit = "1.1.3"
espressoCore = "3.4.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
androidx-constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycle" }
androidx-lifecycle-livedata-ktx = { group = "androidx.lifecycle", name = "lifecycle-livedata-ktx", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-ktx = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
androidx-fragment-ktx = { group = "androidx.fragment", name = "fragment-ktx", version.ref = "fragmentKtx" }
androidx-preference-ktx = { group = "androidx.preference", name = "preference-ktx", version.ref = "preferenceKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui", version.ref = "compose" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling", version.ref = "compose" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview", version.ref = "compose" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4", version.ref = "compose" }
androidx-compose-material = { group = "androidx.compose.material", name = "material", version.ref = "compose" }
androidx-compose-runtime-livedata = { group = "androidx.compose.runtime", name = "runtime-livedata", version.ref = "compose" }
androidx-compose-animation = { group = "androidx.compose.animation", name = "animation", version.ref = "composeAnimation" }
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
firebase-crashlytics = { group = "com.google.firebase", name = "firebase-crashlytics" }
firebase-analytics = { group = "com.google.firebase", name = "firebase-analytics" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
androidx-hilt-compiler = { group = "androidx.hilt", name = "hilt-compiler", version.ref = "androidxHiltCompiler" }
timber = { group = "com.jakewharton.timber", name = "timber", version.ref = "timber" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-test-ext-junit = { group = "androidx.test.ext", name = "junit", version.ref = "androidxTestExtJunit" }
androidx-test-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-kapt = { id = "org.jetbrains.kotlin.kapt", version.ref = "kotlin" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
google-services = { id = "com.google.gms.google-services", version.ref = "googleServices" }
firebase-crashlytics = { id = "com.google.firebase.crashlytics", version.ref = "firebaseCrashlyticsPlugin" }
```

- [ ] **Step 2: Replace root `build.gradle.kts` entirely**

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}
```

This deletes the `ext {}` block (unused local key-file paths), `buildscript {}`, the empty `allprojects {}`, and the custom `clean` task.

- [ ] **Step 3: Replace `app/build.gradle.kts` entirely**

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.christianfoulcard.android.androidsensorengine"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.christianfoulcard.android.androidsensorengine"
        minSdk = 24
        targetSdk = 35
        versionCode = 12
        versionName = "8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
        baseline = file("lint-baseline.xml")
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.preference.ktx)

    // Firebase (versions from the BoM)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Logging
    implementation(libs.timber)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
```

Removed relative to the old file: Apollo plugin/`apollo {}`/`apollo-runtime`, `lifecycle-extensions`, duplicate `core-ktx`, `lifecycle-viewmodel-ktx:2.2.0`, `kotlin-stdlib-jdk8` (the Kotlin plugin adds the stdlib), `com.android.support:support-annotations`, `com.android.support.test:runner`, `fileTree("libs")` (no `app/libs` jars exist), the duplicate `kotlin-android` plugin id, and commented-out blocks.

- [ ] **Step 4: Remove Apollo source**

```bash
git rm app/src/main/java/com/application/ApolloClient.kt
git rm -r app/src/main/graphql
```

In `app/src/main/java/com/application/AndroidSensorEngine.kt`, delete the line `import apolloClient` and the line `        apolloClient` inside `onCreate`. The resulting `onCreate` is:

```kotlin
    override fun onCreate() {
        super.onCreate()
        globalAppContext = applicationContext
        initializeTimber()
    }
```

- [ ] **Step 5: Remove Jetifier**

In `gradle.properties`, delete the line `android.enableJetifier=true`. Leave every other line (including the uncommitted `org.gradle.tooling.parallel=true` and its comment) as is.

- [ ] **Step 6: Confirm nothing still references removed artifacts**

```bash
grep -rn "apollo\|lifecycle.extensions\|ViewModelProviders\|ProcessLifecycleOwner\|android\.support\." app/src build.gradle.kts app/build.gradle.kts
```

Expected: no output. If `ProcessLifecycleOwner` or `ViewModelProviders` appears, stop and report (they came from `lifecycle-extensions`).

- [ ] **Step 7: Create the lint baseline**

```bash
export JAVA_HOME="/c/Users/Taterchip/.jdks/jbr-21.0.11"
./gradlew lint
ls app/lint-baseline.xml
```

Expected: lint prints that it created a baseline file; `ls` shows the file.

- [ ] **Step 8: Run the step gate and launch check** (see Global Constraints). Expected: `BUILD SUCCESSFUL`, `0`, app launches.

- [ ] **Step 9: Commit**

```bash
git add gradle/libs.versions.toml build.gradle.kts app/build.gradle.kts gradle.properties app/lint-baseline.xml app/src/main/java/com/application/AndroidSensorEngine.kt
git status --short
git commit -m "build: move to version catalog and plugins DSL, remove Apollo and dead deps

Co-Authored-By: Claude Opus 5 <noreply@anthropic.com>"
```

Before committing, check `git status --short`: staged (`M `/`A `/`D `) entries must be only the files above plus the two `git rm` deletions. No `.idea/` path may be staged.

---

### Task 2: Kotlin 2.3.21 + Compose compiler plugin + KSP + Hilt 2.58 on AGP 8.13.2 / Gradle 8.14.5

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `build.gradle.kts` (full rewrite)
- Modify: `app/build.gradle.kts` (full rewrite)
- Modify: `gradle/wrapper/gradle-wrapper.properties`, `gradle/wrapper/gradle-wrapper.jar`, `gradlew`, `gradlew.bat` (regenerated)
- Modify: any Kotlin source under `app/src` that fails to compile under K2 (unknown in advance)

**Interfaces:**
- Consumes: Task 1 catalog and aliases.
- Produces: plugin aliases `libs.plugins.kotlin.compose` and `libs.plugins.ksp`; alias `libs.plugins.kotlin.kapt` and library `libs.androidx.hilt.compiler` **no longer exist**; `kotlin { jvmToolchain(21); compilerOptions { jvmTarget } }` block in `app/build.gradle.kts`.

- [ ] **Step 1: Point the wrapper at Gradle 8.14.5**

In `gradle/wrapper/gradle-wrapper.properties` set:

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.14.5-bin.zip
```

(Edit the file directly: running the `wrapper` task first would configure the project with the old Gradle.)

- [ ] **Step 2: Update `gradle/libs.versions.toml`**

In `[versions]` change/add/remove exactly:

```toml
agp = "8.13.2"
kotlin = "2.3.21"
ksp = "2.3.12"
hilt = "2.58"
```

Delete the `composeCompiler` and `androidxHiltCompiler` version lines.
In `[libraries]` delete the `androidx-hilt-compiler` line (it only processes `@HiltWorker`, which the app does not use).
In `[plugins]` delete the `kotlin-kapt` line and add:

```toml
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

- [ ] **Step 3: Replace root `build.gradle.kts` entirely**

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}
```

- [ ] **Step 4: Replace `app/build.gradle.kts` entirely**

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.christianfoulcard.android.androidsensorengine"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.christianfoulcard.android.androidsensorengine"
        minSdk = 24
        targetSdk = 35
        versionCode = 12
        versionName = "8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
        baseline = file("lint-baseline.xml")
    }
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.preference.ktx)

    // Firebase (versions from the BoM)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Logging
    implementation(libs.timber)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
```

Changes vs Task 1: kapt plugin/block and `androidx.hilt` compiler removed, `ksp(libs.hilt.compiler)`, Compose compiler plugin replaces `composeOptions`, `kotlinOptions` → top-level `kotlin {}`, Java 17 target, `packagingOptions` → `packaging`.

- [ ] **Step 5: Regenerate wrapper files with the new Gradle**

```bash
export JAVA_HOME="/c/Users/Taterchip/.jdks/jbr-21.0.11"
./gradlew wrapper --gradle-version 8.14.5 --distribution-type bin
./gradlew --version
```

Expected: `Gradle 8.14.5`. If configuration fails here, the error is a build-script problem — fix it before continuing (the wrapper task needs a configurable project).

- [ ] **Step 6: Compile and fix K2 errors**

```bash
./gradlew :app:compileDebugKotlin --stacktrace
```

Expected: `BUILD SUCCESSFUL`. If Kotlin errors appear, fix each at its reported file:line with the smallest change that keeps behavior (typical K2 fixes: add an explicit type, replace a smart-cast that K2 rejects with a local `val`, add `!!`-free null handling that mirrors the old runtime path). Do not refactor beyond the error. Re-run until green.

If Hilt/KSP fails with a Kotlin-metadata version error, apply the compatibility fallback: try Hilt `2.57.2`, then `2.56.2`, adding a catalog comment.

- [ ] **Step 7: Run the step gate and launch check.** Expected: `BUILD SUCCESSFUL`, `0`, app launches.

- [ ] **Step 8: Manual checklist** — ask the user to run it on the API 35-or-newer phone emulator/device:

1. Fresh install; app launches to home screen.
2. Open each sensor screen: Sound, Light, Pressure, Ambient Temperature, Battery, System, Humidity, Location. Each shows live or "unavailable" data without crashing.
3. Grant microphone permission on Sound; decibel readings update.
4. Grant location permission on Location; coordinates populate.
5. Back navigation from every sensor screen returns to home.
6. Background and foreground the app on the Sound and Location screens.
7. Release build installed (see "Installing the release build" below): repeat items 1–2.

**Installing the release build** (release has no signing config; sign with the debug key for testing only):

```bash
BT=$(ls -d /c/Users/Taterchip/AppData/Local/Android/Sdk/build-tools/* | sort -V | tail -1)
"$BT/apksigner.bat" sign --ks "$HOME/.android/debug.keystore" --ks-pass pass:android --key-pass pass:android \
  --out app/build/outputs/apk/release/app-release-test-signed.apk \
  app/build/outputs/apk/release/app-release-unsigned.apk
adb uninstall com.christianfoulcard.android.androidsensorengine
adb install app/build/outputs/apk/release/app-release-test-signed.apk
```

Wait for the user's confirmation before committing. If an item fails, fix it in this task.

- [ ] **Step 9: Commit**

```bash
git add gradle/libs.versions.toml build.gradle.kts app/build.gradle.kts gradle/wrapper/gradle-wrapper.properties gradle/wrapper/gradle-wrapper.jar gradlew gradlew.bat
git add -u app/src
git status --short
git commit -m "build: Kotlin 2.3 with Compose compiler plugin, kapt to KSP, AGP 8.13, Gradle 8.14

Co-Authored-By: Claude Opus 5 <noreply@anthropic.com>"
```

`git add -u app/src` stages only modifications to already-tracked source files (K2 fixes). Confirm no `.idea/` path is staged.

---

### Task 3: AGP 9.3.1 (built-in Kotlin) + Gradle 9.7.0 + Kotlin 2.4.20 + Hilt 2.60.1 + compileSdk 37

**Prerequisite:** `platforms/android-37` and `build-tools/36.x` installed (Task 0 Step 3).

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `build.gradle.kts` (full rewrite)
- Modify: `app/build.gradle.kts` (full rewrite)
- Modify: `gradle.properties` (full rewrite)
- Modify: `gradle/wrapper/*`, `gradlew`, `gradlew.bat` (regenerated)
- Modify: `app/proguard-rules.pro` only if the release check fails

**Interfaces:**
- Consumes: Task 2 catalog (`kotlin-compose`, `ksp` plugin aliases).
- Produces: plugin alias `libs.plugins.kotlin.android` **no longer exists**; new library alias `libs.kotlin.gradle.plugin`; compileSdk 37 for Task 4's library upgrades.

- [ ] **Step 1: Point the wrapper at Gradle 9.7.0**

In `gradle/wrapper/gradle-wrapper.properties` set:

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-9.7.0-bin.zip
```

- [ ] **Step 2: Update `gradle/libs.versions.toml`**

In `[versions]` set:

```toml
agp = "9.3.1"
kotlin = "2.4.20"
hilt = "2.60.1"
googleServices = "4.5.0"
firebaseCrashlyticsPlugin = "3.0.8"
```

In `[libraries]` add:

```toml
kotlin-gradle-plugin = { group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version.ref = "kotlin" }
```

In `[plugins]` delete the `kotlin-android` line.

- [ ] **Step 3: Replace root `build.gradle.kts` entirely**

```kotlin
// AGP 9 has built-in Kotlin; this pins the Kotlin Gradle plugin above AGP's bundled minimum.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}
```

- [ ] **Step 4: Replace `app/build.gradle.kts` entirely**

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.christianfoulcard.android.androidsensorengine"
    compileSdk = 37
    defaultConfig {
        applicationId = "com.christianfoulcard.android.androidsensorengine"
        minSdk = 24
        targetSdk = 35
        versionCode = 12
        versionName = "8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
        baseline = file("lint-baseline.xml")
    }
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.preference.ktx)

    // Firebase (versions from the BoM)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Logging
    implementation(libs.timber)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
```

Changes vs Task 2: no `kotlin-android` plugin (built-in Kotlin), `compileSdk = 37`, `multiDexEnabled` removed (Hilt 2.60 dropped multidex support; minSdk ≥ 21 has native multidex).

If AGP 9's new DSL rejects `compileSdk = 37`, use:

```kotlin
    compileSdk {
        version = release(37)
    }
```

- [ ] **Step 5: Replace `gradle.properties` entirely**

```properties
# Gradle
org.gradle.jvmargs=-Xmx6g
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configuration-cache=true
# Enabled parallel sync for Gradle 9.4+
org.gradle.tooling.parallel=true

# Android
android.useAndroidX=true

# Kotlin
kotlin.code.style=official
kotlin.incremental=true
```

Removed: `android.defaults.buildfeatures.buildconfig` (BuildConfig unused), `android.nonTransitiveRClass=false`, `android.nonFinalResIds=false`, `org.gradle.configureondemand`, and the stale comment header.

- [ ] **Step 6: Regenerate wrapper files and verify toolchain versions**

```bash
export JAVA_HOME="/c/Users/Taterchip/.jdks/jbr-21.0.11"
./gradlew wrapper --gradle-version 9.7.0 --distribution-type bin
./gradlew --version
./gradlew buildEnvironment | grep -E "com.android.tools.build:gradle:|kotlin-gradle-plugin:"
```

Expected: `Gradle 9.7.0`; resolved `com.android.tools.build:gradle:9.3.1` and `kotlin-gradle-plugin:...2.4.20` (the arrow form `-> 2.4.20` is fine).

- [ ] **Step 7: Compile and fix errors**

```bash
./gradlew :app:compileDebugKotlin --stacktrace
```

Expected: `BUILD SUCCESSFUL`. Fix by category:
- **Configuration cache problem reported by a plugin:** add `org.gradle.configuration-cache.problems=warn` to `gradle.properties` with a comment line above it naming the plugin. Do not disable the cache.
- **`Unresolved reference: R` / resource from a library** (non-transitive R now default): qualify with the library's R, e.g. `com.google.android.material.R.attr.colorPrimary`, or import it with an alias `import androidx.appcompat.R as AppCompatR`. Do not restore the property.
- **`when` on a resource id needs a constant** (non-final R): replace `when` with an `if/else` chain on the same ids.
- **Kotlin 2.4 deprecation-now-error:** smallest behavior-preserving change at the reported line.
- **Hilt/KSP/Crashlytics/google-services incompatible with AGP 9.3.1:** apply the compatibility fallback rule.

- [ ] **Step 8: Run the step gate and launch check.** Expected: `BUILD SUCCESSFUL`, `0`, app launches. If `assembleRelease` fails in R8 (AGP 9 enables R8 strict full mode for keep rules), add the keep rule R8 names in its error message to `app/proguard-rules.pro` and re-run.

- [ ] **Step 9: Manual checklist** — same seven items and release-install commands as Task 2 Step 8, on an API 37 phone emulator. Wait for the user's confirmation. If the release build crashes on launch, run `adb logcat -d -b crash`, add a `-keep` rule for the missing class to `app/proguard-rules.pro`, rebuild, and re-check.

- [ ] **Step 10: Commit**

```bash
git add gradle/libs.versions.toml build.gradle.kts app/build.gradle.kts gradle.properties gradle/wrapper/gradle-wrapper.properties gradle/wrapper/gradle-wrapper.jar gradlew gradlew.bat app/proguard-rules.pro
git add -u app/src
git status --short
git commit -m "build: AGP 9.3 with built-in Kotlin, Gradle 9.7, Kotlin 2.4, Hilt 2.60, compileSdk 37

Co-Authored-By: Claude Opus 5 <noreply@anthropic.com>"
```

Confirm no `.idea/` path is staged.

---

### Task 4: Upgrade libraries (AndroidX, Compose BOM, Firebase, Material, tests)

**Files:**
- Modify: `gradle/libs.versions.toml` (full rewrite)
- Modify: `app/build.gradle.kts` (dependencies block only)
- Modify: `app/src/main/java/com/preferences/SettingsActivity.java:41`
- Modify: other `app/src` files only where the new libraries cause compile errors
- Modify: `app/proguard-rules.pro` only if the release check fails

**Interfaces:**
- Consumes: Task 3 catalog aliases.
- Produces: library alias `libs.androidx.compose.bom`; Compose library aliases now have no `version.ref`; version keys `compose`, `composeAnimation` no longer exist. `androidx.activity` ≥ 1.13.0 provides `enableEdgeToEdge()` for Task 5.

- [ ] **Step 1: Replace `gradle/libs.versions.toml` entirely**

```toml
[versions]
agp = "9.3.1"
kotlin = "2.4.20"
ksp = "2.3.12"
hilt = "2.60.1"
googleServices = "4.5.0"
firebaseCrashlyticsPlugin = "3.0.8"
firebaseBom = "34.19.0"
composeBom = "2026.09.00"
coreKtx = "1.19.0"
appcompat = "1.8.0"
constraintlayout = "2.2.2"
material = "1.14.0"
lifecycle = "2.11.0"
fragmentKtx = "1.9.0"
preferenceKtx = "1.2.1"
activityCompose = "1.13.0"
timber = "5.0.1"
junit = "4.13.2"
androidxTestExtJunit = "1.3.0"
espressoCore = "3.7.0"

[libraries]
kotlin-gradle-plugin = { group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version.ref = "kotlin" }
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
androidx-constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycle" }
androidx-lifecycle-livedata-ktx = { group = "androidx.lifecycle", name = "lifecycle-livedata-ktx", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-ktx = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
androidx-fragment-ktx = { group = "androidx.fragment", name = "fragment-ktx", version.ref = "fragmentKtx" }
androidx-preference-ktx = { group = "androidx.preference", name = "preference-ktx", version.ref = "preferenceKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material = { group = "androidx.compose.material", name = "material" }
androidx-compose-runtime-livedata = { group = "androidx.compose.runtime", name = "runtime-livedata" }
androidx-compose-animation = { group = "androidx.compose.animation", name = "animation" }
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
firebase-crashlytics = { group = "com.google.firebase", name = "firebase-crashlytics" }
firebase-analytics = { group = "com.google.firebase", name = "firebase-analytics" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
timber = { group = "com.jakewharton.timber", name = "timber", version.ref = "timber" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-test-ext-junit = { group = "androidx.test.ext", name = "junit", version.ref = "androidxTestExtJunit" }
androidx-test-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
google-services = { id = "com.google.gms.google-services", version.ref = "googleServices" }
firebase-crashlytics = { id = "com.google.firebase.crashlytics", version.ref = "firebaseCrashlyticsPlugin" }
```

If any version above has a newer **stable** release when you run this task, you may use it (spec rule); check with e.g. `curl -s https://dl.google.com/dl/android/maven2/androidx/core/core-ktx/maven-metadata.xml`.

- [ ] **Step 2: Apply the Compose BOM in `app/build.gradle.kts`**

Replace the `// Compose` section of the `dependencies` block with:

```kotlin
    // Compose (versions from the BoM)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    debugImplementation(composeBom)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
```

Nothing else in `app/build.gradle.kts` changes.

- [ ] **Step 3: Replace the deprecated back call in `SettingsActivity.java`**

In `app/src/main/java/com/preferences/SettingsActivity.java`, change:

```java
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
```

to:

```java
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
```

- [ ] **Step 4: Verify resolved versions**

```bash
export JAVA_HOME="/c/Users/Taterchip/.jdks/jbr-21.0.11"
./gradlew :app:dependencies --configuration releaseRuntimeClasspath | grep -E "androidx.core:core-ktx|androidx.compose.ui:ui:|androidx.activity:activity-compose|firebase-crashlytics:|lifecycle-runtime-ktx"
```

Expected: each line resolves to the catalog version, or to a higher version via `->` from BOM/transitive alignment. No `FAILED` entries.

- [ ] **Step 5: Compile and fix errors**

```bash
./gradlew :app:compileDebugKotlin :app:compileDebugJavaWithJavac --stacktrace
```

Expected: `BUILD SUCCESSFUL`. Fix each compile error with the replacement API named in the library's deprecation message, changing only the reported lines. Warnings are allowed.

- [ ] **Step 6: Run the step gate and launch check.** Expected: `BUILD SUCCESSFUL`, `0`, app launches.

- [ ] **Step 7: Manual checklist** — same seven items and release-install commands as Task 2 Step 8, on the API 37 phone emulator. Wait for the user's confirmation; fix R8 crashes with keep rules as in Task 3 Step 9.

- [ ] **Step 8: Commit**

```bash
git add gradle/libs.versions.toml app/build.gradle.kts app/proguard-rules.pro
git add -u app/src
git status --short
git commit -m "build: upgrade AndroidX, Compose BOM, Firebase, Material and test libraries

Co-Authored-By: Claude Opus 5 <noreply@anthropic.com>"
```

Confirm no `.idea/` path is staged.

---

### Task 5: targetSdk 37, minSdk 26, Android 16/17 behavior changes

**Files:**
- Modify: `app/build.gradle.kts` (`defaultConfig` only)
- Modify: `app/src/main/java/com/ui/HomeScreenActivity.kt:49,58`
- Modify: `app/src/main/java/com/BaseSensorActivity.kt:6,15`
- Delete: `app/src/main/java/com/utils/SystemUi.kt`
- Modify: `app/src/main/java/com/sensors/audio/AudioRecorder.kt` (`pauseRecorder`, `resumeRecorder`)
- Modify: sensor screen composables under `app/src/main/java/com/ui/` only where the edge-to-edge check shows overlap

**Interfaces:**
- Consumes: `androidx.activity.enableEdgeToEdge` (activity ≥ 1.13.0 from Task 4).
- Produces: final state. `com.utils.SystemUi` no longer exists.

- [ ] **Step 1: Update `defaultConfig` in `app/build.gradle.kts`**

```kotlin
    defaultConfig {
        applicationId = "com.christianfoulcard.android.androidsensorengine"
        minSdk = 26
        targetSdk = 37
        versionCode = 12
        versionName = "8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
```

(`versionCode`/`versionName` are unchanged; bumping them for a Play release is the user's call.)

- [ ] **Step 2: Switch `HomeScreenActivity` to `enableEdgeToEdge()`**

In `app/src/main/java/com/ui/HomeScreenActivity.kt`:
- Replace the import line `import com.utils.SystemUi` with `import androidx.activity.enableEdgeToEdge` (then move it to sit alphabetically after `import androidx.activity.compose.setContent`).
- Replace `        SystemUi().hideSystemUIFull(this)` with `        enableEdgeToEdge()`.

Resulting `onCreate` start:

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
```

- [ ] **Step 3: Remove the legacy window flag from `BaseSensorActivity`**

`app/src/main/java/com/BaseSensorActivity.kt` already calls `enableEdgeToEdge()`. Delete the line `import com.utils.SystemUi` and the line `        SystemUi().hideSystemUIFull(this)`. Resulting `onCreate`:

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
```

- [ ] **Step 4: Delete `SystemUi.kt`**

```bash
grep -rn "SystemUi" app/src
```

Expected: only `app/src/main/java/com/utils/SystemUi.kt` itself. Then:

```bash
git rm app/src/main/java/com/utils/SystemUi.kt
```

(`hideSystemUIFull` set `FLAG_LAYOUT_NO_LIMITS`, which `enableEdgeToEdge()` supersedes; `hideSystemUI` had no callers.)

- [ ] **Step 5: Remove always-true API 24 checks in `AudioRecorder.kt`**

In `app/src/main/java/com/sensors/audio/AudioRecorder.kt`, replace:

```kotlin
                recorder?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pause()
                        Timber.tag(TAG).d("Audio Recorder paused")
                    }
                }
```

with:

```kotlin
                recorder?.apply {
                    pause()
                    Timber.tag(TAG).d("Audio Recorder paused")
                }
```

and replace:

```kotlin
                recorder?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        resume()
                        Timber.tag(TAG).d("Audio Recorder resumed")
                    }
                }
```

with:

```kotlin
                recorder?.apply {
                    resume()
                    Timber.tag(TAG).d("Audio Recorder resumed")
                }
```

Keep the `Build.VERSION_CODES.S` check in `filePath` and the `android.os.Build` import (still used there). Then find any other now-dead checks:

```bash
grep -rnE "SDK_INT\s*(>=|<)\s*(Build\.VERSION_CODES\.)?(N|N_MR1|O|2[4-6])\b" app/src/main/java | grep -v "^\S*:\s*//"
```

For each hit that is not commented out: a `>= N/N_MR1/O` (or 24–26) branch is always true — keep only its body; a `< N/N_MR1/O` branch is dead — delete it. Keep all checks for P and above. `LayoutSizingUtils.kt` (R) is not affected.

- [ ] **Step 6: Run the step gate and launch check** on the API 37 phone. Expected: `BUILD SUCCESSFUL`, `0`, app launches.

- [ ] **Step 7: Edge-to-edge inspection (API 37 phone)**

For the home screen and each of the 8 sensor screens, take a screenshot and look at the top and bottom edges:

```bash
adb shell screencap -p /sdcard/s.png && adb pull /sdcard/s.png screenshots-home.png
```

(Save screenshots outside the repo or delete them before committing.) A screen **fails** if text, buttons, or icons are drawn under the status bar or navigation bar (decorative backgrounds/gradients under the bars are fine and expected). For each failing screen, find its root content composable (the outermost `Column`/`Box` inside `setContent { AndroidSensorEngineTheme { ... } }` in the activity, or the screen composable it calls in `app/src/main/java/com/ui/composables/`) and add system-bar insets padding as the first modifier, leaving backgrounds outside it:

```kotlin
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding

Column(
    modifier = Modifier
        .windowInsetsPadding(WindowInsets.systemBars)
        .fillMaxSize()
    // ...existing modifiers and arguments unchanged
)
```

Rebuild, reinstall, and re-screenshot until every screen passes.

- [ ] **Step 8: Background audio check (API 37 phone)**

Open the Sound screen, grant the microphone permission, wait 5 seconds, press Home, wait 10 seconds, reopen the app.

```bash
adb logcat -d | grep -iE "AudioRecord|MediaRecorder|FATAL|AndroidRuntime" | tail -30
```

Expected: no `FATAL`/`AndroidRuntime` crash; no repeated recorder errors after backgrounding.

- [ ] **Step 9: Large-screen / rotation check (API 37 tablet, landscape)**

Portrait locks are ignored on screens ≥600dp at targetSdk 37. On the tablet emulator in landscape, open the home screen and each sensor screen; rotate each once (`adb shell settings put system user_rotation 0` then `1`, with auto-rotate off). A screen **fails** if it crashes or its content is cut off with no way to scroll to it. For a clipped screen, add vertical scrolling to its root content `Column`:

```kotlin
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

Column(
    modifier = Modifier
        .windowInsetsPadding(WindowInsets.systemBars)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    // ...existing modifiers and arguments unchanged
)
```

Do not add `verticalScroll` to a `Column` that already contains a `LazyColumn`/`LazyVerticalGrid` with unbounded height (e.g. `HomeScreenActivity`'s grid) — that crashes; for the home screen, give the grid `Modifier.heightIn(max = 400.dp)` instead only if it is clipped.

After rotating on each sensor screen, check that sensors were not leaked:

```bash
adb shell dumpsys sensorservice | grep -A20 "active connections" | grep -c androidsensorengine
```

Expected after returning to the home screen: `0` (or the same count as before opening any sensor screen).

- [ ] **Step 10: Min-SDK check (API 26 phone)**

Run the launch check and manual checklist items 1–5 on the API 26 emulator.

- [ ] **Step 11: Full manual checklist** — the seven items and release-install commands from Task 2 Step 8, on the API 37 phone. Wait for the user's confirmation of Steps 7–11.

- [ ] **Step 12: Final gate and commit**

```bash
export JAVA_HOME="/c/Users/Taterchip/.jdks/jbr-21.0.11"
./gradlew clean assembleDebug assembleRelease lint --stacktrace
grep -cE 'severity="(Error|Fatal)"' app/build/reports/lint-results-debug.xml
git add app/build.gradle.kts app/proguard-rules.pro
git add -u app/src
git status --short
git commit -m "feat: target SDK 37 with minSdk 26 and Android 16/17 edge-to-edge and large-screen fixes

Co-Authored-By: Claude Opus 5 <noreply@anthropic.com>"
```

Expected: `BUILD SUCCESSFUL`, `0`. Confirm no `.idea/` path or screenshot is staged.

- [ ] **Step 13: Final verification of end state**

```bash
grep -rnE '"[0-9]+\.[0-9]+(\.[0-9]+)?"' build.gradle.kts app/build.gradle.kts
grep -rn "kapt\|apollo\|kotlinOptions\|composeOptions\|packagingOptions\|enableJetifier\|configureondemand" build.gradle.kts app/build.gradle.kts gradle.properties
git log --oneline master..modernize-build
```

Expected: first two commands print nothing; log shows the spec/plan commit(s) plus the five task commits.
