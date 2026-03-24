pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "+"
}

stonecutter {
    create(rootProject) {
        versions("1.20.1", "1.21.1", "1.21.8", "1.21.9", "1.21.11").buildscript("obfuscated.gradle.kts")
        version("26.1")
        vcsVersion = "26.1"
    }
}

rootProject.name = "Stfu"