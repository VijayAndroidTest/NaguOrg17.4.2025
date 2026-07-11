package com.example.naguorg.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.naguorg.R
import com.example.naguorg.SocialMediaIcon


@Composable
fun ContactSection() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp), // Added bottom margin
        contentAlignment = Alignment.BottomCenter // Align content to the bottom
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = "Follow us on:",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp), // Bottom margin for social media icons
                horizontalArrangement = Arrangement.Center
            ) {
                SocialMediaIcon(
                    iconRes = R.drawable.call2,
                    action = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:8838380787")
                        }
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp)) // Corrected spacing

                SocialMediaIcon(
                    iconRes = R.drawable.wh100,
                    action = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://wa.me/918838380787")
                        }
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp)) // Corrected spacing

                SocialMediaIcon(
                    iconRes = R.drawable.facebook,
                    url = "https://www.facebook.com/share/1G5Re3C9Pc/",
                    context = context
                )
                Spacer(modifier = Modifier.width(8.dp)) // Corrected spacing

                SocialMediaIcon(
                    iconRes = R.drawable.insta,
                    url = "https://www.instagram.com/nagu_organics?utm_source=qr&igsh=ZTNoYWx6ZXBzazN6",
                    context = context
                )
                Spacer(modifier = Modifier.width(8.dp)) // Corrected spacing

                SocialMediaIcon(
                    iconRes = R.drawable.twitter,
                    url = "https://x.com/Thennarasu1996?t=5DuEeAB26fLU0KFgqTt7NQ&s=09",
                    context = context
                )
            }
        }
    }
}