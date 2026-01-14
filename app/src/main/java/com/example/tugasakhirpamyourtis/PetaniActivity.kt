package com.example.tugasakhirpamyourtis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpamyourtis.api.RetrofitClient
import com.example.tugasakhirpamyourtis.model.DashboardSummary
import com.example.tugasakhirpamyourtis.model.RiwayatTransaksi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class PetaniActivity : AppCompatActivity() {

    private lateinit var tvPendapatan: TextView
    private lateinit var tvJumlahOrder: TextView
    private lateinit var rvPesanan: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petani)

        // 1. Inisialisasi UI
        tvPendapatan = findViewById(R.id.tvTotalPendapatan)
        tvJumlahOrder = findViewById(R.id.tvTotalPesanan)
        rvPesanan = findViewById(R.id.rvPesananMasuk)

        // --- PERBAIKAN DI SINI (Tambahkan <Button> dan <ImageView>) ---
        val btnKelola = findViewById<Button>(R.id.btnKelolaProduk)
        val btnLaporan = findViewById<Button>(R.id.btnLihatLaporan)
        val btnLogout = findViewById<ImageView>(R.id.btnLogOutIcon)
        // -------------------------------------------------------------

        rvPesanan.layoutManager = LinearLayoutManager(this)

        // 2. Aksi Tombol
        btnKelola.setOnClickListener {
            // Ubah target ke halaman Kelola, bukan Tambah
            startActivity(Intent(this, KelolaSayurActivity::class.java))
        }

        btnLaporan.setOnClickListener {
            Toast.makeText(this, "Fitur Laporan sedang dikembangkan", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            getSharedPreferences("UserSession", MODE_PRIVATE).edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        // 3. Load Data
        loadDashboardData()
    }

    override fun onResume() {
        super.onResume()
        loadDashboardData() // Refresh data saat kembali ke halaman ini
    }

    private fun loadDashboardData() {
        // A. Ambil Ringkasan Pendapatan
        RetrofitClient.instance.getDashboardSummary().enqueue(object : Callback<DashboardSummary> {
            override fun onResponse(call: Call<DashboardSummary>, response: Response<DashboardSummary>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        // Format Rupiah
                        val formatRp = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                        tvPendapatan.text = formatRp.format(data.total_pendapatan)
                        tvJumlahOrder.text = "${data.total_pesanan} pesanan bulan ini"
                    }
                }
            }
            override fun onFailure(call: Call<DashboardSummary>, t: Throwable) {
                // Ignore error UI untuk summary agar tidak mengganggu user
            }
        })

        // B. Ambil Daftar Pesanan Terbaru
        RetrofitClient.instance.getPesananPetani().enqueue(object : Callback<List<RiwayatTransaksi>> {
            override fun onResponse(call: Call<List<RiwayatTransaksi>>, response: Response<List<RiwayatTransaksi>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    // Kita pakai Adapter yang sama dengan sebelumnya
                    rvPesanan.adapter = PetaniAdapter(list)
                }
            }
            override fun onFailure(call: Call<List<RiwayatTransaksi>>, t: Throwable) {
                Toast.makeText(this@PetaniActivity, "Gagal memuat pesanan", Toast.LENGTH_SHORT).show()
            }
        })
    }
}