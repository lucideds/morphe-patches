# devmaxx-patches

Morphe patches for [Devmaxx](https://devmaxx.dev) (`dev.devmaxx`).

| Patch | What it does |
|---|---|
| Remove pairIP licence check | Lets a re-signed APK start. Google's pairIP tamper check fails on any re-signed build and hard-blocks the app with a "reinstall from Play" dialog. Does **not** touch in-app purchases. |

## What this is for

Devmaxx is a paid app. Devmaxx-patches removes Google's **pairIP tamper check** so a
locally re-signed APK will launch on a device without working Play licensing (de-Googled
Android, sandboxed Play, etc.). That check blocks the app from starting at all; it is not an
in-app purchase check.

Subscription entitlement is **not** modified. Devmaxx uses RevenueCat; the subscription state is
fetched from `api-production.8-lives-cat.io` and lands in
`dev.devmaxx.data.model.UserSubscription`. Anything gated on that still requires a valid purchase.

## Build

Requires JDK 21+ and the Morphe patcher (`app.morphe:morphe-patcher`, GitHub Packages).

```bash
./gradlew buildAndroid      # -> patches/build/libs/patches-*.mpp
```

`build.gradle.kts` must pass `-dontobfuscate` to R8: a renamed patch class breaks the
`PatchLoader` reflection that discovers patches from the bundle.

## Layout

```
patches/src/main/kotlin/app/morphe/patches/devmaxx/
  PairIpLicenseCheckPatch.kt
```

## Licence / attribution

Morphe patches are GPL-3.0 with GPLv3 §7 branding restrictions. This is an independent
third-party patch set and is not endorsed by or affiliated with Morphe or Devmaxx.
