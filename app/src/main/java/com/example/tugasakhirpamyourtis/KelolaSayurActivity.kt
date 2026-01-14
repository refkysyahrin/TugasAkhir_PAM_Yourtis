package com.example.tugasakhirpamyourtis

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpamyourtis.api.RetrofitClient
import com.example.tugasakhirpamyourtis.model.RegisterResponse
import com.example.tugasakhirpamyourtis.model.Sayur
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KelolaSayurActivity : AppCompatActivity() {

    private lateinit var rvKelola: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelola_sayur)

        rvKelola = findViewById(R.id.rvKelolaSayur)
        rvKelola.layoutManager = LinearLayoutManager(this)

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData() // Refresh data saat kembali dari Edit
    }

    private fun loadData() {
        RetrofitClient.instance.getDaftarSayur().enqueue(object : Callback<List<Sayur>> {
            override fun onResponse(call: Call<List<Sayur>>, response: Response<List<Sayur>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()

                    // Pasang Adapter dengan Logic Hapus
                    rvKelola.adapter = KelolaSayurAdapter(list) { sayur ->
                        konfirmasiHapus(sayur)
                    }
                }
            }

            override fun onFailure(call: Call<List<Sayur>>, t: Throwable) {
                Toast.makeText(this@KelolaSayurActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun konfirmasiHapus(sayur: Sayur) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Produk")
            .setMessage("Yakin ingin menghapus ${sayur.nama_sayur}?")
            .setPositiveButton("Hapus") { _, _ ->
                hapusData(sayur.id_sayur)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun hapusData(idSayur: Int) {
        RetrofitClient.instance.hapusSayur(idSayur).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@KelolaSayurActivity, "Produk berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    loadData() // Refresh list
                } else {
                    Toast.makeText(this@KelolaSayurActivity, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@KelolaSayurActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}