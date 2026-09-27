# Privacy

Velora is an adult app. It is not directed at children. There is no advertising SDK.

## Data collected

| Data | Why | Where | Retention |
| --- | --- | --- | --- |
| Age, sex, height, weight, goal, activity level, units, step goal, dietary note | Calorie estimate and display units | Room on device | Until the user deletes data or uninstalls |
| Food diary, recipes, meals, water count | The user asked to track them | Room on device | Same |
| Workouts, exercises, activities | The user asked to track them | Room on device | Same |
| Steps, distance, active calories | Shown if Health Connect grants them | Read from Health Connect; a daily step total is cached in Room | Cache until deleted |
| Weight and measurements | Progress | Room | Until deleted |
| Progress photos | Optional, user-picked | App internal storage | Until deleted |
| Reminder settings and theme | Preferences | DataStore | Until deleted |
| Play purchase token | Entitlement on device; later server verification | Play Billing library storage | Managed by Play |
| Analytics event names | Product diagnostics, debug only today | Not shipped to a vendor | Not retained by us |

Not collected: precise location, contacts, advertising id, food photos (unless a future recognition upload is confirmed), passwords (there is no account).

## Third parties

| Party | Role in MVP |
| --- | --- |
| Google Play Billing | Subscription purchase, if the user buys one |
| Health Connect | Optional fitness and weight store on the device |
| Google ML Kit barcode | On-device barcode detection. Images are processed for codes and are not uploaded by this app for that feature |
| USDA FoodData Central | Source of the small bundled reference subset. No live call from the app |
| Analytics or ads vendors | None integrated |

A future recognition provider and a future sync server are described in `BACKEND_ARCHITECTURE.md`. They are not contacted when the API base URL is empty.

## AI processing

The recognition interface can return candidates. The shipped implementation returns "not configured" and drops the image. The UI must not describe that as an analysis. When a provider is added, the app will explain that the photo leaves the device and will wait for confirmation. Candidates are not logged until the user selects one and a portion.

## Analytics

Event names may be counted later. Parameters must not include weight, calories, nutrients, food names, barcodes, photos, step counts, or free-text dietary notes. The release tracker currently discards events rather than send them somewhere undisclosed.

## Advertising

None.

## Export

Profile → Export writes a JSON document the user chooses through the system document UI. The file contains the diary because that is the point of export. It is the user's copy.

## Deletion

Profile → Delete all local data wipes Room, DataStore, and progress photos. There is no account to delete yet. `DELETE /v1/me` is specified for the future server. Uninstalling the app removes app-specific storage. Health Connect records the user wrote (only if they enabled export) are deleted by the user in Health Connect; Velora does not silently purge another app's store.

## Policy URL

The Play listing needs a public privacy-policy URL before production release. The in-app policy screen repeats this summary and leaves a placeholder URL in `PLAY_STORE_CHECKLIST.md` until that page is hosted.
