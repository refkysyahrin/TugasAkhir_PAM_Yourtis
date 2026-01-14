package com.example.tugasakhirpamyourtis

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpamyourtis.api.RetrofitClient
import com.example.tugasakhirpamyourtis.model.RiwayatTransaksi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        val rvRiwayat = findViewById<RecyclerView>(R.id.rvRiwayat)
        rvRiwayat.layoutManager = LinearLayoutManager(this)

        // Ambil ID User dari Session
        val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
        val idUser = prefs.getInt("ID_USER", 0)

        // Panggil API
        RetrofitClient.instance.getRiwayat(idUser).enqueue(object : Callback<List<RiwayatTransaksi>> {
            override fun onResponse(call: Call<List<RiwayatTransaksi>>, response: Response<List<RiwayatTransaksi>>) {
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    rvRiwayat.adapter = RiwayatAdapter(data)
                } else {
                    Toast.makeText(this@RiwayatActivity, "Gagal ambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<RiwayatTransaksi>>, t: Throwable) {
                Toast.makeText(this@RiwayatActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}