package com.example.tugasakhirpamyourtis

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tugasakhirpamyourtis.api.RetrofitClient
import com.example.tugasakhirpamyourtis.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val etJumlah = findViewById<EditText>(R.id.etJumlahBeli)
        val btnBeli = findViewById<Button>(R.id.btnBeli)

        // 2. Tangkap Data dari Intent
        val idSayur = intent.getIntExtra("ID_SAYUR", 0)
        val nama = intent.getStringExtra("NAMA")
        val harga = intent.getIntExtra("HARGA", 0)
        val stok = intent.getIntExtra("STOK", 0)
        val deskripsi = intent.getStringExtra("DESKRIPSI")
        val gambar = intent.getStringExtra("GAMBAR")

        // 3. Tampilkan Data ke Layar
        tvNama.text = nama
        tvHarga.text = "Rp $harga /kg"
        tvStok.text = "Stok: $stok Kg"
        tvDeskripsi.text = deskripsi ?: "-"

        // Ganti URL ini sesuai IP Emulator/Laptop
        val fullUrl = "http://10.0.2.2:3000/uploads/" + gambar
        Glide.with(this).load(fullUrl).into(imgSayur)

        // 4. Logika Tombol Beli
        btnBeli.setOnClickListener {
            val jumlahStr = etJumlah.text.toString()

            // Validasi Input Kosong
            if (jumlahStr.isEmpty()) {
                Toast.makeText(this, "Masukkan jumlah beli!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jumlah = jumlahStr.toInt()

            // Validasi Stok
            if (jumlah > stok) {
                Toast.makeText(this, "Stok tidak cukup!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (jumlah <= 0) {
                Toast.makeText(this, "Minimal beli 1 Kg", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Hitung Total Bayar
            val totalBayar = jumlah * harga

            // Ambil ID Pembeli dari Session (Login)
            val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
            val idPembeli = prefs.getInt("ID_USER", 0)

            // Data Tambahan Sesuai Tabel Database
            val metodeKirim = "Diantar"
            val metodeBayar = "COD"
            val status = "Pending"

            // Kirim ke Database
            prosesTransaksi(idPembeli, idSayur, jumlah, totalBayar, metodeKirim, metodeBayar, status)
        }
    }

    private fun prosesTransaksi(
        idPembeli: Int,
        idSayur: Int,
        jumlah: Int,
        totalBayar: Int,
        metodeKirim: String,
        metodeBayar: String,
        status: String
    ) {
        RetrofitClient.instance.beliSayur(idPembeli, idSayur, jumlah, totalBayar, metodeKirim, metodeBayar, status)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DetailSayurActivity, "Transaksi Berhasil! Silakan tunggu kurir.", Toast.LENGTH_LONG).show()
                        finish() // Tutup halaman, kembali ke menu
                    } else {
                        Toast.makeText(this@DetailSayurActivity, "Gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@DetailSayurActivity, "Error Koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}