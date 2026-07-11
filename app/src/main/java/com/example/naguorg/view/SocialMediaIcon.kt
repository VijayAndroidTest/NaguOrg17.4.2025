package com.example.naguorg.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun SocialMediaIcon(iconRes: Int, url: String, context: Context) {
    Box(
        modifier = Modifier
            .size(35.dp) // Larger touch area
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "Social Media Icon",
            modifier = Modifier.fillMaxSize() // Ensure full area is clickable
        )
    }
}