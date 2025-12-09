pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8-beta.1"
}

stonecutter {
    create(rootProject) {
        versions("1.20.1", "1.21.8", "1.21.9", "1.21.11")
        vcsVersion = "1.21.9"
    }
}

rootProject.name = "Stfu"