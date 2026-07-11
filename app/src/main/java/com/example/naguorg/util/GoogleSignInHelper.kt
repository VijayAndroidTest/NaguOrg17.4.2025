package com.example.naguorg.util

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

object GoogleSignInHelper {

    fun getClient(
        activity: Activity
    ): GoogleSignInClient {

        val options = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestEmail()
            .requestIdToken("924827024871-ojjn6cajlevvta20d4mv6pkqg1u2b94q.apps.googleusercontent.com")
            .build()

        return GoogleSignIn.getClient(activity, options)
    }

    fun handleSignInResult(
        data: Intent?,
        onSuccess: (AuthCredential) -> Unit,
        onFailure: (String) -> Unit
    ) {

        try {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            val account = task.getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(
                account.idToken,
                null
            )

            onSuccess(credential)

        } catch (e: Exception) {

            onFailure(e.message ?: "Google Sign In Failed")
        }
    }
}