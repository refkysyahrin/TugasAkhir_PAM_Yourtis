package com.example.tugasakhirpamyourtis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpamyourtis.model.RiwayatTransaksi

class PetaniAdapter(private val listData: List<RiwayatTransaksi>) : RecyclerView.Adapter<PetaniAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId: TextView = view.findViewById(R.id.tvIdTrx)
        val tvNama: TextView = view.findViewById(R.id.tvNamaPembeli)
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusPetani)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan_petani, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listData[position]

        holder.tvId.text = "#${item.id_transaksi}"
        // Tampilkan nama pembeli (jika null, tulis Seseorang)
        holder.tvNama.text = "Pembeli: ${item.nama_pembeli ?: "Seseorang"}"
        holder.tvTotal.text = "Pendapatan: Rp ${item.total_bayar}"
        holder.tvStatus.text = item.status
    }

    override fun getItemCount() = listData.size
}