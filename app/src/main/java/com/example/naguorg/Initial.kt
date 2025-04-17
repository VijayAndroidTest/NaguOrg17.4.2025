package com.example.naguorg

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.naguorg.ui.theme.NaguOrgTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Initial : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this@Initial)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        setContent {

            var showSplash by remember { mutableStateOf(true) }
            var isLoggedIn by remember { mutableStateOf(false) } // Track login status
            val auth = remember { FirebaseAuth.getInstance() }

            // Check login status after splash delay
            LaunchedEffect(Unit) {
                delay(3000) // Show splash screen for 1 second
                showSplash = false
            isLoggedIn = auth.currentUser != null // Check if user is already logged in
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = Color(0xFFFFA000), fontWeight = FontWeight.ExtraBold)) {
                                        append("NAGU")
                                    }
                                    withStyle(style = SpanStyle(color = Color(0xFF004AAD), fontWeight = FontWeight.ExtraBold)) {
                                        append(" ORGANICS")
                                    }
                                },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    when {
                        showSplash -> SplashScreen()
                       !isLoggedIn -> LoginScreen(onLoginSuccess = { isLoggedIn = true })
                        else -> NaguOrganics()
                    }
                }
            }
        }
}


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

@Composable
fun availProductBtn() {
    val context = LocalContext.current

    var isClicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isClicked) 0.9f else 1f,
        animationSpec = tween(200)
    )

    Button(
        onClick = {
            isClicked = true
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            Toast.makeText(context, "Avail Products", Toast.LENGTH_SHORT).show()
            isClicked = false // Reset click effect
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 80.dp, bottom = 10.dp)
            .scale(scale)
            .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Shop",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.product_list_button),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}



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
            // Product List Button

//            var isClicked by remember { mutableStateOf(false) }
//            val scale by animateFloatAsState(
//                targetValue = if (isClicked) 0.9f else 1f,
//                animationSpec = tween(200)
//            )
//            Button(
//                onClick = {
//                    isClicked = true
//                    val intent = Intent(context, MainActivity::class.java)
//                    context.startActivity(intent)
//                    Toast.makeText(context, "Product List", Toast.LENGTH_SHORT).show()
//                    isClicked = false // Reset after click
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 10.dp)
//                    .scale(scale)
//                    .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//            ) {
//                Icon(
//                    imageVector = Icons.Default.ShoppingCart,
//                    contentDescription = "Shop",
//                    tint = Color.White,
//                    modifier = Modifier.size(24.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = stringResource(R.string.product_list_button),
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//            }


            // Social Media Section
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
}}