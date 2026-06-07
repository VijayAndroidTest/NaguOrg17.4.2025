package com.example.naguorg

import android.app.Application
import com.google.firebase.Firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this // Assign instance
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        // Enable Firebase App Check with Play Integrity
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )


    }

    // In your MyApplication.kt

    private fun isUpdateRequired(currentVersion: String, minVersion: String): Boolean {
        // Basic comparison: returns true if minVersion is greater than currentVersion
        // Note: If versions are simple (1.0, 1.1), this works.
        // For more complex versions (1.0.2), use a semantic versioning library.
        return minVersion.replace(".", "").toInt() > currentVersion.replace(".", "").toInt()
    }

    private fun showUpdateDialog(message: String) {
        // Since Application context doesn't have a UI, you should post this
        // to a handler or trigger it from your MainActivity once it's created.
        println("Update Message: $message")
    }
}


