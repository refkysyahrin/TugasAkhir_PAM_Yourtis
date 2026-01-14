package com.example.tugasakhirpamyourtis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailSayurActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_sayur)

        // 1. Inisialisasi Komponen UI
        val imgSayur = findViewById<ImageView>(R.id.imgDetailSayur)
        val tvNama = findViewById<TextView>(R.id.tvDetailNama)
        val tvHarga = findViewById<TextView>(R.id.tvDetailHarga)
        val tvStok = findViewById<TextView>(R.id.tvDetailStok)
        val tvDeskripsi = findViewById<TextView>(R.id.tvDetailDeskripsi)
        val etJumlah = findViewById<EditText>(R.id.etJumlahBeli)
        val btnBeli = findViewById<Button>(R.id.btnBeli)

        // 2. Tangkap Data dari Daftar Sayur (dikirim dari Adapter)
        val idSayur = intent.getIntExtra("ID_SAYUR", 0)
        val nama = intent.getStringExtra("NAMA") ?: ""
        val harga = intent.getIntExtra("HARGA", 0)
        val stok = intent.getIntExtra("STOK", 0)
        val deskripsi = intent.getStringExtra("DESKRIPSI")
        val gambar = intent.getStringExtra("GAMBAR")

        // 3. Tampilkan Data ke Layar
        tvNama.text = nama
        tvHarga.text = "Rp $harga /kg"
        tvStok.text = "Stok: $stok Kg"
        tvDeskripsi.text = deskripsi ?: "-"

        // Load Gambar
        // Pastikan IP address sesuai (10.0.2.2 untuk emulator)
        val fullUrl = "http://10.0.2.2:3000/uploads/" + gambar
        Glide.with(this)
            .load(fullUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(imgSayur)

        // 4. Logika Tombol Beli -> Pindah ke Halaman Checkout
        btnBeli.setOnClickListener {
            val jumlahStr = etJumlah.text.toString()

            // Validasi: Input Kosong
            if (jumlahStr.isEmpty()) {
                Toast.makeText(this, "Masukkan jumlah beli!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jumlah = jumlahStr.toInt()

            // Validasi: Minimal Beli 1
            if (jumlah <= 0) {
                Toast.makeText(this, "Minimal beli 1 Kg", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi: Stok Cukup?
            if (jumlah > stok) {
                Toast.makeText(this, "Stok tidak cukup! Hanya sisa $stok Kg", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Jika Valid, Pindah ke CheckoutActivity
            val intent = Intent(this, CheckoutActivity::class.java)

            // Bawa data penting ke halaman sebelah
            intent.putExtra("ID_SAYUR", idSayur)
            intent.putExtra("NAMA", nama)
            intent.putExtra("HARGA", harga)
            intent.putExtra("JUMLAH", jumlah) // Bawa jumlah yang diinput user

            startActivity(intent)
            // Kita tidak pakai finish() agar user bisa tekan Back untuk kembali ke detail ini
        }
    }
}