package com.example.tugasakhirpamyourtis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PetaniActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petani)

        // 1. Ambil Nama User dari Intent (Data kiriman dari Login)
        val username = intent.getStringExtra("USERNAME") ?: "Petani"

        // Tampilkan Nama di Layar
        val tvWelcome = findViewById<TextView>(R.id.tvWelcomeName)
        tvWelcome.text = "Halo, $username!"

        // 2. LOGIKA TOMBOL: Kelola Produk
        // Ini akan membuka halaman TambahSayurActivity yang baru saja Anda buat
        val btnKelola = findViewById<Button>(R.id.btnKelolaProduk)
        btnKelola.setOnClickListener {
            val intent = Intent(this, TambahSayurActivity::class.java)
            startActivity(intent)
        }

        // 3. LOGIKA TOMBOL: Logout (Keluar)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Hapus data sesi di memori HP (SharedPreferences)
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Kembali ke Halaman Login & Hapus Riwayat Halaman (agar tidak bisa di-Back)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show()
        finish()
    }
}