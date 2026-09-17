# Build Foundation Modernization — Design

**Date:** 2026-09-16
**Branch:** `modernize-build`
**Status:** Approved; amended during planning (see Amendments — they override the sections below)

## Amendments (2026-09-16, during planning)

Compatibility research while planning invalidated parts of the original sequence. The user approved changes 1–2; 3–7 follow from them or from code inspection.

1. **Bridge step on AGP 8.** AGP 9 requires Kotlin Gradle plugin ≥ 2.2.10 (built-in Kotlin), Kotlin 2 requires the Compose compiler plugin, and Hilt ≥ 2.59 requires AGP 9 — so original Steps 2 and 3 cannot be separate green commits. New order:
   - Step 2 — *Kotlin 2 on AGP 8*: Gradle 8.14.5, AGP 8.13.2, Kotlin 2.3.21 + Compose compiler plugin, KSP 2.3.12 (kapt removed), Hilt 2.58, JVM target 17, `kotlinOptions` → `kotlin { compilerOptions }`, `packagingOptions` → `packaging`.
   - Step 3 — *AGP 9*: Gradle 9.7.0, AGP 9.3.1 built-in Kotlin, Kotlin 2.4.20, Hilt 2.60.1, Crashlytics plugin 3.0.8, Google Services 4.5.0, `gradle.properties` changes, configuration cache, compileSdk 37.
2. **Final toolchain versions:** AGP **9.3.1** (not 9.4.0) and Gradle **9.7.0** (not 9.7.1), to stay inside Kotlin 2.4.20's official compatibility range (AGP 8.5.2–9.3.1, Gradle 7.6.3–9.7.0). AGP 9.3 supports API 37.
3. **compileSdk 37 moves to Step 3** (targetSdk stays 35 until Step 5). Current AndroidX releases require a recent compileSdk, so Step 4's library upgrades need it first. Changing compileSdk alone does not change runtime behavior.
4. **Kotlin Gradle plugin version under AGP 9** is pinned via root `buildscript { dependencies { classpath(libs.kotlin.gradle.plugin) } }`, as documented by AGP 9 for using a KGP newer than its bundled minimum. This is the only `buildscript` content allowed.
5. **`androidx.hilt:hilt-compiler` is removed, not migrated to KSP.** It only processes `@HiltWorker`; the app has no `@HiltWorker` and no `androidx.hilt` runtime artifacts.
6. **`multiDexEnabled` is removed in Step 3** (Hilt 2.60 dropped multidex support), not Step 5.
7. **`extras/PinShortcuts.kt` is entirely commented out** — no edit needed, and manual checklist item 6 (pin a shortcut) is dropped.
8. **Command-line builds use JDK 21** (`C:\Users\Taterchip\.jdks\jbr-21.0.11`). Android Studio's bundled JBR 25 cannot run Gradle 8.x.

## Goal

Bring Android Sensor Engine's build foundation up to date — Android 17 (SDK 37), current Gradle, Android Gradle Plugin (AGP), Kotlin, and all dependencies — plus the code migrations those upgrades force. App behavior and UI stay the same, apart from what Android 16/17 behavior changes require.

## Scope

**In scope**

- Gradle wrapper, AGP, Kotlin, KSP, Hilt, Compose, AndroidX, Firebase, Material, test library upgrades
- Version catalog (`gradle/libs.versions.toml`) and `plugins {}` DSL for all plugins
- kapt → KSP; Compose compiler → Kotlin Compose plugin; AGP 9 built-in Kotlin
- Removing Apollo GraphQL (instantiated but never queried) and dead/legacy dependencies
- compileSdk/targetSdk 37, minSdk 26, JVM target 17
- Fixes required by SDK 36/37 behavior changes (edge-to-edge, predictive back, large-screen orientation)

**Out of scope (follow-ups)**

- Material 2 → Material 3
- LiveData → Flow
- Single-activity / Navigation migration
- Removing unused manifest permissions (`WRITE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE`, `ACTIVITY_RECOGNITION`, `WAKE_LOCK`, `POST_NOTIFICATIONS`)
- Deleting the unregistered `SettingsActivity.java` / fully commented-out `SettingsActivity.kt`
- Tablet/landscape redesign
- Adding unit or instrumented test coverage

## Starting state (as of 2026-09-16)

| Area | Current |
|---|---|
| Gradle wrapper | 8.6 |
| AGP | 8.4.2 (`buildscript` classpath) **and** 8.1.4 (`plugins {}`) — conflicting |
| Kotlin | 1.9.25, `composeOptions.kotlinCompilerExtensionVersion = "1.5.15"` |
| Annotation processing | kapt, Hilt 2.48.1 |
| SDK | compileSdk 35, targetSdk 35, minSdk 24 |
| Java/JVM target | 1.8 |
| Compose | 1.6.8 per-artifact, no BOM, Material 2 |
| Apollo | `com.apollographql.apollo3` 4.0.0-beta.1; client created in `AndroidSensorEngine.onCreate`, no queries executed |
| Versions | Inline `val` strings in `app/build.gradle.kts`; no version catalog |
| Tests | 3 instrumented test files, no unit tests |
| Local SDK | Only `platforms/android-35` installed (no 36/37); build-tools up to 34.0.0 |
| IDE | Android Studio 2026.1.4 (bundled JBR 25); project JDK `jbr-21` |

## Target versions

Latest stable releases, checked against Maven Central / Google Maven on 2026-09-16. If a release is newer at implementation time and is stable, it may be used; pre-releases (alpha/beta/rc) may not.

| Component | Target |
|---|---|
| Gradle wrapper | 9.7.1 |
| AGP | 9.4.0 |
| Kotlin | 2.4.20 |
| KSP | 2.3.12 |
| Hilt (`com.google.dagger`) | 2.60.1 |
| `androidx.hilt:hilt-compiler` | 1.4.0 |
| Compose BOM | 2026.09.00 |
| Firebase BOM | 34.19.0 |
| Firebase Crashlytics Gradle plugin | 3.0.8 |
| Google Services plugin | 4.5.0 |
| `androidx.core:core-ktx` | 1.19.0 |
| `androidx.activity:activity-compose` | 1.13.0 |
| `androidx.lifecycle:*` | 2.11.0 |
| `androidx.appcompat:appcompat` | 1.8.0 |
| `androidx.fragment:fragment-ktx` | 1.9.0 |
| `androidx.preference:preference-ktx` | 1.2.1 |
| `androidx.constraintlayout:constraintlayout` | 2.2.2 |
| `com.google.android.material:material` | 1.14.0 |
| Timber | 5.0.1 |
| `androidx.test.ext:junit` | 1.3.0 |
| `androidx.test.espresso:espresso-core` | 3.7.0 |
| JUnit | 4.13.2 |
| compileSdk / targetSdk | 37 / 37 |
| minSdk | 26 |
| JDK toolchain / bytecode target | 21 / 17 |

**Compatibility fallback:** if the Crashlytics plugin, Google Services plugin, KSP, or Hilt release is incompatible with AGP 9.4.0 or Kotlin 2.4.20, use the newest *stable* release of that component that works, pin it in the catalog, and record the reason as a comment in `libs.versions.toml`. Do not move AGP or Kotlin to a pre-release to work around it.

## End-state build architecture

### `gradle/libs.versions.toml`

The single source of every version. Sections: `[versions]`, `[libraries]`, `[plugins]`. No version strings appear in any `build.gradle.kts`.

### Root `build.gradle.kts`

Contains only a `plugins {}` block declaring each plugin via `alias(libs.plugins.…) apply false`:
`android-application`, `kotlin-compose`, `ksp`, `hilt`, `google-services`, `firebase-crashlytics`.

Removed: `ext {}` block (unused hardcoded paths to local signing files), `buildscript {}`, empty `allprojects {}`, custom `clean` task (uses deprecated `buildDir`; Gradle's base `clean` suffices).

### `settings.gradle.kts`

Unchanged in structure (`pluginManagement` + `dependencyResolutionManagement` with `FAIL_ON_PROJECT_REPOS`, `google()`, `mavenCentral()`).

### `app/build.gradle.kts`

- Plugins: `android-application`, `kotlin-compose`, `ksp`, `hilt`, `google-services`, `firebase-crashlytics`. **No** `kotlin-android` / `org.jetbrains.kotlin.android` (AGP 9 built-in Kotlin), **no** `kapt`, **no** Apollo.
- `kotlin { jvmToolchain(21); compilerOptions { jvmTarget = JVM_17 } }`; `compileOptions` source/target `VERSION_17`.
- Removed: `kotlinOptions`, `composeOptions`, `kapt {}`, `apollo {}`, `fileTree("libs")` dependency.
- `packagingOptions` → `packaging`.
- `buildFeatures { viewBinding = true; compose = true }` retained.
- `lint { abortOnError = false; checkReleaseBuilds = false }` retained.
- Release `isMinifyEnabled = true` with existing ProGuard files retained. Debug `multiDexEnabled = true` removed (native multidex from API 21; minSdk 26).

### Dependencies

| Change | Items |
|---|---|
| Remove | Apollo plugin + `apollo-runtime`; `androidx.lifecycle:lifecycle-extensions`; duplicate `core-ktx`; duplicate `lifecycle-viewmodel-ktx` (the pinned `2.2.0` one); `org.jetbrains.kotlin:kotlin-stdlib-jdk8`; `com.android.support:support-annotations`; `com.android.support.test:runner` |
| Add | `platform(libs.androidx.compose.bom)` for `implementation`, `androidTestImplementation`, `debugImplementation`; Compose artifacts without versions |
| Replace | `kapt(hilt-android-compiler)` → `ksp(libs.hilt.compiler)`; `kapt(androidx.hilt:hilt-compiler)` → `ksp(libs.androidx.hilt.compiler)` |
| Keep (upgraded) | core-ktx, appcompat, constraintlayout, material, lifecycle runtime/livedata/viewmodel(-compose), activity-compose, fragment-ktx, preference-ktx, Compose ui/material/ui-tooling(-preview)/animation/runtime-livedata/ui-test-junit4, Firebase Crashlytics + Analytics (via BOM), Hilt, Timber, JUnit, androidx.test ext-junit + espresso |

### `gradle.properties`

| Property | Action | Reason |
|---|---|---|
| `android.enableJetifier=true` | Remove | No `com.android.support` dependencies remain |
| `android.defaults.buildfeatures.buildconfig=true` | Remove | `BuildConfig` is not referenced anywhere |
| `android.nonTransitiveRClass=false` | Remove (adopt default `true`) | Code only references the app's own `R` |
| `android.nonFinalResIds=false` | Remove (adopt default `true`) | No `when`/`switch` on resource IDs |
| `org.gradle.configureondemand=true` | Remove | Legacy; incompatible with configuration cache |
| `org.gradle.configuration-cache=true` | Add | Modern build speedup |
| `org.gradle.jvmargs=-Xmx6g`, `org.gradle.parallel`, `org.gradle.caching`, `android.useAndroidX`, `kotlin.code.style`, `kotlin.incremental`, `org.gradle.tooling.parallel` | Keep | |

If removing `nonTransitiveRClass=false` or `nonFinalResIds=false` produces compile errors, fix the references (qualify library `R` imports) rather than restoring the override.

## Migration sequence

Five steps, each one commit on `modernize-build`. Each step must pass the **step gate** before the next begins.

**Step gate:** `./gradlew clean assembleDebug assembleRelease lint` succeeds; lint reports no errors beyond the Step 1 baseline; the debug app launches to the home screen on an emulator. Steps 2–5 additionally require the manual checklist (below).

### Step 1 — Restructure at current versions

No version numbers change.

1. Create `gradle/libs.versions.toml` with the versions currently in use. Resolve the AGP conflict to **8.4.2**.
2. Convert root and app build files to `plugins { alias(...) }`; delete `buildscript {}`, `ext {}`, `allprojects {}`, custom `clean`.
3. Remove Apollo: plugin, `apollo {}` block, `apollo-runtime` dependency, `app/src/main/java/com/application/ApolloClient.kt`, `app/src/main/graphql/`, and in `AndroidSensorEngine.kt` the `import apolloClient` line and the bare `apolloClient` statement in `onCreate`.
4. Remove the dead dependencies listed above and `android.enableJetifier`.
5. Run lint and save the report as the baseline (`app/lint-baseline.xml` via `lint { baseline = file("lint-baseline.xml") }`).

### Step 2 — Gradle 9.7.1 + AGP 9.4.0

1. `./gradlew wrapper --gradle-version 9.7.1` (regenerates wrapper jar and scripts), then run again so the new wrapper updates itself.
2. AGP → 9.4.0. Remove `kotlin-android` / `org.jetbrains.kotlin.android` plugin (built-in Kotlin). If built-in Kotlin requires a newer Kotlin Gradle plugin than 1.9.25, bump Kotlin to the minimum required version here; Step 3 completes the move to 2.4.20.
3. Migrate DSL removed/deprecated in AGP 9: `packagingOptions` → `packaging`, `kotlinOptions` → `kotlin { compilerOptions }`, and any other deprecation reported by the build.
4. Apply the `gradle.properties` changes from the table above.
5. Install SDK build-tools required by AGP 9.4.0 via `sdkmanager`.

### Step 3 — Kotlin 2.4.20, Compose compiler plugin, KSP, Hilt

1. Kotlin → 2.4.20. Add `org.jetbrains.kotlin.plugin.compose` (version = Kotlin version); delete `composeOptions`.
2. Add KSP 2.3.12; replace both `kapt(...)` with `ksp(...)`; remove `kapt` plugin and `kapt {}` block.
3. Hilt → 2.60.1 (plugin and libraries); `androidx.hilt:hilt-compiler` → 1.4.0.
4. Fix K2 compiler errors (smart-cast, nullability, deprecated-API-now-error).

### Step 4 — Library upgrades

1. Bump all catalog library versions to the target table; switch Compose to the BOM.
2. Replace APIs removed or error-level-deprecated in the new versions. Known: `SettingsActivity.java:41` `onBackPressed()` → `getOnBackPressedDispatcher().onBackPressed()`.
3. Add R8 keep rules to `app/proguard-rules.pro` only if the release build fails or the manual release check crashes.

### Step 5 — SDK 37, minSdk 26, behavior changes

1. Install `platforms;android-37` via `sdkmanager`.
2. compileSdk 37, targetSdk 37, minSdk 26. Remove `multiDexEnabled`.
3. Remove version checks made always-true by minSdk 26 (e.g. `SDK_INT >= N` in `sensors/audio/AudioRecorder.kt`; commented-out O check in `extras/PinShortcuts.kt`). Keep checks for R and S.
4. Apply the behavior-change handling below.

## Android 16 / 17 behavior changes

Sources: developer.android.com behavior-changes pages for Android 16 (API 36) and Android 17 (API 37).

| Change | Impact on this app | Handling |
|---|---|---|
| Edge-to-edge opt-out removed (36) | `BaseSensorActivity` calls `enableEdgeToEdge()`; `HomeScreenActivity` uses legacy `utils/SystemUi.kt` (`WindowCompat.setDecorFitsSystemWindows`, pre-R branch) | Call `enableEdgeToEdge()` in `HomeScreenActivity`; remove the legacy window code from `SystemUi.kt` that it replaces. On API 37, any screen with content under status/nav bars gets `WindowInsets.systemBars` padding in its Compose layout. |
| Predictive back; `onBackPressed()` no longer invoked (36) | No overrides. Only call site is in unregistered `SettingsActivity.java` | Replace call with dispatcher (Step 4). Do not set `enableOnBackInvokedCallback="false"`. |
| Orientation/resizability ignored on ≥600dp; opt-out removed (37) | All 8 sensor activities declare `screenOrientation="portrait"`; they will rotate/resize on tablets and foldables | Keep the attributes (phones honor them). On an API 37 tablet emulator in landscape, each screen must be usable (content visible or scrollable, no crash). Rotation must not leak sensor listeners or the audio recorder. No redesign. |
| Background audio hardening (37) | Sound screen records while visible; recording is stopped in lifecycle callbacks | Manual check: background the app while on the Sound screen; no crash or error log. |
| Local network permission, ECH, CT, CP2, RemoteViews limits, Bluetooth, health permissions, static-final reflection, native DCL | Not used (Apollo removed; no widgets, contacts, Bluetooth, health sensors, reflection on statics, native libs) | None. |

## Verification

**Automated (every step):** step gate above.

**Manual checklist (Steps 2–5):**

1. Fresh install; app launches to home screen.
2. Open each sensor screen: Sound, Light, Pressure, Ambient Temperature, Battery, System, Humidity, Location. Each shows live or "unavailable" data without crashing.
3. Grant microphone permission on Sound; decibel readings update.
4. Grant location permission on Location; coordinates populate.
5. Back navigation from every sensor screen returns to home.
6. Pin a shortcut from the home screen (if the launcher supports it).
7. Background and foreground the app on the Sound and Location screens.
8. Release build (`assembleRelease`, R8 on) installed: repeat items 1–2.

**Emulators:**

- Steps 2–4: API 37 phone.
- Step 5: API 37 phone, API 26 phone, API 37 tablet in landscape (items 1, 2, 5 plus rotation on each sensor screen).

## Risks and rollback

- **Plugin incompatibility with AGP 9.4.0 / Kotlin 2.4.20** — handled by the compatibility fallback rule.
- **R8 stripping Hilt/Firebase classes in release** — caught by manual checklist item 8; fixed with keep rules.
- **K2 compiler errors** — expected to be small (≈7.5k lines Kotlin); fixed in Step 3.
- **Configuration cache incompatibility with a plugin** — if a plugin fails with configuration cache, set `org.gradle.configuration-cache.problems=warn` and record the offending plugin in a comment; do not disable the cache.
- **Rollback** — each step is an isolated commit; `git revert <step commit>` restores the prior working state.
- **Not touched** — signing keys, `app/google-services.json`, `app/release/`, uncommitted `.idea/` changes (never staged in these commits).
