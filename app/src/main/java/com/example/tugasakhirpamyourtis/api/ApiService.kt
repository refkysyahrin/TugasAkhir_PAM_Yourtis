package com.example.tugasakhirpamyourtis.api

import com.example.tugasakhirpamyourtis.model.LoginResponse
import com.example.tugasakhirpamyourtis.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.Part


interface ApiService {
    // Endpoint: http://10.0.2.2:3000/api/auth/register
    @POST("auth/register")
    fun register(@Body data: Map<String, String>): Call<RegisterResponse>

    // Endpoint: http://10.0.2.2:3000/api/auth/login
    @POST("auth/login")
    fun login(@Body data: Map<String, String>): Call<LoginResponse>

    // UPLOAD SAYUR
    @Multipart
    @POST("sayur/tambah")
    fun tambahSayur(
        @Part("id_petani") idPetani: RequestBody,
        @Part("nama_sayur") namaSayur: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part
    ): Call<com.example.tugasakhirpamyourtis.model.RegisterResponse>
}