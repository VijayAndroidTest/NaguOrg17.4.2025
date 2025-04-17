package com.example.naguorg

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

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