package com.example.tugasakhirpamyourtis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PetaniActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petani)

        // Ambil Data Nama dari Intent (yang dikirim LoginActivity)
        val username = intent.getStringExtra("USERNAME") ?: "Petani"

        findViewById<TextView>(R.id.tvWelcomeName).text = "Halo, $username"

        // Tombol Logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            // Hapus sesi (opsional) dan kembali ke Login
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Tombol Kelola Produk (Nanti kita buat halamannya)
        findViewById<Button>(R.id.btnKelolaProduk).setOnClickListener {
            // TODO: Pindah ke halaman Tambah Sayur
        }
    }
}