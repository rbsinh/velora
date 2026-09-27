# Velora product requirements

Velora is an original adult health-tracking app for food, movement, workouts, and body metrics. It is not affiliated with Cal AI, Step Counter, MyFitnessPal, or any other product. Public product patterns (a daily calorie summary, meal slots, a step goal, workout logging) are category conventions. Visual design, copy, data model, and implementation are original.

Velora is for adults 18 and older. It does not provide medical advice, diagnosis, or treatment.

## Product vision

Help someone see, in one place, what they ate, how they moved, and how their body metrics are changing, with estimates clearly labeled as estimates and with their diary stored on the device first.

## Target users

- Adults who want a single place for food, steps, workouts, and weight.
- People who need the diary to work on a plane, in a gym basement, or on a metered connection.
- People who will not accept a photo of a meal being logged without their confirmation.

Not targeted: children, clinical patients seeking medical nutrition therapy, or elite-sport laboratories.

## Problems solved

- Food logging is fragmented from step and workout history.
- Calorie apps often present formulas as facts.
- Photo logging that auto-commits an uncertain meal is untrustworthy.
- Fitness apps that require an account before a single meal can be logged fail offline.

## MVP scope

Included:

- Onboarding that collects only personalization fields, editable later.
- Local food log: search a small USDA-attributed reference set, custom foods, recipes, saved meals, repeat a previous slot, breakfast/lunch/dinner/snacks, edit and delete.
- Daily calories, remaining calories, protein, carbohydrate, fat, and fiber/sugar/sodium when the source provided them.
- Provenance on every nutrient value: verified reference, user-entered, or estimated.
- Barcode scan that looks up a local product and, when a backend is configured, a remote catalog. Unknown codes become a user-created product.
- AI food recognition behind `FoodRecognitionService`. The MVP provider is explicitly unconfigured: it does not invent foods and does not upload the photo.
- Step history from Health Connect when that provider is available, plus manual steps labeled as manual.
- Manual activity entry (walk, run, cycle, workout, general).
- Workout templates, sessions, sets, reps, load, duration, rest timer, history, personal records.
- Weight, waist and other measurements, optional local progress photos, BMI shown only as a ratio.
- Goal and calorie-target engine with a documented formula, safety floor, and manual overrides.
- Dashboard, weekly and monthly summaries, charts with numeric values.
- Optional once-daily reminders, off until the user enables them.
- Export and deletion of local data.
- Play Billing subscription client for future premium on-device features. No fake unlock.

## Post-MVP scope

- Account-based cloud sync through a backend the project controls.
- USDA FoodData Central search proxied by that backend (the API key never ships in the APK).
- A real food-recognition provider behind the same interface, still requiring confirmation.
- Server-verified Play purchase tokens before any cloud or AI quota is granted.
- Wear companion, widgets, and shared meal plans.

## Premium features

Free:

- Food log, custom foods, recipes, meals, barcode lookup against on-device data.
- Steps, manual activity, basic workouts, weight, basic charts, reminders, export.

Premium, only after Play Billing reports an owned subscription:

- Longer progress history views and extra chart ranges on the device.

Not premium-gated, and not silently enabled by a local flag:

- Cloud sync and AI recognition. Those stay off until a server exists that verifies purchases with Google. Shipping a client boolean would be treated as an entitlement bug.

## Monetization

- Optional auto-renewing subscription sold with Google Play Billing.
- No ads in the MVP, so there is no advertising SDK and no ad-related data sharing.
- The free tier must remain usable for daily logging.

## User journeys

1. First launch: age gate (18+), body metrics, goal, activity, units, step goal, estimate disclaimer, then the dashboard.
2. Log lunch: search or custom food, pick a serving, set quantity, save. Dashboard remaining calories update offline.
3. Repeat breakfast: copy yesterday's breakfast into today as new entries.
4. Scan a barcode: known local product opens nutrition; unknown code offers "Create product" with empty nutrients the user must fill. The app does not invent a label.
5. Photograph food: preview stays on device. Analyze either explains that no provider is configured and discards the image, or, later, asks permission to upload and then lists possible matches the user must choose.
6. Steps: if Health Connect is available, request only step, distance, and active-calorie reads. If it is not, show that automatic steps are unavailable and accept a manual total.
7. Start a workout: select a template, log sets, pause, finish. History and personal records update on device.
8. Weigh in: add weight, see trend only when there is enough history.
9. Delete everything: wipe the local database, preferences, and progress photos.

## Functional requirements

- FR1. Onboarding and profile editing support age, sex, height, weight, goal, optional target weight, activity level, workout days per week, a short dietary preference note, units, and a daily step goal.
- FR2. Sex includes female, male, and unspecified. The unspecified path uses a documented average of the two Mifflin-St Jeor constants and says so.
- FR3. Calorie, BMR, and TDEE outputs are labeled estimates.
- FR4. Users can override calorie and macro targets. Overrides below the safety floor require an explicit warning state.
- FR5. Food search covers the local reference catalog and user foods. Results show provenance.
- FR6. Missing micronutrients render as "not provided", never as zero.
- FR7. Recipes and meals recompute from ingredient snapshots.
- FR8. Logging a recognition candidate requires an explicit user choice and a portion.
- FR9. Barcode failures (unknown, offline, invalid, multiple) have their own states.
- FR10. Step totals support day, week, and month ranges and name the source.
- FR11. Workouts support library, custom templates, start, pause, complete, sets, rest, history, and personal records.
- FR12. Weight history shows goal, weekly change, and monthly change without a diagnosis.
- FR13. Reminders are per category and default off.
- FR14. Core logs work with airplane mode on.
- FR15. The user can export JSON and delete local data.

## Non-functional requirements

- Cold start should not block on the network. There is no mandatory network call on launch.
- The UI uses Material 3, supports light and dark themes, and keeps primary actions inside thumb reach.
- Lists that back the dashboard are Room flows, not polling.
- Background work is WorkManager, not a sticky foreground service.
- Release logs must not contain diary contents, tokens, or health values.

## Privacy requirements

See `PRIVACY.md`. Summary: minimize collection, keep the diary on device, no advertising ID, no precise location, photos discarded unless the user is about to send them to a configured recognizer, and deletion must be local and complete.

## Security requirements

See `SECURITY.md`. Summary: platform encryption at rest, HTTPS only when a backend is configured, no secrets in the APK, Play purchases are not a server credential, and health data is not logged.

## Accessibility requirements

- TalkBack labels on icon buttons and charts.
- Charts expose the same numbers as text.
- Touch targets at least 48dp.
- Text uses `sp`. Contrast targets WCAG AA for body text and controls.
- Animations respect the system animator duration scale.
- The five-destination bar is a deliberate cap so targets stay usable on a 360dp-wide phone. Workouts live inside Move, one tap from Home.

## Google Play requirements

See `PLAY_STORE_CHECKLIST.md`. The app is not directed at children. Health Connect, camera, and notification permissions are optional and justified in product copy. Billing uses the Play Billing Library.

## Analytics requirements

See `ARCHITECTURE.md`. Events are names and coarse feature ids only. No weight, calories, food names, barcodes, or step counts are attached as parameters.

## Assumptions

- Week boundaries use ISO weeks (Monday).
- A "serving" in the reference catalog is 100 g because that is how the included USDA records are published. Gram quantity is a conversion, not a new measurement.
- Distance is shown from Health Connect when present. The app does not estimate distance from steps and present it as measured.
- Active energy is shown only when Health Connect provides it.
