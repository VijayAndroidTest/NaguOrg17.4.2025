package com.example.naguorg.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.naguorg.repository.LoginRepository
import com.google.firebase.auth.AuthCredential

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf("")
        private set

    fun login(
        credential: AuthCredential,
        context: Context,
        name: String,
        mobile: String,
        onSuccess: () -> Unit
    ) {

        loading = true

        repository.signInWithGoogle(
            credential = credential,

            onSuccess = {

                repository.saveUser(
                    context = context,
                    name = name,
                    mobile = mobile
                )

                loading = false
                onSuccess()
            },

            onFailure = {

                loading = false
                error = it
            }
        )
    }

    fun clearError() {
        error = ""
    }
}