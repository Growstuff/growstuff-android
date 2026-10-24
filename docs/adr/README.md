# Architecture decision records

Short notes on the significant decisions for the Growstuff Android app: what we decided, why, and what follows from it. Each one is a single decision. If we change our mind we write a new record that supersedes the old one, and leave the old one in place.

Each record has a status (proposed, accepted, or superseded by a later one), a date, the context, the decision, the options we turned down, and the consequences.

| # | Decision | Status |
|---|---|---|
| [0001](0001-native-android-app-in-kotlin-and-compose.md) | A native Android app, written in Kotlin with Jetpack Compose | Accepted |
| [0002](0002-sign-in-with-oauth-and-pkce.md) | Sign in with OAuth 2 and PKCE, through the website | Accepted |
| [0003](0003-agpl-licence.md) | License the app under AGPL-3.0 | Accepted |
| [0004](0004-designed-for-low-vision-phone-first.md) | Designed for low vision, phone first, tablets supported | Accepted |

## Still to decide

These are tracked in the "Decisions to make before building" issue (#12), and will get a record here once they are settled:

- the application id, and who owns the Google Play developer account
- whether to build two flavours: `play` (with Google services such as Firebase push) and `foss` (with none)
- the minimum Android version
- whether source files carry `AGPL-3.0-or-later` or `AGPL-3.0-only`
- how jobs work offline (the plan is a local database that syncs when there is a connection)

## Writing a new record

Copy the shape of an existing one, give it the next number, add it to the table above, and set its status. Keep it short.
