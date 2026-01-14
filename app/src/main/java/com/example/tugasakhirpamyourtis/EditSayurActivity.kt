package com.example.tugasakhirpamyourtis

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

class EditSayurActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var imgPreview: ImageView
    private var idSayur: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_sayur)

        // 1. Inisialisasi
        val etNama = findViewById<EditText>(R.id.etEditNama)
        val etHarga = findViewById<EditText>(R.id.etEditHarga)
        val etStok = findViewById<EditText>(R.id.etEditStok)
        val etDeskripsi = findViewById<EditText>(R.id.etEditDeskripsi)
        imgPreview = findViewById(R.id.imgEditPreview)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

        // 2. Tangkap Data Lama (Dari Adapter)
        idSayur = intent.getIntExtra("ID", 0)
        etNama.setText(intent.getStringExtra("NAMA"))
        etHarga.setText(intent.getIntExtra("HARGA", 0).toString())
        etStok.setText(intent.getIntExtra("STOK", 0).toString())
        etDeskripsi.setText(intent.getStringExtra("DESKRIPSI"))

        // Load Gambar Lama
        val gambarLama = intent.getStringExtra("GAMBAR")
        val fullUrl = "http://10.0.2.2:3000/uploads/" + gambarLama
        Glide.with(this).load(fullUrl).into(imgPreview)

        // 3. Logic Ganti Foto (Klik Gambar)
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                imgPreview.setImageURI(uri)
            }
        }
        imgPreview.setOnClickListener {
            pickImage.launch("image/*")
        }

        // 4. Logic Tombol Update
        btnUpdate.setOnClickListener {
            val nama = etNama.text.toString()
            val harga = etHarga.text.toString()
            val stok = etStok.text.toString()
            val deskripsi = etDeskripsi.text.toString()

            uploadUpdate(idSayur, nama, harga, stok, deskripsi, selectedImageUri)
        }
    }

    private fun uploadUpdate(id: Int, nama: String, harga: String, stok: String, deskripsi: String, uri: Uri?) {
        // Siapkan Text Body
        val namaBody = nama.toRequestBody("text/plain".toMediaTypeOrNull())
        val hargaBody = harga.toRequestBody("text/plain".toMediaTypeOrNull())
        val stokBody = stok.toRequestBody("text/plain".toMediaTypeOrNull())
        val deskripsiBody = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())

        // Siapkan Gambar (Bisa Null jika tidak diganti)
        var bodyGambar: MultipartBody.Part? = null
        if (uri != null) {
            val file = getFileFromUri(uri)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            bodyGambar = MultipartBody.Part.createFormData("gambar", file.name, requestFile)
        }

        // Panggil API Update
        RetrofitClient.instance.updateSayur(id, namaBody, hargaBody, stokBody, deskripsiBody, bodyGambar)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditSayurActivity, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show()
                        finish() // Kembali ke menu kelola
                    } else {
                        Toast.makeText(this@EditSayurActivity, "Gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@EditSayurActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Fungsi Helper Convert URI ke File
    private fun getFileFromUri(uri: Uri): File {
        val contentResolver = applicationContext.contentResolver
        val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return tempFile
    }
}