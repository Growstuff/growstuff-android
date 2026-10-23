# 0003: License the app under AGPL-3.0

- Status: Accepted
- Date: 2026-10-24
- Decided by: Brenda Wallace

## Context

The Growstuff backend is licensed under the GNU Affero General Public License v3.0. We want the app to be open source in the same spirit, so that anyone can see, change and share it.

## Decision

The app is licensed under **AGPL-3.0**, the same as the backend. The repository carries the licence text in `LICENSE`.

## Options we turned down

- **A more permissive licence** (such as Apache-2.0 or MIT). Would let others use the code in closed products, which does not fit the project's aims.

## Consequences

- The libraries we plan to use (AndroidX, Jetpack Compose, Kotlin, OkHttp, Retrofit, Room, Hilt, Coil, AppAuth) are Apache-2.0, which is compatible with AGPL-3.0. We check the licence of every new dependency.
- Distributing the app means offering the source. The Play listing and an About screen in the app link to this repository, and releases are tagged.
- Google's proprietary libraries (Play Services, Firebase) and the Google Maps SDK are a grey area for an AGPL app, and F-Droid will not accept them. We avoid them in the core, and use OpenStreetMap-based maps. Whether to ship two build flavours is still to be decided (see "Still to decide" in the index).
- Source files carry an SPDX licence header. Whether it says `or-later` or `only` is still to be decided, and will match the backend.
- Play's Play App Signing means Google holds the signing key. Android lets people install their own builds, so this is fine in practice; we note it in the README. We would like someone to confirm the licensing is fine for Play distribution before the first public release.
