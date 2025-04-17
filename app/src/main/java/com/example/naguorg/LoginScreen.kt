    package com.example.naguorg

    import android.annotation.SuppressLint
    import android.app.Activity
    import android.content.Context
    import android.content.SharedPreferences
    import android.util.Log
    import android.widget.Toast
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TopAppBar
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.SpanStyle
    import androidx.compose.ui.text.buildAnnotatedString
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.text.input.TextFieldValue
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.text.withStyle
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.google.android.gms.auth.api.signin.GoogleSignIn
    import com.google.android.gms.auth.api.signin.GoogleSignInAccount
    import com.google.android.gms.auth.api.signin.GoogleSignInClient
    import com.google.android.gms.auth.api.signin.GoogleSignInOptions
    import com.google.firebase.Firebase
    import com.google.firebase.FirebaseApp
    import com.google.firebase.FirebaseException
    import com.google.firebase.appcheck.FirebaseAppCheck
    import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.auth.GoogleAuthProvider
    import com.google.firebase.auth.PhoneAuthCredential
    import com.google.firebase.auth.PhoneAuthOptions
    import com.google.firebase.auth.PhoneAuthProvider
    import com.google.firebase.auth.auth
    import com.google.firebase.auth.userProfileChangeRequest
    import java.util.concurrent.TimeUnit

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("ContextCastToActivity")
    @Composable
    fun LoginScreen(onLoginSuccess: () -> Unit) {


        var hasPermission by remember { mutableStateOf(false) }
        // Launcher to request permission
        val requestPermissionLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                hasPermission = isGranted
            }



        val context = LocalContext.current as Activity
        val auth = remember { Firebase.auth }
        val googleSignInClient = remember { getGoogleSignInClient(context) }

        var name by remember { mutableStateOf(TextFieldValue("")) }
        var mobileNumber by remember { mutableStateOf(TextFieldValue("")) }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(Exception::class.java)
                    account?.let {
                        firebaseAuthWithGoogle(it, auth, name.text, mobileNumber.text, context, onLoginSuccess)
                    }
                } catch (e: Exception) {
                    isLoading = false
                    Toast.makeText(context, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                isLoading = false
            }
        }
        Scaffold(

        )
        { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Ensures content is placed below the top bar
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.White, Color(0xFF6dd5ed))
                        )
                    )
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(4.dp)
                        .border(0.dp, Color.Gray, RoundedCornerShape(0.dp)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { newValue ->
                            name = newValue
                            if (newValue.text.isNotBlank()) {
                                errorMessage = ""  // Clear the error when the user enters a name
                            }
                        },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = mobileNumber,
                        onValueChange = { newValue ->
                            if (newValue.text.all { it.isDigit() }) {
                                if (newValue.text.length <= 10) {
                                    mobileNumber = newValue
                                    errorMessage = if (newValue.text.length == 10) "" else "Enter a valid 10-digit number"
                                }
                            }
                        },
                        label = { Text("Mobile Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = {
                                if (name.text.isBlank()) {
                                    errorMessage = "Name cannot be empty"
                                }
                                else if (mobileNumber.text.length != 10) {
                                    errorMessage = "Enter a valid 10-digit mobile number"
                                } else {
                                    isLoading = true
                                    val signInIntent = googleSignInClient.signInIntent
                                    launcher.launch(signInIntent)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.giconsign), // Replace with your Google icon
                                contentDescription = "Google Logo",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Adds spacing between icon and text
                            Text(
                                text = "Sign in with Google",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }


    private fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount,
        auth: FirebaseAuth,
        name: String,
        mobileNumber: String,
        context: Context,
        onLoginSuccess: () -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        Log.d("Google Sign-In", "User: ${it.uid}, Name: $name, Mobile: $mobileNumber")

                        // Save user data in SharedPreferences
                        saveUserData(context, name, mobileNumber)

                        onLoginSuccess()
                    }
                } else {
                    Log.e("Google Sign-In", "Authentication Failed: ${task.exception?.message}")
                }
            }
    }


    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("924827024871-ojjn6cajlevvta20d4mv6pkqg1u2b94q.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    // Function to save user data in SharedPreferences
    private fun saveUserData(context: Context, name: String, mobileNumber: String) {
        val sharedPref: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("userName", name)
            putString("userMobile", mobileNumber)
            apply()
            Log.d("WhatsAppShare", "User Name: $name, User Phone: $mobileNumber")

        }
    }