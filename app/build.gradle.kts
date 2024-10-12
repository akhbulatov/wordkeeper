import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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
        compose = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtension.get()
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.constraintLayout)
    implementation(libs.core.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.recyclerView)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

    implementation(libs.flowbinding.android)

    implementation(libs.firebase.crashlytics)

    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.cicerone)

    debugImplementation(libs.leakCanary.android)
}
