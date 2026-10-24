# growstuff-android

The Growstuff app for Android phones and tablets, in Kotlin and Jetpack Compose. It is early and experimental. The plan is in the roadmap issue (#13), and the decisions behind it are in [docs/adr](docs/adr/).

## Designed for low vision

The app's designs are optimised for people with low vision, and for anyone using a phone outdoors in bright sun with dirty hands. When you build or change a screen, keep to this:

- Thick black outlines (4px) around every block and control, so edges are easy to see.
- High contrast: text on its background should meet WCAG AA (4.5:1), using the Growstuff palette. Colour is never the only way to tell things apart.
- Large type, and large tap targets: the main controls are at least 56px tall.
- Short, plain labels, with actions in capitals.
- It must work with Android's font scaling and with TalkBack.

## What works so far

Sign in, home, my jobs (tick off with undo, add a job) and my gardens. On a tablet, home and the chosen area sit side by side.

The app can't talk to growstuff.org yet: signing in needs OAuth on the server (#2). Until then every repository is a fake that reads JSON:API documents from [app/src/main/assets/fixtures](app/src/main/assets/fixtures/), written in the same shape as the real `/api/v1` responses. The fixture dates are moved so that the fixtures' "today" (24 October 2026) is today on the phone. Changes, such as ticking off a job, last until the app is closed.

The fixtures include some things the API doesn't serve yet:

- a signed-in member (there is no "who am I" endpoint)
- a garden's `location`, `area` and `area_unit`
- a job's `finished_at`
- job ideas (suggested jobs)

## Building

You need JDK 21 (Gradle downloads it if it's missing) and the Android SDK, which Android Studio installs for you. Without Android Studio, install the command-line tools and set `ANDROID_HOME`, or put `sdk.dir=/path/to/sdk` in `local.properties`.

There are two flavours. `play` may use Google services, such as Firebase for push notifications, and `foss` has no proprietary code. Today they are the same.

```sh
./gradlew assembleFossDebug       # build a debug APK
./gradlew installFossDebug        # build and install on a connected phone
./gradlew testFossDebugUnitTest   # unit tests
./gradlew ktlintCheck             # code style (ktlintFormat fixes most of it)
./gradlew lintFossDebug           # Android lint
```

The debug app is installed as `org.growstuff.android.debug`, so it sits beside a release build.

### Running on your phone

1. On the phone, open Settings, About phone, and tap Build number seven times to turn on developer options.
2. In Settings, System, Developer options, turn on USB debugging.
3. Plug the phone in and accept the prompt on the phone.
4. Check it is there with `adb devices`, then run `./gradlew installFossDebug`.

In Android Studio, pick the phone in the device menu and press Run.

## Licence

AGPL-3.0-or-later, the same as the Growstuff website. See [LICENSE](LICENSE), and [NOTICE](NOTICE) for third-party material. Every source file starts with an SPDX licence line. Google Play's app signing means Google holds the release signing key; you can still build and install your own copy.
