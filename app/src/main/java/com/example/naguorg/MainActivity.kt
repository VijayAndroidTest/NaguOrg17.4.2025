package com.example.naguorg

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.naguorg.ui.theme.NaguOrgTheme
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            NaguOrgTheme {
                // Initialize Remote Config
                val remoteConfig = Firebase.remoteConfig
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 0
                }
                remoteConfig.setConfigSettingsAsync(configSettings)

                // Fetch and Activate
                // ... inside onCreate, in the fetchAndActivate listener
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val updateMessage = remoteConfig.getString("update_message")
                            val updateUrl = remoteConfig.getString("update_url")

                            // Get the minimum version allowed
                            val minVersionCode = remoteConfig.getLong("min_version_code").toInt()

                            // Get current app version
                            val currentVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                packageManager.getPackageInfo(packageName, 0).longVersionCode.toInt()
                            } else {
                                @Suppress("DEPRECATION")
                                packageManager.getPackageInfo(packageName, 0).versionCode
                            }

                            Log.d("RemoteConfig", "Current: $currentVersionCode, MinRequired: $minVersionCode")

                            // Trigger update if current version is less than the minimum required
                            if (currentVersionCode < minVersionCode) {
                                showUpdateDialog(updateMessage, updateUrl)
                            }
                        }
                    }

                NaguOrganicsApp()
            }
        }

        requestNotificationPermission()
        fetchFCMToken()
    }

    private fun showUpdateDialog(message: String, url: String) {
        AlertDialog.Builder(this)
            .setTitle("App Update Required")
            .setMessage(message)
            .setCancelable(false) // User cannot click outside the dialog to dismiss it
            .setPositiveButton("Update") { _, _ ->
                // Redirect user to the store or browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                // Optional: Close the app after sending them to update
                finish()
            }
            // Removed the NegativeButton ("Later") entirely
            .show()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    private fun fetchFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "FCM Token: $token")
            }
        }
    }
}