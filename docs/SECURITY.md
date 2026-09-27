# Security

Health and fitness content is sensitive personal data. The MVP threat model is a lost or stolen unlocked phone, a curious log pipeline, and a future backend that must not trust the app.

## On device

- The diary is in the app-specific database. Android file-based encryption applies when the user has a device lock. That is the MVP encryption-at-rest control.
- SQLCipher was not added. It raises backup, performance, and key-management complexity before there is an account. Revisit if threat modeling shows the platform encryption is not enough (for example, if backups are later enabled).
- Progress photos live in internal storage and are excluded from backup.
- No account token exists yet. When one does, store it with Jetpack Security (`EncryptedSharedPreferences` / a Keystore-backed key), not in a world-readable file and not in logs.
- Web traffic uses HTTPS. Cleartext is disabled. Certificate pinning is intentionally not half-configured; pin only after the production host and backup pins exist.
- The release build enables R8.

## Secrets

These must never be committed or placed in `BuildConfig` as production credentials:

- USDA FoodData Central key
- Model-provider key
- Play Developer API service account
- Upload keystore passwords (CI secrets only)

`BuildConfig.API_BASE_URL` is a URL, not a secret. Empty means no network client is installed in the graph.

## Billing

Granting premium from a hardcoded boolean is treated as a defect. `PremiumEntitlementMapper` requires a purchased Play subscription for the known product id. A modified client can still lie about local UI gates on a rooted device. That is accepted only because premium currently hides extra on-device charts, not someone else's data. Any future cloud or AI route must verify the purchase token on the server with the Play Developer API.

## Logging

`VeloraLog` is a no-op in release. Debug logging is for engineering messages. Do not pass diary fields into it. Crash reports are not wired. If they are added later, they need a scrubber and a privacy-policy disclosure first.

## Permissions

Requested only when the feature is used:

- Health Connect read permissions for steps, distance, and active calories.
- Health Connect write for weight and exercise only if the user turns export on.
- Camera for barcode and food photos.
- Notifications for reminders the user enabled.

Not requested: location, activity recognition (no direct step sensor), read-media-images (photo picker instead), SMS, contacts, microphone.

## Sync integrity

Outbox retries are idempotent on client id. The conflict rule prefers server revision, then client time, then deletion. The server must ignore client-supplied user ids that do not match the token.
