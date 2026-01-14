package com.example.tugasakhirpamyourtis

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailSayurActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_sayur)

        // 1. Inisialisasi Komponen
        val imgSayur = findViewById<ImageView>(R.id.imgDetailSayur)
        val tvNama = findViewById<TextView>(R.id.tvDetailNama)
        val tvHarga = findViewById<TextView>(R.id.tvDetailHarga)
        val tvStok = findViewById<TextView>(R.id.tvDetailStok)
        val tvDeskripsi = findViewById<TextView>(R.id.tvDetailDeskripsi)
        val btnBeli = findViewById<Button>(R.id.btnBeli)

        // 2. Ambil Data dari Intent (Kiriman dari Adapter)
        val nama = intent.getStringExtra("NAMA")
        val harga = intent.getIntExtra("HARGA", 0)
        val stok = intent.getIntExtra("STOK", 0)
        val deskripsi = intent.getStringExtra("DESKRIPSI")
        val gambar = intent.getStringExtra("GAMBAR") // Nama file gambar

        // 3. Tampilkan ke Layar
        tvNama.text = nama
        tvHarga.text = "Rp $harga"
        tvStok.text = "Stok Tersedia: $stok Kg"
        tvDeskripsi.text = deskripsi ?: "Tidak ada deskripsi."

        // Load Gambar Besar
        val fullUrl = "http://10.0.2.2:3000/uploads/" + gambar
        Glide.with(this).load(fullUrl).into(imgSayur)

        // 4. Tombol Beli (Nanti kita kembangkan fitur Transaksi di sini)
        btnBeli.setOnClickListener {
            Toast.makeText(this, "Fitur Beli akan segera hadir!", Toast.LENGTH_SHORT).show()
        }
    }
}