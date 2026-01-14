package com.example.tugasakhirpamyourtis

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpamyourtis.model.RiwayatTransaksi

class RiwayatAdapter(private val listData: List<RiwayatTransaksi>) : RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTgl: TextView = view.findViewById(R.id.tvTglTransaksi)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvId: TextView = view.findViewById(R.id.tvIdTransaksi)
        val tvTotal: TextView = view.findViewById(R.id.tvTotalBayar)
        val tvMetode: TextView = view.findViewById(R.id.tvMetode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listData[position]

        holder.tvTgl.text = item.tgl_transaksi
        holder.tvId.text = "ID: ${item.id_transaksi}"
        holder.tvTotal.text = "Rp ${item.total_bayar}"
        holder.tvMetode.text = "Bayar: ${item.metode_bayar}"
        holder.tvStatus.text = item.status

        // Ganti warna status biar cantik
        when (item.status) {
            "Pending" -> holder.tvStatus.setTextColor(Color.parseColor("#FF9800")) // Oranye
            "Dikirim" -> holder.tvStatus.setTextColor(Color.parseColor("#2196F3")) // Biru
            "Selesai" -> holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Hijau
            else -> holder.tvStatus.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount() = listData.size
}