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
                    minimumFetchIntervalInSeconds = 360
                }
                remoteConfig.setConfigSettingsAsync(configSettings)

                // Fetch and Activate
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val updateMessage = remoteConfig.getString("update_message")
                            // You should also have an 'update_url' parameter in Firebase
                            val updateUrl = remoteConfig.getString("update_url")

                            if (updateMessage.isNotEmpty() && updateUrl.isNotEmpty()) {
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
            .setTitle("App Update Available")
            .setMessage(message)
            .setCancelable(false) // Prevent user from dismissing if it's a mandatory update
            .setPositiveButton("Update") { _, _ ->
                // Redirect user to the store or browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            .setNegativeButton("Later", null)
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