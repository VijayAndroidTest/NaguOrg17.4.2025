package com.example.naguorg.view

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SplashScreen() {

    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                setVideoURI(Uri.parse("android.resource://${context.packageName}/raw/splashvideonew"))
                setOnPreparedListener { it.start() }
                setOnCompletionListener { it.start() } // Restart when finished
            }
        },
        modifier = Modifier.fillMaxSize()
    )


}