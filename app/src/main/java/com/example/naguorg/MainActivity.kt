package com.example.naguorg

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.naguorg.ui.theme.NaguOrgTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.Manifest
import android.app.AlertDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()

        setContent {
            NaguOrgTheme {

               
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600 // Fetch every hour (adjust as needed)
                }

                val remoteConfig = Firebase.remoteConfig
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val updateMessage = remoteConfig.getString("update_message")
                            if (updateMessage.isNotEmpty()) {
                                // Display the update message
                                showUpdateDialog(updateMessage)
                            }
                        } else {
                            Log.e("MainActivity", "Remote Config fetch failed", task.exception)
                        }
                    }
//                remoteConfig.setConfigSettingsAsync(configSettings)
//
//                // Set default values (optional)
//                remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
//
//                remoteConfig.fetchAndActivate()
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            val updated = task.result
//                            val welcomeMessage = remoteConfig.getString("welcome_message")
//                            Log.d("MainActivity", "Remote Config fetched. Welcome message: $welcomeMessage")
//                            // Use welcomeMessage
//                        } else {
//                            Log.e("MainActivity", "Remote Config fetch failed", task.exception)
//                            // Handle error, e.g., show a default message
//                        }
//                    }

                NaguOrganicsApp()
            }
        }

        requestNotificationPermission()
        fetchFCMToken()
    }
    fun showUpdateDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("App Update Available")
            .setMessage(message)
            .setPositiveButton("Update") { _, _ ->
                // Redirect user to the update source (e.g., Play Store, APK download link)
                // Add your update logic here.
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
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "FCM Token: $token")
        }
    }
}