# 0001: A native Android app, written in Kotlin with Jetpack Compose

- Status: Accepted
- Date: 2026-10-24
- Decided by: Brenda Wallace

## Context

Growstuff is a website (Rails). Gardeners want to use it out in the garden, on a phone: look things up, tick off jobs, and record what they have planted and harvested. The site works on a phone and can be installed as a web app, but we want something that works well with a poor signal and can use the camera.

We want three main areas in the app: my gardens, friends and neighbours, and the jobs to do. Sign-up can stay on the website.

## Decision

Build a **native Android app**, **written in Kotlin** and using **Jetpack Compose** for the screens. **Android only** for now; iOS is not part of this work.

## Options we turned down

- **A progressive web app on the existing site.** Least work, and already partly possible, but weaker offline support, camera and notifications, and not what we want to offer.
- **A cross-platform toolkit** (Flutter or React Native). Would make an iOS version easier later, but we do not need iOS now, and a native app gives the best Android experience.
- **Java.** Still supported, but Compose is Kotlin-only and Google's current libraries and documentation are written in Kotlin.

## Consequences

- The app lives in its own repository (`Growstuff/growstuff-android`), with its own build, tests, signing and release process. Backend changes it needs happen in `Growstuff/growstuff`.
- Android Studio and the Android SDK are needed to work on it.
- There is no iOS app. If that changes, it is a new decision.
- Screens are written once and adapt to the screen size, so tablets are supported as well as phones (see 0004).
