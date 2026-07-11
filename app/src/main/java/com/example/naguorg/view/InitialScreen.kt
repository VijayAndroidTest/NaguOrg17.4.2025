package com.example.naguorg.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.naguorg.repository.LoginRepository
import com.example.naguorg.ui.LoginScreen
import com.example.naguorg.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun InitialScreen() {

    var showSplash by remember { mutableStateOf(true) }
    var isLoggedIn by remember { mutableStateOf(false) }

    val repository = remember {
        LoginRepository(FirebaseAuth.getInstance())
    }

    val viewModel = remember {
        LoginViewModel(repository)
    }

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false
        isLoggedIn = auth.currentUser != null
    }

    Scaffold(
        topBar = {
            NaguTopBar()
        }
    ) { padding ->

        Box(Modifier.padding(padding)) {

            when {

                showSplash ->
                    SplashScreen()

                !isLoggedIn ->
                    LoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = {
                            isLoggedIn = true
                        }
                    )

                else ->
                    NaguOrganics()
            }
        }
    }
}