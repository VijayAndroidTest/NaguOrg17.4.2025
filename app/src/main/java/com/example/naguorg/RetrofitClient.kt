package com.example.naguorg

object RetrofitClient {

    private const val BASE_URL = "https://cashfreebackend-fkf3.onrender.com/"

    val api: CashfreeApi by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                retrofit2.converter.gson.GsonConverterFactory.create()
            )
            .build()
            .create(CashfreeApi::class.java)
    }
}