package com.example.tugasakhirpamyourtis.api

import com.example.tugasakhirpamyourtis.model.DashboardSummary
import com.example.tugasakhirpamyourtis.model.LoginResponse
import com.example.tugasakhirpamyourtis.model.RegisterResponse
import com.example.tugasakhirpamyourtis.model.RiwayatTransaksi
import com.example.tugasakhirpamyourtis.model.Sayur // Pastikan model Sayur sudah dibuat
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // 1. REGISTER
    @POST("auth/register")
    fun register(@Body request: Map<String, String>): Call<RegisterResponse>

    // 2. LOGIN
    @POST("auth/login")
    fun login(@Body request: Map<String, String>): Call<LoginResponse>

    // 3. UPLOAD SAYUR (MULTIPART)
    @Multipart
    @POST("sayur")
    fun tambahSayur(
        @Part("id_petani") idPetani: RequestBody,
        @Part("nama_sayur") nama: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part
    ): Call<RegisterResponse>

    // 4. AMBIL DAFTAR SAYUR (GET) -> FITUR BARU
    @GET("sayur")
    fun getDaftarSayur(): Call<List<Sayur>>

    // 5. TRANSAKSI BELI (Sesuaikan dengan tb_transaksi)
    @FormUrlEncoded
    @POST("transaksi")
    fun beliSayur(
        @Field("id_pembeli") idPembeli: Int, // Sesuai kolom database
        @Field("id_sayur") idSayur: Int,     // Dikirim untuk disimpan (mungkin ke tb_detail_transaksi)
        @Field("jumlah") jumlah: Int,        // Jumlah beli
        @Field("total_bayar") totalBayar: Int, // Sesuai kolom database
        @Field("metode_kirim") metodeKirim: String, // "Diantar"
        @Field("metode_bayar") metodeBayar: String, // "COD"
        @Field("status") status: String      // "Pending"
    ): Call<RegisterResponse>

    // 6. AMBIL RIWAYAT (GET)
    @GET("transaksi/{id_user}")
    fun getRiwayat(
        @Path("id_user") idUser: Int
    ): Call<List<RiwayatTransaksi>>

    // 7. PETANI LIHAT PESANAN MASUK (GET)
    @GET("transaksi/petani/semua")
    fun getPesananPetani(): Call<List<RiwayatTransaksi>>

    // 8. AMBIL DATA DASHBOARD (Ringkasan)
    @GET("transaksi/admin/summary")
    fun getDashboardSummary(): Call<DashboardSummary>

    // 9. UPDATE SAYUR (PUT)
    // Menggunakan Multipart karena bisa jadi update foto juga
    @Multipart
    @PUT("sayur/{id}")
    fun updateSayur(
        @Path("id") idSayur: Int,
        @Part("nama_sayur") nama: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part? // Bisa null jika tidak ganti foto
    ): Call<RegisterResponse>

    // 10. HAPUS SAYUR (DELETE)
    @DELETE("sayur/{id}")
    fun hapusSayur(
        @Path("id") idSayur: Int
    ): Call<RegisterResponse>
}