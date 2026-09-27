# HTTP API contract

This contract is what `:core:network` calls when `VELORA_API_BASE_URL` is non-empty. Nothing in the repository serves these routes. An empty base URL binds an unconfigured client that fails with `RemoteUnavailable` and does not open a socket.

All routes require `Authorization: Bearer <access token>` except a future token exchange. The MVP client does not attach a bearer token because accounts are not implemented; the configured client will receive 401 and surface it as an auth error rather than retry forever.

## `GET /v1/foods/search?q={query}`

200:

```json
{
  "foods": [
    {
      "id": "usda:173944",
      "name": "Bananas, raw",
      "provenance": "VERIFIED_REFERENCE",
      "sourceName": "USDA FoodData Central",
      "sourceRecordId": "173944",
      "servings": [
        {
          "label": "100 g",
          "grams": 100,
          "caloriesKcal": 89,
          "proteinGrams": 1.09,
          "carbohydrateGrams": 22.8,
          "fatGrams": 0.33,
          "fiberGrams": 2.6,
          "sugarGrams": 12.2,
          "sodiumMilligrams": 1
        }
      ]
    }
  ]
}
```

Nullable micronutrients are JSON null when the upstream record has no value. The client rejects a food that omits calories, protein, carbohydrate, or fat.

400 empty query. 429 rate limited. 5xx retryable.

## `GET /v1/foods/barcode/{code}`

200 uses the same food object. 404 means unknown, which is not a crash. 409 with a `foods` array means multiple matches; the user chooses.

## `POST /v1/sync`

```json
{
  "mutations": [
    {
      "clientId": "uuid",
      "entity": "food_log_entry",
      "operation": "UPSERT",
      "payload": {},
      "clientUpdatedAtEpochMillis": 0
    }
  ]
}
```

200:

```json
{
  "accepted": [
    { "clientId": "uuid", "serverRevision": 10 }
  ]
}
```

The client deletes outbox rows only for accepted ids. Unknown ids stay queued. 409 is a conflict response with the server copy; `ConflictResolver` applies.

## `POST /v1/ai/food-recognition`

Multipart image, maximum 4 MB. The client must not call this until the user confirms upload.

200:

```json
{
  "candidates": [
    { "label": "Chicken biryani", "confidence": 0.42, "foodId": null }
  ]
}
```

`foodId` null means the label is a guess without verified nutrients. The client must not attach invented macros. The user can pick the label and enter nutrients, or match a local food themselves.

`confidence` is required. A body without it is an invalid response.

503 means the provider is down. The UI shows retry. It does not log a guess.

## `POST /v1/billing/play/verify`

```json
{ "productId": "velora_premium_monthly", "purchaseToken": "<from Play>" }
```

The server calls Google Play Developer API. Response is `{ "active": true }` or false. The Android app does not implement this call for gating local charts; those use the Billing library directly. This route is for future server-side resources.

## `DELETE /v1/me`

204 after the account is deleted. The client then runs the same local wipe as Profile → Delete.

## Errors

```json
{ "code": "unavailable", "message": "short human text" }
```

The client maps transport failures, timeouts, 401, 404, 409, 429, and 5xx onto `RemoteError`. It does not throw through Compose.
