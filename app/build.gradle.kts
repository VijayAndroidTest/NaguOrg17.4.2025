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
            storeFile = file("C:\\Jenkins\\keys\\nagukey.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")?:"Vijay@123"
            keyAlias = "nagukey"
            keyPassword = System.getenv("KEY_PASSWORD")?:"Vijay@123"
        }
    }

    defaultConfig {
        applicationId = "com.example.naguorg"
        minSdk = 24
        targetSdk = 35
//        val jenkinsBuildNumber = System.getenv("BUILD_NUMBER")?.toInt() ?: 1
//
//        versionCode = jenkinsBuildNumber
//        versionName = "1.${jenkinsBuildNumber}"
        versionCode=27
        versionName="nagu update"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
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
                artifactType = "APK"
                // This path is correct for assembleRelease output
                artifactPath = "app/build/outputs/apk/release/app-release.apk"
                releaseNotes = "Production Release v1.4"
                groups = "testers"
            }
        }

    }
    sourceSets {
        getByName("androidTest") {
            assets.srcDirs(files("$projectDir/schemas"))
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
    // Room Testin Migration db
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("com.cloudinary:cloudinary-android:2.3.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("androidx.compose.foundation:foundation:1.6.0")
    implementation("com.google.firebase:firebase-appcheck-playintegrity:17.0.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("com.google.code.gson:gson:2.10")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.cashfree.pg:api:2.2.8")
    implementation("com.cashfree.pg:ui:2.2.8")
    implementation("com.cashfree.pg:ui:2.2.8")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.zxing:core:3.5.2")
    implementation("androidx.compose.ui:ui-graphics:1.5.0")
    implementation("com.google.android.gms:play-services-base:18.2.0")
    implementation("com.google.android.gms:play-services-wallet:19.2.0")
}