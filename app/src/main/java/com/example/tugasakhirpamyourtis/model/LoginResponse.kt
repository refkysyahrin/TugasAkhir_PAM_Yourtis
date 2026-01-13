package com.example.tugasakhirpamyourtis.model

data class LoginResponse (
    val message: String,
    val data: User?
)

data class User(
    val id_user: Int,
    val username: String,
    val email: String,
    val role: String,
    val no_hp: String,
    val alamat: String
)