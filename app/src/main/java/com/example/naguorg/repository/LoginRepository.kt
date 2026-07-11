package com.example.naguorg.repository

import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth

class LoginRepository(
    private val auth: FirebaseAuth
) {

    fun signInWithGoogle(
        credential: AuthCredential,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        auth.signInWithCredential(credential)
            .addOnSuccessListener {

                onSuccess()

            }.addOnFailureListener {

                onFailure(it.message ?: "Login Failed")

            }
    }

    fun saveUser(
        context: Context,
        name: String,
        mobile: String
    ) {

        context.getSharedPreferences(
            "UserPrefs",
            Context.MODE_PRIVATE
        ).edit()
            .putString("userName", name)
            .putString("userMobile", mobile)
            .apply()
    }
}