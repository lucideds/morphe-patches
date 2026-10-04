# morphe-patches

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

## Add to Morphe

Open this link on your phone, or paste it into Morphe's "Add patch source" dialog:

```
https://morphe.software/add-source?github=lucideds/morphe-patches
```

Requires **Expert Mode**: Morphe → Settings → Expert settings → Expert Mode.

The manager reads `patches-bundle.json` from the repo root, which points at the `.mpp` attached
to the latest GitHub release. **Bump `version` and `download_url` in `patches-bundle.json` when
you tag a release** — that file is the only thing the manager reads, so a new tag alone is not
enough.

## Build

Requires JDK 21+ and GitHub Packages access to the Morphe registry (`gpr.user` / `gpr.key` in
`~/.gradle/gradle.properties`).

```bash
./gradlew buildAndroid      # -> patches/build/libs/patches-*.mpp
gh release create v<version> patches/build/libs/patches-*.mpp
```

The bundle must be built with `-dontobfuscate`: a renamed patch class breaks the `PatchLoader`
reflection that discovers patches from the bundle.

## Layout

```
patches/src/main/kotlin/app/morphe/patches/devmaxx/
  PairIpLicenseCheckPatch.kt
```

## Licence / attribution

Morphe patches are GPL-3.0 with GPLv3 §7 branding restrictions. This is an independent
third-party patch set and is not endorsed by or affiliated with Morphe or Devmaxx.
