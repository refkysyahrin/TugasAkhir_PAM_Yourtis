package com.example.tugasakhirpamyourtis

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasakhirpamyourtis.api.RetrofitClient
import com.example.tugasakhirpamyourtis.model.RegisterResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class TambahSayurActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var imgPreview: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_sayur)

        val etNama = findViewById<EditText>(R.id.etNamaSayur)
        val etHarga = findViewById<EditText>(R.id.etHargaSayur)
        val etStok = findViewById<EditText>(R.id.etStokSayur)
        val etDeskripsi = findViewById<EditText>(R.id.etDeskripsiSayur)
        imgPreview = findViewById(R.id.imgPreview)
        val btnSimpan = findViewById<Button>(R.id.btnSimpanSayur)

        // --- BAGIAN INI YANG MEMBUKA GALERI/FILE FILE ---
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                imgPreview.setImageURI(uri) // Tampilkan foto yang dipilih
                imgPreview.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                Toast.makeText(this, "Batal memilih foto", Toast.LENGTH_SHORT).show()
            }
        }

        // Saat gambar abu-abu diklik -> Buka File Manager
        imgPreview.setOnClickListener {
            pickImage.launch("image/*") // Filter: Hanya tampilkan file Gambar
        }

        // --- TOMBOL SIMPAN ---
        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val harga = etHarga.text.toString().trim()
            val stok = etStok.text.toString().trim()
            val deskripsi = etDeskripsi.text.toString().trim()

            if (nama.isEmpty() || harga.isEmpty() || stok.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Harap isi semua data dan PILIH FOTO!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil ID Petani dari Session Login
            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
            val idPetani = sharedPref.getInt("ID_USER", 0)

            uploadSayur(idPetani, nama, harga, stok, deskripsi, selectedImageUri!!)
        }
    }

    private fun uploadSayur(idPetani: Int, nama: String, harga: String, stok: String, deskripsi: String, uri: Uri) {
        // Konversi Teks
        val idBody = idPetani.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val namaBody = nama.toRequestBody("text/plain".toMediaTypeOrNull())
        val hargaBody = harga.toRequestBody("text/plain".toMediaTypeOrNull())
        val stokBody = stok.toRequestBody("text/plain".toMediaTypeOrNull())
        val deskripsiBody = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())

        // Konversi File Gambar
        try {
            val file = getFileFromUri(uri)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val bodyGambar = MultipartBody.Part.createFormData("gambar", file.name, requestFile)

            // Kirim ke Server
            RetrofitClient.instance.tambahSayur(idBody, namaBody, hargaBody, stokBody, deskripsiBody, bodyGambar)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@TambahSayurActivity, "Sukses Tambah Sayur!", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this@TambahSayurActivity, "Gagal Server: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@TambahSayurActivity, "Error Koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal memproses gambar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi Pembantu: Ubah URI Galeri jadi File Sementara
    private fun getFileFromUri(uri: Uri): File {
        val contentResolver = applicationContext.contentResolver
        val tempFile = File.createTempFile("upload_img", ".jpg", cacheDir)
        tempFile.deleteOnExit()

        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()
        return tempFile
    }
}