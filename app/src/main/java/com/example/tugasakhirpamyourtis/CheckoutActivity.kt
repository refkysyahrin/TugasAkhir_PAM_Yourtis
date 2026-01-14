package com.example.tugasakhirpamyourtis

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasakhirpamyourtis.api.RetrofitClient
import com.example.tugasakhirpamyourtis.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // 1. Tangkap Data dari Detail Activity
        val idSayur = intent.getIntExtra("ID_SAYUR", 0)
        val namaSayur = intent.getStringExtra("NAMA")
        val harga = intent.getIntExtra("HARGA", 0)
        val jumlahBeli = intent.getIntExtra("JUMLAH", 0)
        val totalBayar = jumlahBeli * harga

        // 2. Inisialisasi UI
        val tvNama = findViewById<TextView>(R.id.tvNamaProduk)
        val tvRincian = findViewById<TextView>(R.id.tvRincianHarga)
        val tvTotal = findViewById<TextView>(R.id.tvTotalCheckout)
        val rgKirim = findViewById<RadioGroup>(R.id.rgPengiriman)
        val rgBayar = findViewById<RadioGroup>(R.id.rgPembayaran)
        val layoutRekening = findViewById<LinearLayout>(R.id.layoutRekening)
        val btnPesan = findViewById<Button>(R.id.btnBuatPesanan)

        // Set Data ke Tampilan
        tvNama.text = namaSayur
        tvRincian.text = "Rp $harga x $jumlahBeli Kg"
        tvTotal.text = "Total Bayar: Rp $totalBayar"

        // 3. Logika Tampilkan Rekening (Sesuai REQ-TRX-03)
        rgBayar.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbTransfer) {
                layoutRekening.visibility = View.VISIBLE
            } else {
                layoutRekening.visibility = View.GONE
            }
        }

        // 4. Klik Tombol Pesan
        btnPesan.setOnClickListener {
            // Cek Metode Kirim
            val selectedKirimId = rgKirim.checkedRadioButtonId
            val rbKirim = findViewById<RadioButton>(selectedKirimId)
            val metodeKirim = rbKirim.text.toString() // "Diantar" atau "Pickup"

            // Cek Metode Bayar
            val selectedBayarId = rgBayar.checkedRadioButtonId
            val rbBayar = findViewById<RadioButton>(selectedBayarId)
            val metodeBayar = rbBayar.text.toString() // "COD" atau "Transfer Bank"

            // Ambil ID Pembeli
            val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
            val idPembeli = prefs.getInt("ID_USER", 0)

            // Kirim ke Database
            prosesTransaksi(idPembeli, idSayur, jumlahBeli, totalBayar, metodeKirim, metodeBayar)
        }
    }

    private fun prosesTransaksi(idPembeli: Int, idSayur: Int, jumlah: Int, total: Int, kirim: String, bayar: String) {
        // Status awal selalu "Pending"
        val status = "Pending"

        RetrofitClient.instance.beliSayur(idPembeli, idSayur, jumlah, total, kirim, bayar, status)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CheckoutActivity, "Pesanan Berhasil Dibuat!", Toast.LENGTH_LONG).show()
                        finish() // Tutup halaman checkout
                    } else {
                        Toast.makeText(this@CheckoutActivity, "Gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@CheckoutActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}