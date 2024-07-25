import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.akhbulatov.wordkeeper"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        applicationId = "com.akhbulatov.wordkeeper"
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
        versionCode = 12
        versionName = "2.0.1"
        resourceConfigurations += setOf("en", "ru")

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file(keystoreProperties["release_store_file"]!!)
            storePassword = keystoreProperties["release_store_password"] as String
            keyAlias = keystoreProperties["release_key_alias"] as String
            keyPassword = keystoreProperties["release_key_password"] as String
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            manifestPlaceholders["enableCrashReporting"] = false
        }

        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["enableCrashReporting"] = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlinVersion"]}")

    // AndroidX
    implementation("com.google.android.material:material:${rootProject.extra["materialVersion"]}")
    implementation("androidx.appcompat:appcompat:${rootProject.extra["appcompatVersion"]}")
    implementation("androidx.constraintlayout:constraintlayout:${rootProject.extra["constraintLayoutVersion"]}")
    implementation("androidx.core:core-ktx:${rootProject.extra["coreVersion"]}")
    implementation("androidx.fragment:fragment-ktx:${rootProject.extra["fragmentVersion"]}")
    implementation("androidx.recyclerview:recyclerview:${rootProject.extra["recyclerViewVersion"]}")

    implementation("io.github.reactivecircus.flowbinding:flowbinding-android:${rootProject.extra["flowBindingVersion"]}")

    // DI
    implementation("com.google.dagger:dagger:${rootProject.extra["daggerVersion"]}")
    ksp("com.google.dagger:dagger-compiler:${rootProject.extra["daggerVersion"]}")

    // Database
    implementation("androidx.room:room-runtime:${rootProject.extra["roomVersion"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["roomVersion"]}")
    ksp("androidx.room:room-compiler:${rootProject.extra["roomVersion"]}")

    // Async
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["coroutinesVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["coroutinesVersion"]}")

    // Navigation
    implementation("com.github.terrakok:cicerone:${rootProject.extra["ciceroneVersion"]}")

    // Dev Tools
    implementation("com.google.firebase:firebase-crashlytics:${rootProject.extra["firebaseCrashlyticsVersion"]}")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${rootProject.extra["leakCanaryVersion"]}")
}
