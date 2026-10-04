group = "app.morphe"

patches {
    about {
        name = "Devmaxx Patches"
        description = "Morphe patches for Devmaxx (dev.devmaxx)"
        source = "git@github.com:lucideds/morphe-patches.git"
        author = "lucideds"
        contact = "https://github.com/lucideds/morphe-patches/issues"
        website = "https://github.com/lucideds/morphe-patches"
        license = "GNU General Public License v3.0, with additional GPL section 7 requirements"
    }
}

dependencies {
    // Required due to smali, or the build fails. Can be removed once smali is bumped.
    implementation(libs.guava)
    implementation(libs.morphe.patches.library)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
