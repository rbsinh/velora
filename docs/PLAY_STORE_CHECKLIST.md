# Play Store checklist

Working title: **Velora**. Application id: `app.velora.track`. Version code 1, version name `0.1.0`.

This is a preparation checklist, not a claim that the listing is submitted.

## App content

- [ ] Confirm the name "Velora" is available in the target Play locales and does not infringe a trademark.
- [ ] Write a short and full description in original words. Do not use another app's screenshots or copy.
- [ ] Provide a 512×512 hi-res icon derived from the adaptive icon.
- [ ] Provide phone screenshots of the real UI.
- [ ] Feature graphic.
- [ ] Categorize as Health & Fitness.
- [ ] Content rating questionnaire. Not directed at children. No violence, no user-generated public sharing in the MVP.
- [ ] Target audience: 18 and older. Age gate in onboarding.
- [ ] Ads declaration: no ads.
- [ ] News / COVID / health: the app is a personal tracker and shows estimates. It does not claim to diagnose or treat.
- [ ] Privacy policy URL hosted on HTTPS. In-app screen must match it.
- [ ] Account deletion URL is not required until accounts exist. Local deletion is in the app. When accounts ship, add a web deletion path.

## Data safety form

Declare:

- Health and fitness info, collected for app functionality, stored on device, not shared with an ad network.
- Optional photos (progress), stored on device, user-selected.
- Purchase history handled by Google Play.
- No approximate or precise location.
- No data sold.
- No data shared with third parties for advertising.
- Users can delete local data in Profile.
- Encryption in transit: not applicable until a backend is called. When the API URL is set, traffic is HTTPS.
- Encryption at rest: device encryption for app-specific storage. Do not claim application-level SQLCipher.

Update the form before enabling cloud sync or a recognition provider. Those are new disclosures (photos sent to a server, account identifiers).

## Permissions and declarations

| Permission | Declaration |
| --- | --- |
| Health Connect reads (steps, distance, active calories) | Shown in Health Connect rationale activity. Used to display activity the user already stored. |
| Health Connect writes (weight, exercise) | Only after an in-app opt-in. Default off. |
| Camera | Barcode scan and optional food photo. |
| Notifications | Only after the user enables a reminder. |
| Billing | Added by the Play Billing manifest. Digital goods use Play Billing, not an external checkout. |
| Internet | Declared for a future API and Play. Core logging does not need it. |

Do not declare location, background location, or activity recognition.

Health Connect policy: the rationale activity is exported with `androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE` and the Android 14 view-permission-usage alias. The Play Console health-apps declaration must describe the same use.

## Build artifacts

- [ ] `bundleRelease` produces an AAB signed with the upload key from CI secrets.
- [ ] `minify` and resource shrinking are on for release.
- [ ] Version code increments every upload.
- [ ] Play App Signing enrolled. The upload key is not the app signing key.
- [ ] Internal testing track before production.
- [ ] Pre-launch report reviewed. This environment has no emulator; that gap is in `TESTING.md`.

## Policy rejects to avoid

- Do not promise medical accuracy.
- Do not ship a subscription that unlocks from a debug switch in release.
- Do not request permissions at first launch that the user has not reached (camera, notifications, health).
- Do not embed a food database whose license forbids redistribution.

## Store listing draft (original)

Velora keeps a private log of meals, steps, workouts, and weight on your phone. Calorie targets are estimates based on a published formula, not medical advice. You confirm any food before it is logged.
