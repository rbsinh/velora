# Velora

Velora is an offline-first Android app for food logging, estimated calorie targets, steps, workouts, weight, and progress. It is an original product. It does not use another tracker’s name, branding, or data.

Calorie and macro targets are estimates from the Mifflin–St Jeor equation. They are not medical advice. The app does not diagnose disease and does not invent nutrient values.

## What it does

- Onboarding collects age, sex, height, weight, goal, activity, units, and a step goal. The estimate disclaimer is required.
- Food log for breakfast, lunch, dinner, and snacks, with search, custom foods, recipes, saved meals, and repeat-yesterday.
- Nutrition totals for calories, protein, carbohydrate, fat, and optional fiber, sugar, and sodium. Missing micronutrients stay unknown.
- Each nutrient bundle is labeled verified reference, user-entered, or estimated. The least certain source wins when values are summed.
- Barcode scan with CameraX and ML Kit. An unknown code can become a user-entered food. The scanned code is passed into that form.
- Photo recognition goes through `FoodRecognitionService`. Nothing is logged until the person confirms a match and a portion. With no backend configured, the photo is discarded.
- Steps from Health Connect when the device and permission allow it. Manual totals are labeled manual and do not overwrite Health Connect unless the person chooses that override. Distance and active calories are shown only when Health Connect returns them.
- Workouts with a small original exercise library, sets, a rest timer that lives only as long as the process, history, and personal records.
- Weight, waist, informational BMI, and on-device progress photos.
- Home dashboard, progress text and bars, optional reminders, export of the food log and weights, and local data deletion.
- Play Billing can record a subscription entitlement on the device. Cloud sync and AI stay off until a server verifies the purchase token. A local flag does not unlock those services.

## Architecture

```
:app
:feature:onboarding :feature:dashboard :feature:nutrition :feature:activity
:feature:workout :feature:metrics :feature:profile
:core:designsystem :core:analytics :core:health :core:database :core:network
:core:domain :core:model
```

Compose screens render state and send events. ViewModels talk to repository interfaces. Calorie, meal, step, workout, sync, and scan rules live in `:core:domain` and are unit tested on the JVM. Room is the source of truth. Health Connect is read for steps, distance, and active calories. See `docs/ARCHITECTURE.md`.

## Requirements

- JDK 17 for compilation. The Gradle JVM modules request toolchain 17. This machine also has Temurin 21 for the Gradle daemon.
- Android SDK with compile SDK 37, build-tools 36.0.0, and platform-tools.
- `local.properties` with `sdk.dir` pointing at the SDK. That file is gitignored.
- If `sdkmanager` installs `platforms;android-37.0` and Android Gradle Plugin looks for `platforms/android-37`, link `platforms/android-37` to `platforms/android-37.0`.

minSdk is 26. targetSdk is 36. compileSdk is 37. Health Connect’s provider is not on API 26–27; those devices use manual entry.

## Setup

```bash
export JAVA_HOME="$HOME/.local/jdks/current"   # JDK 21 is fine for the daemon
export ANDROID_HOME="$HOME/.local/android-sdk"
```

Gradle must be able to see a JDK 17 installation. On a machine that keeps JDKs outside the default search path, set `org.gradle.java.installations.paths` in the user Gradle properties, not in the project file.

If the network uses a private certificate authority, point the JVM at a trust store before resolving dependencies:

```bash
export JAVA_TOOL_OPTIONS="-Djavax.net.ssl.trustStore=$HOME/.local/jdks/velora-cacerts -Djavax.net.ssl.trustStorePassword=changeit"
```

Do not commit that trust store.

## Build and test

```bash
./gradlew test
./gradlew :app:assembleDebug
```

`./gradlew test` and `:app:assembleDebug` succeeded on 27 September 2026. JVM tests: 26 in `:core:domain`, 5 in `:core:network`, 2 in `:core:database`. Failures: 0.

Instrumented tests compile with:

```bash
./gradlew :app:assembleDebugAndroidTest
```

`connectedAndroidTest` was not run. This environment has no emulator and no device, so camera preview, Health Connect permission UI, Play purchase, TalkBack, and the Compose test were not executed on a phone.

## Environment variables

| Variable | Purpose |
| --- | --- |
| `VELORA_API_BASE_URL` | Optional. Empty binds the unconfigured food and recognition clients. No API secret belongs in the APK. |
| `VELORA_UPLOAD_STORE_FILE` | Release keystore path. Release signing is skipped when this is unset. |
| `VELORA_UPLOAD_STORE_PASSWORD` | Keystore password. |
| `VELORA_UPLOAD_KEY_ALIAS` | Key alias. |
| `VELORA_UPLOAD_KEY_PASSWORD` | Key password. |

## Backend

None is deployed. Room, DataStore, and Health Connect cover the MVP. A future API is described in `docs/BACKEND_ARCHITECTURE.md` and `docs/API.md`. USDA FoodData Central and any recognition provider must be called from that server so keys stay off the device.

The bundled catalog is one verified USDA SR Legacy record: bananas, raw, FDC 173944, per 100 g. Other foods are user-entered. Do not add nutrient numbers that were not returned by a licensed source.

## Release

```bash
./gradlew :app:bundleRelease
```

The release build minifies and shrinks resources. It is signed only when the upload keystore variables are set. Play checklist: `docs/PLAY_STORE_CHECKLIST.md`.

## Documentation

- `docs/PRODUCT_REQUIREMENTS.md`
- `docs/ARCHITECTURE.md`
- `docs/BACKEND_ARCHITECTURE.md`
- `docs/INFRASTRUCTURE.md`
- `docs/DATABASE.md`
- `docs/SECURITY.md`
- `docs/PRIVACY.md`
- `docs/PLAY_STORE_CHECKLIST.md`
- `docs/API.md`
- `docs/TESTING.md`
