package com.example.naguorg

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    val amount: Double
)

data class OrderResponse(
    @SerializedName("order_id")
    val orderId: String,

    @SerializedName("payment_session_id")
    val paymentSessionId: String
)
