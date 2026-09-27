# Architecture

## Decisions that constrain the build

| Topic | Choice | Why |
| --- | --- | --- |
| Language and UI | Kotlin, Jetpack Compose, Material 3 | Requested native stack. |
| minSdk | 26 | Stable Health Connect client floor and `java.time` without desugaring. API 26–27 do not have the Health Connect provider; the UI must say so. |
| compileSdk | 37 | Required by Jetpack Compose 1.12. |
| targetSdk | 36 | Play requirement for new apps and updates as of 31 August 2026 (Android 16). Targeting 37 would opt into newer runtime behavior that Play does not yet require. |
| AGP / Gradle / Kotlin | 9.4.0 / 9.6 / 2.4.10 | Current stable AGP. Built-in Kotlin is left enabled. The Kotlin Android plugin is not applied. |
| Local store | Room 3 | Offline source of truth. |
| Preferences | DataStore | Theme, reminder opt-in, disclaimer, onboarding flag. |
| Steps | Health Connect `connect-client` 1.1.0 | Official fitness store. No always-on step sensor service. |
| Camera | CameraX 1.5 + ML Kit barcode scanning | On-device barcodes. Images for recognition are not saved when the provider is unconfigured. |
| Billing | Play Billing Library 9.1.0 | Current library. Entitlement requires `Purchase.PurchaseState.PURCHASED` and a known product id. |
| Backend | Not deployed | Avoids cost and a secret-in-APK failure mode. Interfaces exist. |
| DI | Hilt 2.60.1 + KSP 2.3.12 | Constructor injection at the edges. |
| Charts | Small Compose canvas | No third-party chart license, and the same numbers are rendered as text. |

## Modules

```
:core:model        JVM domain types
:core:domain       Engines, repository interfaces, scan/session/sync rules
:core:network      Retrofit contract used only when VELORA_API_BASE_URL is set
:core:database     Room, DataStore, USDA subset seed, repository implementations
:core:designsystem Compose theme and components
:core:analytics    AnalyticsTracker
:core:health       Health Connect reader
:feature:onboarding
:feature:dashboard
:feature:nutrition Food log, recipes, meals, barcode, recognition UI
:feature:activity  Steps and manual activity
:feature:workout
:feature:metrics   Weight, measurements, goals, progress
:feature:profile   Settings, reminders, export, delete, billing
:app               Navigation, Hilt graph, manifest, workers
```

Feature modules depend on design system, domain, and Hilt. They do not depend on each other. `:app` is the composition root.

`:core:model` and `:core:domain` are plain JVM so calorie, meal, step, workout, sync, and scan rules run as unit tests without an emulator.

## Layers

- UI: Compose screens. They render `UiState` and send events. They do not compute TDEE, meal sums, or entitlements.
- ViewModel: loads repositories, exposes `StateFlow`.
- Domain: pure functions and repository interfaces.
- Data: Room and Health Connect implement those interfaces.

## Offline and sync

Room rows are authoritative. Mutations also insert a `sync_outbox` row. `SyncCoordinator` asks `SyncTransport`:

- `NotConfigured` leaves the outbox intact and does not pretend the server has the data.
- `Failure(retryable)` keeps the row and increments attempts. WorkManager retries with a network constraint.
- `Success` deletes accepted outbox ids. Push is idempotent on client id.

Conflicts, when two copies exist, use `ConflictResolver`: a server revision beats a client clock; otherwise the later `updatedAt` wins; equal timestamps prefer a deletion so a delete is not resurrected. The future server must assign the revision. Client clocks are not "corrected" on device.

## Food data

`FoodRepository` searches Room. The database is seeded from `usda_foundation_subset.json`. The asset currently contains one verified USDA SR Legacy record, bananas raw (FDC 173944), per 100 g. More records may be added only from a real FoodData Central response. The full USDA catalog is not in the APK. Live search and barcode lookup against USDA go through `RemoteFoodDataSource`, which is unconfigured until `VELORA_API_BASE_URL` points at a backend that holds the data.gov key.

Provenance is an enum on every nutrient bundle. Aggregation promotes the least-certain source: estimated over user-entered over verified.

## Recognition

```kotlin
interface FoodRecognitionService {
    suspend fun recognize(image: ByteArray): FoodRecognitionResult
}
```

`UnconfiguredFoodRecognitionService` returns `Unavailable` and the UI discards the bytes. A later provider must still return candidates, not a committed log. `FoodScanCoordinator` encodes that rule and is unit tested.

## Steps and activity

`StepRepository` merges Health Connect daily totals with manual entries. Manual and Health Connect values for the same day are not added together; the Health Connect value wins when present, and the screen says which source was used. If they were summed, a person who typed a guess and also granted Health Connect would double count.

`ActivityRepository` stores user-entered sessions locally. Writing them into Health Connect is off unless the user enables that export. The app does not request step-write permission.

## Billing

`PremiumEntitlementMapper` accepts a list of store purchases. Premium requires product id `velora_premium_monthly`, state purchased, and a non-blank token. Pending, unspecified, and empty lists are not premium. The mapper has no boolean override. Cloud and recognition bindings ignore this flag.

## Analytics

`AnalyticsTracker` methods take an event name and optional non-health dimensions (`source=barcode`, `slot=lunch`). `ReleaseAnalyticsTracker` drops parameters entirely and does not send events to a vendor. Debug builds log the event name only.

## Navigation

Bottom bar: Home, Food, Move, Progress, Profile. Move hosts steps and workouts. Six equal tabs on a 360dp phone fall below a comfortable target; this is recorded in the product requirements.

## Logging

`VeloraLog` in debug prints tag and message. Release printing is disabled. Call sites must not pass food names, weights, emails, or tokens. The logger has no extra redaction engine because the safe design is to never pass those values.
