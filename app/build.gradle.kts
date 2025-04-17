    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        alias(libs.plugins.kotlin.compose)
        alias(libs.plugins.google.gms.google.services)
        alias(libs.plugins.google.firebase.crashlytics)
        alias(libs.plugins.google.firebase.appdistribution)
        alias(libs.plugins.google.firebase.firebase.perf)
        id("kotlin-parcelize") // Add this line

        id("kotlin-kapt") // ✅ Add this line for KAPT support
    }

    android {
        namespace = "com.example.naguorg"
        compileSdk = 35

        defaultConfig {
            applicationId = "com.example.naguorg"
            minSdk = 24
            targetSdk = 35
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        buildTypes {
            debug  {

    //            isMinifyEnabled = false
    //            proguardFiles(
    //                getDefaultProguardFile("proguard-android-optimize.txt"),
    //                "proguard-rules.pro"
    //            )
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
        implementation ("com.squareup.okhttp3:okhttp:4.9.3") // Required for network requests
        implementation("androidx.compose.foundation:foundation:1.6.0") // Or latest
        implementation("com.google.firebase:firebase-appcheck-playintegrity:17.0.1") // or latest
        implementation("com.google.android.gms:play-services-auth:20.7.0")
        implementation("androidx.navigation:navigation-compose:2.7.5") // Latest version
        implementation("com.google.code.gson:gson:2.10")
        implementation("androidx.compose.material:material-icons-extended:1.6.0") // Use the latest version
        implementation("androidx.room:room-runtime:2.6.1")
        kapt("androidx.room:room-compiler:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")
        implementation("com.google.zxing:core:3.5.1")
        implementation("com.razorpay:checkout:1.6.33")
        implementation("com.google.zxing:core:3.5.2")
        implementation("androidx.compose.ui:ui-graphics:1.5.0")
        implementation ("com.google.android.gms:play-services-base:18.2.0")
        implementation ("com.google.android.gms:play-services-wallet:19.2.0")
    }