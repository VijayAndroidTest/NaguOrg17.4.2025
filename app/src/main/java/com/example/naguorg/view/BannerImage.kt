package com.example.naguorg.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.naguorg.R

@Composable
fun BannerImage() {


    Column(
        modifier = Modifier
            .fillMaxWidth(),

        horizontalAlignment = Alignment.CenterHorizontally // Centers content horizontally
    ) {


        val image: Painter = painterResource(id = R.drawable.nagu_organics_logo)
        Image(
            painter = image,
            contentDescription = "Nagu Organics Banner",
            modifier = Modifier.padding(top = 6.dp)
        )
    }

}