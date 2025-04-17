package com.example.naguorg

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
@Composable
fun SocialMediaIcon(iconRes: Int, action: () -> Unit) {
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = "Social Media Icon",
        modifier = Modifier
            .clickable { action() }
            .height(40.dp)
            .padding(4.dp)
    )
}