package com.example.tugasakhirpamyourtis.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Pastikan IP ini benar (10.0.2.2 untuk Emulator)
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    val instance: ApiService by lazy {

        // 1. Setting Logging (Agar kita bisa lihat data di Logcat)
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        // 2. Setting Client
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // 3. Setting Retrofit
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Masukkan client di sini
            .build()
            .create(ApiService::class.java)
    }
}