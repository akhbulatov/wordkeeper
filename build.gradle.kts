// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("compileSdkVersion", 34)
        set("targetSdkVersion", 34)
        set("minSdkVersion", 24)

        set("kotlinVersion", "2.0.0")
        set("androidGradleVersion", "8.5.1")
        set("materialVersion", "1.13.0-alpha04")
        set("appcompatVersion", "1.7.0")
        set("constraintLayoutVersion", "2.1.4")
        set("coreVersion", "1.13.1")
        set("fragmentVersion", "1.8.2")
        set("recyclerViewVersion", "1.3.2")
        set("firebaseCrashlyticsVersion", "19.0.3")
        set("flowBindingVersion", "1.2.0")
        set("daggerVersion", "2.51.1")
        set("roomVersion", "2.6.1")
        set("coroutinesVersion", "1.9.0-RC")
        set("ciceroneVersion", "7.1")
        set("leakCanaryVersion", "2.14")
    }

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${rootProject.extra["androidGradleVersion"]}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${rootProject.extra["kotlinVersion"]}")
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.2")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.51.0")
    }
}

plugins {
    id("com.google.devtools.ksp") version "2.0.0-1.0.23"
    id("com.github.ben-manes.versions") version "0.51.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
