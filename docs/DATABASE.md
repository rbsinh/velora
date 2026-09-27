# Database

Room is the source of truth. Schema version 1. Entities use string UUID primary keys so a future sync push is idempotent. User-owned rows have `deleted` and `updatedAtEpochMillis`.

Dates are ISO-8601 local dates (`YYYY-MM-DD`). Instants are epoch milliseconds. The device zone is applied when grouping days. The stored date is the user's local date at entry time, not a server date.

## Tables

| Table | Purpose |
| --- | --- |
| `user_profile` | Single row of personalization and targets. |
| `food` | Reference foods, user foods, and barcode products. |
| `food_serving` | One or more servings. Reference foods use 100 g. |
| `food_log_entry` | A logged item. Nutrients are the snapshot times quantity, so later edits to the food do not rewrite history. |
| `recipe` / `recipe_item` | User recipes. Item nutrients are snapshots. |
| `meal_template` / `meal_template_item` | Saved meals that can be logged again. |
| `weight_entry` | Body weight in kilograms. |
| `body_measurement` | Named circumference in centimeters. |
| `progress_photo` | Internal-file path. Not backed up. |
| `exercise` | Original exercise library plus user exercises. |
| `workout_template` / `workout_template_exercise` | A plan. |
| `workout_session` / `workout_set` | A performed workout. |
| `activity_entry` | Manual walk, run, cycle, workout, or general activity. |
| `step_day` | Daily step total and source (`HEALTH_CONNECT` or `MANUAL`). |
| `water_day` | Glasses of about 250 ml. A habit counter, not a fluid prescription. |
| `sync_outbox` | Pending mutations. |

## Nutrient columns

Calories (kcal), protein, carbohydrate, and fat are required for a food the user can log. Fiber, sugar, and sodium are nullable. Null means the source did not provide a value. Queries must not coalesce those to zero.

`provenance` is `VERIFIED_REFERENCE`, `USER_ENTERED`, or `ESTIMATED`.

## Indexes

- `food_log_entry(local_date)`
- `food(name)`
- `food(barcode)`
- `step_day(local_date)`
- `weight_entry(local_date)`
- `sync_outbox(createdAtEpochMillis)`

## Seeding

On first open, `CatalogSeeder` inserts `assets/usda_foundation_subset.json` if those FDC ids are absent. It never overwrites a user-edited row. User foods have a null `fdcId`.

The file is a subset retrieved from USDA FoodData Central (U.S. government work, public domain). It currently holds one verified record: bananas, raw, FDC 173944, per 100 g. Each record stores `fdcId`, description, data type, and per-100 g nutrients that were present in the response. It is not the USDA database, and it is not a license to scrape other nutrition vendors.

## Migrations

Version 1 has no migration. The next schema change needs a `Migration` and a schema JSON file under `core/database/schemas`. Destructive migration is not enabled.

## Backup

`data_extraction_rules.xml` excludes the database, shared preferences, and the photos directory from cloud backup and device transfer. The supported copy path is the in-app export.

## DataStore

`velora_settings` holds theme, onboarding complete, reminder hours, Health Connect export opt-in, and the estimate disclaimer acknowledgement. It does not hold the food diary.
