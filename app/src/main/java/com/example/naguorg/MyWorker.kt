package com.example.naguorg

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker (appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Your background task here
        Log.d("MyWorker", "Work is running in background")

        // Return success
        return Result.success()
    }
}