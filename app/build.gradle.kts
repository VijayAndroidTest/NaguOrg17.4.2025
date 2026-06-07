plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.firebase.appdistribution)
    alias(libs.plugins.google.firebase.firebase.perf)
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.naguorg"
    compileSdk = 35

    signingConfigs {
        create("release") {
            // Use double backslashes to escape the path in Kotlin strings
            storeFile = file("C:\\Jenkins\\keys\\nagukey.jks")
            // DO NOT put passwords here. Jenkins will provide them via environment variables.
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = "nagukey"
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }

    defaultConfig {
        applicationId = "com.example.naguorg"
        minSdk = 24
        targetSdk = 35
        // Updated versioning
        versionCode = 2
        versionName = "1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            firebaseAppDistribution {
                artifactType = "APK"
                releaseNotes = "New test build from Jenkins"
                groups = "testers"
            }
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            firebaseAppDistribution {
                // REMOVE: artifactFile = "..." (This is what caused the error)
                artifactType = "APK"
                releaseNotes = "Production Release v1.1"
                groups = "testers"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // ... (Your existing dependencies remain unchanged)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.functions)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)
    implementation(libs.firebase.inappmessaging.display)
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation(libs.firebase.perf)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")
    implementation ("androidx.work:work-runtime-ktx:2.8.1")
    implementation ("com.cloudinary:cloudinary-android:2.3.1")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("androidx.compose.foundation:foundation:1.6.0")
    implementation("com.google.firebase:firebase-appcheck-playintegrity:17.0.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("com.google.code.gson:gson:2.10")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")


    implementation("com.google.zxing:core:3.5.2")
    implementation("androidx.compose.ui:ui-graphics:1.5.0")
    implementation ("com.google.android.gms:play-services-base:18.2.0")
    implementation ("com.google.android.gms:play-services-wallet:19.2.0")
}