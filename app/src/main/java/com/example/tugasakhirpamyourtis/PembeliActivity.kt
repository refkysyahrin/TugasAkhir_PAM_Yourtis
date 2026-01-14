package com.example.tugasakhirpamyourtis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpamyourtis.api.RetrofitClient
import com.example.tugasakhirpamyourtis.model.Sayur
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PembeliActivity : AppCompatActivity() {

    private lateinit var rvSayur: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembeli)

        // 1. Inisialisasi Komponen
        val tvWelcome = findViewById<TextView>(R.id.tvWelcomePembeli)
        val btnRiwayat = findViewById<Button>(R.id.btnRiwayat)
        val btnLogout = findViewById<Button>(R.id.btnLogoutPembeli)

        rvSayur = findViewById(R.id.rvSayur)
        rvSayur.layoutManager = LinearLayoutManager(this) // Agar list menurun ke bawah

        // 2. Tampilkan Nama User dari Login
        val username = intent.getStringExtra("USERNAME") ?: "Pembeli"
        tvWelcome.text = "Halo, $username!"

        // 3. Tombol Riwayat (Pindah ke Halaman Riwayat)
        btnRiwayat.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
        }

        // 4. Tombol Logout (Hapus sesi & kembali ke Login)
        btnLogout.setOnClickListener {
            val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            // Hapus history agar user tidak bisa back ke halaman ini
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // 5. Panggil Data Sayur dari Server
        loadDataSayur()
    }

    private fun loadDataSayur() {
        RetrofitClient.instance.getDaftarSayur().enqueue(object : Callback<List<Sayur>> {
            override fun onResponse(call: Call<List<Sayur>>, response: Response<List<Sayur>>) {
                if (response.isSuccessful) {
                    val listData = response.body()
                    if (listData != null) {
                        // Pasang data ke Adapter
                        rvSayur.adapter = SayurAdapter(listData)
                    } else {
                        Toast.makeText(this@PembeliActivity, "Data sayur kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@PembeliActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Sayur>>, t: Throwable) {
                Toast.makeText(this@PembeliActivity, "Error Koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Agar saat user kembali dari halaman detail/riwayat, data di-refresh
    override fun onResume() {
        super.onResume()
        loadDataSayur()
    }
}