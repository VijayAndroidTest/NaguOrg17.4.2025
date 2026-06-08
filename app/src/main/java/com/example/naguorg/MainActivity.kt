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
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val updateMessage = remoteConfig.getString("update_message")
                            val updateUrl = remoteConfig.getString("update_url")
                            val minVersionCode = remoteConfig.getLong("min_version_code").toInt()

                            val currentVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                packageManager.getPackageInfo(packageName, 0).longVersionCode.toInt()
                            } else {
                                @Suppress("DEPRECATION")
                                packageManager.getPackageInfo(packageName, 0).versionCode
                            }

                            // Logs for debugging
                            Log.w("FORCE_UPDATE_TEST", "Current: $currentVersionCode, MinRequired: $minVersionCode")

                            if (currentVersionCode < minVersionCode) {
                                showUpdateDialog(updateMessage, updateUrl)
                            }
                        } else {
                            Log.e("FORCE_UPDATE_TEST", "Fetch failed: ${task.exception}")
                        }
                    }

                // Call your Composable here inside NaguOrgTheme
                NaguOrganicsApp()
            }
        }

        requestNotificationPermission()
        fetchFCMToken()
    }

    private fun showUpdateDialog(message: String, url: String) {
        // Use 'this' as the context
        AlertDialog.Builder(this)
            .setTitle("App Update Required")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Update") { _, _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                finish()
            }
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