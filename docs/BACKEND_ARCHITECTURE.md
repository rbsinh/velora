# Backend architecture

No backend is deployed for the MVP. The Android app must not contain a USDA key, a model-provider key, or a Play Developer API service account.

When a backend is justified (cloud sync, remote food search, or recognition), run one small HTTPS service. Do not start with Kubernetes or a fleet of microservices.

## Recommended shape

A single Cloud Run service (or one Supabase project, if SQL and auth are preferred over writing an auth server) plus Postgres.

| Concern | Choice | Reason |
| --- | --- | --- |
| Cost | Cloud Run scale-to-zero, or Supabase free/low tier | No always-on cluster. |
| Android client | HTTPS JSON only | No vendor SDK required in the app, so the data-sharing surface stays small. |
| Auth | OIDC (Google or email magic link) issuing a short-lived bearer token | The app stores the refresh token in the Android Keystore-backed EncryptedDataStore only after accounts exist. |
| Database | Postgres | Relational diary maps cleanly. Row-level security if Supabase is used. |
| Object storage | Private bucket for recognition uploads, lifecycle-deleted within 24 hours | Photos are not a permanent health archive unless the user opts in later. |
| AI provider | Called only from the service | The provider key lives in the server secret manager. |

Firebase was not added to the client. It is convenient and also easy to over-collect. A first-party HTTPS API is easier to audit against `PRIVACY.md`.

## API

Base path `/v1`. JSON. TLS only. See `API.md` for the contract the Retrofit client already implements.

- `POST /v1/sync` body is the outbox batch. Response lists accepted client ids and server revisions.
- `GET /v1/foods/search?q=` proxies USDA FoodData Central. The service caches public reference foods. It does not log the query next to a user id at info level; search terms can reveal health conditions.
- `GET /v1/foods/barcode/{code}` returns 404 when unknown. The client offers manual creation.
- `POST /v1/ai/food-recognition` accepts an image only after the user confirms upload. Response is candidates with confidence. Nutrients are omitted unless the server matched a verified food id. The client still requires the user to pick a candidate.
- `DELETE /v1/me` deletes the account and stored rows, and queues object deletion.
- `POST /v1/billing/play/verify` sends a purchase token. The server calls the Google Play Developer API. The client's premium flag is never accepted as proof.

## Authentication

- Authorization code or Play-services one-tap, exchanged by the server.
- Access tokens last minutes. Refresh tokens rotate.
- Each diary table has `user_id`. Queries are scoped. A missing scope is a bug, not a default to "all users".

## Database

- Postgres, daily logical backups, point-in-time recovery once the store holds real diaries.
- Client ids are UUID primary keys so retries do not duplicate.
- `server_revision` is a monotonic bigint assigned by the database.

## Storage

Recognition images live in a private bucket with a 24-hour lifecycle rule. The diary itself stays in Postgres, not in object storage.

## API security

- TLS 1.2+.
- Authorization header required on every diary route.
- Rate limit per user and per IP on search and recognition (for example 30 recognition calls per user per day, 60 searches per minute). Return 429. The client surfaces retry.
- Request size cap on images (4 MB).
- No stack traces in response bodies.
- CORS is irrelevant to the Android client and should be deny-by-default if a browser client does not exist.

## Logging and monitoring

- Structured logs: request id, route, status, latency. No body, no barcode, no image, no Authorization header.
- Metrics: request count, 5xx rate, recognition latency, sync conflict count.
- Alerts on error rate and backup failure.
- Traces may include route names, not payloads.

## Backup and disaster recovery

- Automated daily backups, retention 30 days, restore tested before production diaries exist.
- Object storage versioning off for recognition images so deletion is real.
- RPO target for a future production launch: 24 hours. RTO: one business day. These are starter targets, not a medical-device claim.

## Environments

Dev, staging, and production are separate projects, databases, and Play billing products (license testers on staging). The app selects a base URL from the environment at build time. An empty URL means the remote implementations are not bound.
