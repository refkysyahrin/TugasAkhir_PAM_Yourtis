package com.example.tugasakhirpamyourtis

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasakhirpamyourtis.api.RetrofitClient
import com.example.tugasakhirpamyourtis.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 1. Inisialisasi Komponen UI
        val etUsername = findViewById<EditText>(R.id.etRegUsername)
        val etEmail = findViewById<EditText>(R.id.etRegEmail)
        val etPassword = findViewById<EditText>(R.id.etRegPassword)
        val etNoHp = findViewById<EditText>(R.id.etRegNoHp)
        val etAlamat = findViewById<EditText>(R.id.etRegAlamat)
        val spinnerRole = findViewById<Spinner>(R.id.spinnerRole)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

// 2. Isi Pilihan Role ke Spinner
        val roles = arrayOf("Pembeli", "Petani")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        spinnerRole.adapter = adapter

        // 3. Aksi Tombol Daftar
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val noHp = etNoHp.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            val role = spinnerRole.selectedItem.toString() // Ambil role yang dipilih

            // Validasi Input
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || noHp.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- KIRIM KE API REGISTER ---
            val requestData = mapOf(
                "username" to username,
                "email" to email,
                "password" to password,
                "no_hp" to noHp,
                "alamat" to alamat,
                "role" to role
            )

            RetrofitClient.instance.register(requestData).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_LONG).show()
                        finish() // Kembali ke halaman Login otomatis
                    } else {
                        // Jika gagal (misal email kembar)
                        Toast.makeText(this@RegisterActivity, "Gagal: Email mungkin sudah terdaftar.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error Koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
}