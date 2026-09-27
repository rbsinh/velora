# Testing

## What is automated

JVM tests in `:core:domain` cover:

- Mifflin-St Jeor BMR, TDEE, goal adjustment, safety floor, unspecified sex, and overrides.
- Macro split and rounding.
- Meal nutrient sums and provenance promotion.
- Null micronutrients staying null.
- Step day, week, and month aggregation, including Health Connect winning over a manual value for the same day.
- Weight trend requiring enough points.
- Workout session start, pause, resume, and complete using a fixed clock.
- Calorie and step goal adherence, including an empty food log not counting as "on plan".
- Sync conflict cases: server revision, client clock, equal-timestamp deletion, idempotent ids.
- Premium mapping: purchased, pending, blank token, unknown product.
- Food-scan coordinator: unconfigured provider discards the image and does not log; candidates do not log until a portion is confirmed.

`:core:network` uses MockWebServer for search success, 404 barcode, 500, and a recognition payload that is missing confidence.

`:core:database` tests the USDA catalog parser against the bundled banana record and rejects rows that omit required macros. Room migrations are not covered. Repository behavior beyond the parser is exercised through the domain fakes, not through SQLite.

## Commands

```bash
./gradlew test
./gradlew :app:assembleDebug
```

Release bundle, when signing environment variables are present:

```bash
./gradlew :app:bundleRelease
```

## Result on 27 September 2026

`./gradlew test :app:assembleDebug` completed successfully.

| Module | Tests | Failures |
| --- | --- | --- |
| `:core:domain` | 26 | 0 |
| `:core:network` | 5 | 0 |
| `:core:database` | 2 | 0 |

Debug APK: `:app:assembleDebug`.

## What this environment did not run

The machine used to create the project did not have an Android emulator or a physical device. These were not executed:

- `connectedAndroidTest`
- TalkBack traversal on a device
- Camera barcode against a real preview
- Health Connect permission UI
- Play Billing purchase flow (needs a Play-signed build and license testers)
- Startup macrobenchmark

`app/src/androidTest/kotlin/app/velora/track/EmptyStateTest.kt` checks that an empty-state title and body are exposed as text. `./gradlew :app:assembleDebugAndroidTest` compiled that test APK. It was not installed or executed, because there is no emulator or device.

## Risk-based gaps to close before production

- Room migration test once schema version 2 exists.
- End-to-end sync test against a staging server once one exists.
- Billing library integration test with Play's test card.
- Screenshot or accessibility-scanner pass on a phone and a small display.
