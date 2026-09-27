# Infrastructure

## MVP (now)

```
Android app
  Room + DataStore on the device
  Health Connect, when the device has it
  Play Billing, when the user buys a subscription
```

No Cloud Run service, no database host, no Kubernetes, no Firebase project. Those would cost money and expand privacy scope before the diary works offline.

`VELORA_API_BASE_URL` empty is the default. Release signing values come from the environment, not from the repo.

## Later, if sync or recognition is turned on

Prefer one Cloud Run service and one managed Postgres (Cloud SQL) in a single region close to the developer, with a public HTTPS endpoint and no public database IP.

Supabase is the alternative if the operator wants hosted Postgres and auth without writing a token service. Either choice is one deployable, not a microservice mesh.

Rejected for this product stage:

- GKE or any Kubernetes layout.
- A separate service per feature.
- Shipping the USDA or model key inside the app "just for development".

## Environments

| | Dev | Staging | Production |
| --- | --- | --- | --- |
| App id suffix | `.debug` on the debug build type | `app.velora.track.staging` when a staging flavor is added | `app.velora.track` |
| API | localhost or a dev Cloud Run URL via `VELORA_API_BASE_URL` | staging URL | production URL |
| Play Billing | license testers, staging product ids | license testers | production subscription |
| Logs | verbose local logcat | server logs without bodies | minimal |
| Database | none, or a disposable dev instance | staging Postgres | production Postgres with backups |

The debug build type adds `.debug` to the application id so it can be installed beside a release build.

## Configuration

- `local.properties` holds only `sdk.dir` and is gitignored.
- API base URL: environment variable `VELORA_API_BASE_URL` copied into `BuildConfig` at build time. Empty string if unset.
- Signing: `VELORA_UPLOAD_STORE_FILE`, `VELORA_UPLOAD_STORE_PASSWORD`, `VELORA_UPLOAD_KEY_ALIAS`, `VELORA_UPLOAD_KEY_PASSWORD`. If they are absent, `assembleRelease` still configures a release build but signing is skipped with a Gradle warning rather than embedding a debug key as if it were production.

## Promotion path

1. Develop against Room only.
2. `./gradlew test` and `./gradlew :app:assembleDebug`.
3. When a backend exists, point a staging build at staging and run sync tests against it.
4. Play Console internal testing track with a signed AAB.
5. Production after the Play checklist, including a public privacy-policy URL.

## Secrets

GitHub Actions (or the operator's CI) should store signing and future server secrets in the CI secret store. They are not files in this repository.
