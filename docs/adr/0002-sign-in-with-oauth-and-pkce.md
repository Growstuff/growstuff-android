# 0002: Sign in with OAuth 2 and PKCE, through the website

- Status: Accepted
- Date: 2026-10-24
- Decided by: Brenda Wallace

## Context

The Growstuff API today authenticates with a per-member token that a member copies from their account page on the website. There is no login or logout in the API, which is not workable for a phone app. People need to sign in, stay signed in, and sign out. Sign-up stays on the website.

## Decision

The app signs in with **OAuth 2 using the authorisation code flow with PKCE**, as a public client with no secret in the app. Signing in opens the website's own login page in the browser; the app never sees the password. Sign-out revokes the token on the server and clears what the app has stored.

On the Rails side this is planned with the Doorkeeper gem, with short-lived access tokens and refresh tokens. The existing per-member API token keeps working, so integrations such as `homeassistant-growstuff` are not affected.

## Options we turned down

- **An email-and-password endpoint that returns a token.** Much less work, but the app would handle passwords, and it would not work with password managers or any future change to how people sign in.

## Consequences

- The backend needs OAuth added before the app can sign in: tracked in the backend epic in this repo (#2).
- The app uses a library for the browser hop (AppAuth for Android) and stores tokens encrypted with the Android Keystore.
- Sign-up and account deletion are links to the website, which also meets Google Play's requirement for an account deletion link.
- Reviewers at Google Play need a test account to sign in, so we will provide one when we publish.
