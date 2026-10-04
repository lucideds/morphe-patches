package app.morphe.patches.devmaxx

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch

private val COMPATIBILITY_DEVMAXX = Compatibility(
    name = "Devmaxx",
    packageName = "dev.devmaxx",
    description = "Devmaxx - language learning app.",
    appIconColor = 0x1B5E20,
)

/**
 * Removes Google's pairIP licence (tamper) check.
 *
 * pairIP wraps the Application class: AndroidManifest.xml declares
 * `android:name="com.pairip.application.Application"`, which extends `dev.devmaxx.DevmaxxApp` and
 * calls `LicenseClient.checkLicense(Context)` from `attachBaseContext`. A second caller sits in
 * `LicenseContentProvider`. checkLicense compares the APK signature against the one Play
 * delivered, so ANY re-signed APK fails it and the app raises a hard
 * "Check that Google Play is enabled ... try reinstalling the app" dialog. On a device with no
 * working Play licensing there is nothing to reinstall from, so the app cannot start.
 *
 * This does NOT touch in-app purchase entitlement. Devmaxx uses RevenueCat; subscription state is
 * fetched from api-production.8-lives-cat.io and materialises in
 * `dev.devmaxx.data.model.UserSubscription`, whose `a()Z` is the gate. Deliberately left alone.
 *
 * The fingerprint anchors on a distinctive error string rather than the class name, so it
 * survives R8 renaming of pairIP's internals. Verified against versionCode 20.
 */
val pairIpLicenseCheckPatch = bytecodePatch(
    name = "Remove pairIP licence check",
    description = "Lets a re-signed APK start. pairIP's signature check fails on any re-signed " +
        "build and hard-blocks the app with a reinstall-from-Play dialog. Does not affect " +
        "in-app purchases.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_DEVMAXX)

    execute {
        // static checkLicense(Context)V. The only method in the dex containing this string.
        val checkLicense = Fingerprint(
            returnType = "V",
            parameters = listOf("Landroid/content/Context;"),
            strings = listOf("Cannot check license with null context."),
        )

        // return-void as the first instruction; the remainder becomes unreachable dead code.
        checkLicense.method.addInstructions(
            0,
            """
            return-void
            """,
        )
    }
}
