import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebase.crashlytics)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.akhbulatov.wordkeeper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.akhbulatov.wordkeeper"
        minSdk = 24
        targetSdk = 34
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
    implementation(libs.kotlin.stdlib)

    implementation(libs.material)
    implementation(libs.appcompat)
    implementation(libs.constraintLayout)
    implementation(libs.core.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.recyclerView)

    implementation(libs.flowbinding.android)

    implementation(libs.firebase.crashlytics)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.cicerone)

    debugImplementation(libs.leakCanary.android)
}
