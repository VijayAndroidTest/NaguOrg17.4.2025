package com.example.naguorg.nage_common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.naguorg.nage_common.MyWorker

// Firebase messaging
class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule any WorkManager jobs if needed
            WorkManager.getInstance(context).enqueue(
                OneTimeWorkRequestBuilder<MyWorker>().build()
            )
        }
    }
}