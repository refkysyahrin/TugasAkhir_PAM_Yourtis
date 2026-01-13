package com.example.tugasakhirpamyourtis.api

import com.example.tugasakhirpamyourtis.model.LoginResponse
import com.example.tugasakhirpamyourtis.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call


interface ApiService {
    // Endpoint: http://10.0.2.2:3000/api/auth/register
    @POST("auth/register")
    fun register(@Body data: Map<String, String>): Call<RegisterResponse>

    // Endpoint: http://10.0.2.2:3000/api/auth/login
    @POST("auth/login")
    fun login(@Body data: Map<String, String>): Call<LoginResponse>
}