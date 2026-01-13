package com.example.tugasakhirpamyourtis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasakhirpamyourtis.api.RetrofitClient
import com.example.tugasakhirpamyourtis.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Kenalkan Komponen UI
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        // 2. Aksi Tombol Login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- PROSES LOGIN KE API ---
            val requestData = mapOf(
                "email" to email,
                "password" to password
            )

            RetrofitClient.instance.login(requestData).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null && loginResponse.data != null) {

                            // LOGIN BERHASIL!
                            Toast.makeText(this@LoginActivity, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                            val role = loginResponse.data.role
                            val username = loginResponse.data.username
                            val idUser = loginResponse.data.id_user

                            // Simpan ID User (Penting untuk transaksi nanti)
                            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putInt("ID_USER", idUser)
                                putString("ROLE", role)
                                apply()
                            }

                            // Cek Role dan Arahkan
                            if (role == "Petani") {
                                val intent = Intent(this@LoginActivity, PetaniActivity::class.java)
                                intent.putExtra("USERNAME", username)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@LoginActivity, PembeliActivity::class.java)
                                intent.putExtra("USERNAME", username)
                                startActivity(intent)
                            }

                            // Tutup halaman login agar tidak bisa back
                            finish()

                            // TODO: Nanti kita arahkan ke Dashboard yang sesuai
                            // Untuk sekarang kita tampilkan pesan saja dulu
                            Toast.makeText(this@LoginActivity, "Masuk sebagai: $role", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Gagal! Cek Email/Password.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 3. Aksi Tombol Register (Pindah Halaman)
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}