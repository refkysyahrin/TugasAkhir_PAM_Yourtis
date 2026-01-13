package com.example.tugasakhirpamyourtis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PembeliActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembeli)

        // 1. Ambil Nama User
        val username = intent.getStringExtra("USERNAME") ?: "Pembeli"

        // Tampilkan Nama
        val tvWelcome = findViewById<TextView>(R.id.tvWelcomePembeli)
        tvWelcome.text = "Selamat Belanja, $username!"

        // 2. LOGIKA TOMBOL: Logout
        val btnLogout = findViewById<Button>(R.id.btnLogoutPembeli)
        btnLogout.setOnClickListener {
            logout()
        }

        // TODO: Di sinilah nanti kita akan memanggil API untuk menampilkan Daftar Sayur
        // loadDaftarSayur()
    }

    private fun logout() {
        // Hapus data sesi
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Kembali ke Login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        Toast.makeText(this, "Sampai Jumpa!", Toast.LENGTH_SHORT).show()
        finish()
    }
}