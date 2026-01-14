package com.example.tugasakhirpamyourtis.model

data class RiwayatTransaksi(
    val id_transaksi: String,
    val tgl_transaksi: String,
    val total_bayar: Int,
    val status: String,
    val metode_bayar: String
)
