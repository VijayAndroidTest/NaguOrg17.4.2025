package com.example.naguorg.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.navigation.NavController
import com.example.naguorg.util.GoogleSignInHelper

import com.example.naguorg.viewmodel.LoginViewModel

@Composable
fun LoginScreen(

    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {

    val context = LocalContext.current

    var name by remember {
        mutableStateOf("")
    }

    var mobile by remember {
        mutableStateOf("")
    }

    val activity = context as Activity

    val googleClient = GoogleSignInHelper.getClient(activity)

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                GoogleSignInHelper.handleSignInResult(
                    result.data,

                    onSuccess = { credential ->

                        viewModel.login(

                            credential = credential,
                            context = context,
                            name = name,
                            mobile = mobile
                        )

                        {
                            onLoginSuccess()

                        }

                    },

                    onFailure = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    LaunchedEffect(viewModel.error) {

        if (viewModel.error.isNotEmpty()) {

            Toast.makeText(
                context,
                viewModel.error,
                Toast.LENGTH_LONG
            ).show()

            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Name")
                },
                singleLine = true,
                isError = name.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = mobile,
                onValueChange = {

                    if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                        mobile = it
                    }
                },
                label = {
                    Text("Mobile Number")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                isError = mobile.isNotEmpty() && mobile.length != 10,
                supportingText = {
                    if (mobile.isNotEmpty() && mobile.length != 10) {
                        Text("Mobile number must contain 10 digits")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            val isNameValid = name.trim().isNotEmpty()
            val isMobileValid = mobile.length == 10
            val isFormValid = isNameValid && isMobileValid

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid && !viewModel.loading,
                onClick = {

                    if (name.isBlank()) {



                        return@Button
                    }

                    if (mobile.length != 10) {



                        return@Button
                    }

                    launcher.launch(
                        googleClient.signInIntent
                    )
                }
            ) {

                Text("Sign in with Google")
            }
        }

        if (viewModel.loading) {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}