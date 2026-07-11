package com.example.naguorg.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NaguTopBar() {

    TopAppBar(
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color(0xFFFFA000),
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("NAGU")
                    }

                    withStyle(
                        SpanStyle(
                            color = Color(0xFF004AAD),
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append(" ORGANICS")
                    }
                }
            )
        }
    )
}