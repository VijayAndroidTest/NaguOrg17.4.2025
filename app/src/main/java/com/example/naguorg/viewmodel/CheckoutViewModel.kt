package com.example.naguorg.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.naguorg.domain_api.OrderResponse
import com.example.naguorg.repository.CheckoutRepository
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val repository: CheckoutRepository
) : ViewModel() {

    var loading by mutableStateOf(false)
        private set

    var paymentSession by mutableStateOf<OrderResponse?>(null)
        private set

    var error by mutableStateOf("")
        private set

    fun createOrder(amount: Double) {
        viewModelScope.launch {

            loading = true

            try {
                paymentSession = repository.createOrder(amount)
            } catch (e: Exception) {
                error = e.message ?: ""
            }

            loading = false
        }
    }
}