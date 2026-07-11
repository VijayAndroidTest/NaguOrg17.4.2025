package com.example.naguorg.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NaguOrganics() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Centers content within the Box
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Spaces out the elements
        ) {
            BannerImage()
            Spacer(modifier = Modifier.height(20.dp)) // Add 20dp space
            availProductBtn()
            Spacer(modifier = Modifier.height(20.dp)) // Add 20dp space
            ContactSection()
        }
    }
}