package com.example.tugasakhirpamyourtis

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import okhttp3.RequestBody
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

        // 1. Pilih Gambar dari Galeri
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                imgPreview.setImageURI(selectedImageUri) // Tampilkan preview
                imgPreview.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

        imgPreview.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        // 2. Simpan ke Server
        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val harga = etHarga.text.toString().trim()
            val stok = etStok.text.toString().trim()
            val deskripsi = etDeskripsi.text.toString().trim()

            if (nama.isEmpty() || harga.isEmpty() || stok.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Lengkapi data dan pilih gambar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil ID Petani dari SharedPreference (Login Session)
            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
            val idPetani = sharedPref.getInt("ID_USER", 0)

            uploadSayur(idPetani, nama, harga, stok, deskripsi, selectedImageUri!!)
        }
    }

    private fun uploadSayur(idPetani: Int, nama: String, harga: String, stok: String, deskripsi: String, uri: Uri) {
        // Konversi Data Teks ke RequestBody
        val idBody = idPetani.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val namaBody = nama.toRequestBody("text/plain".toMediaTypeOrNull())
        val hargaBody = harga.toRequestBody("text/plain".toMediaTypeOrNull())
        val stokBody = stok.toRequestBody("text/plain".toMediaTypeOrNull())
        val deskripsiBody = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())

        // Konversi Gambar URI ke File Multipart
        val file = getFileFromUri(uri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val bodyGambar = MultipartBody.Part.createFormData("gambar", file.name, requestFile)

        // Panggil API
        RetrofitClient.instance.tambahSayur(idBody, namaBody, hargaBody, stokBody, deskripsiBody, bodyGambar)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TambahSayurActivity, "Sukses Tambah Sayur!", Toast.LENGTH_LONG).show()
                        finish() // Tutup halaman
                    } else {
                        Toast.makeText(this@TambahSayurActivity, "Gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@TambahSayurActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Fungsi Pembantu: Mengubah URI Galeri menjadi File Sementara agar bisa diupload
    private fun getFileFromUri(uri: Uri): File {
        val contentResolver = applicationContext.contentResolver
        val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
        tempFile.deleteOnExit()

        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()
        return tempFile
    }
}