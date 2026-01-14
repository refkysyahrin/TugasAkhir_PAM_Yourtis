package com.example.tugasakhirpamyourtis

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tugasakhirpamyourtis.model.Sayur

class KelolaSayurAdapter(
    private val listSayur: List<Sayur>,
    private val onDeleteClick: (Sayur) -> Unit // Fungsi callback buat Hapus
) : RecyclerView.Adapter<KelolaSayurAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgProduk)
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvHarga: TextView = view.findViewById(R.id.tvHarga)
        val tvStok: TextView = view.findViewById(R.id.tvStok)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnHapus: ImageButton = view.findViewById(R.id.btnHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kelola_sayur, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sayur = listSayur[position]

        holder.tvNama.text = sayur.nama_sayur
        holder.tvHarga.text = "Rp ${sayur.harga}"
        holder.tvStok.text = "Stok: ${sayur.stok}"

        // Load Gambar
        val fullUrl = "http://10.0.2.2:3000/uploads/" + sayur.gambar
        Glide.with(holder.itemView.context)
            .load(fullUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.img)

        // AKSI HAPUS
        holder.btnHapus.setOnClickListener {
            onDeleteClick(sayur) // Panggil fungsi hapus di Activity
        }

        // AKSI EDIT (Nanti kita buat EditSayurActivity)
        holder.btnEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditSayurActivity::class.java)

            // Kirim semua data agar form terisi otomatis
            intent.putExtra("ID", sayur.id_sayur)
            intent.putExtra("NAMA", sayur.nama_sayur)
            intent.putExtra("HARGA", sayur.harga)
            intent.putExtra("STOK", sayur.stok)
            intent.putExtra("DESKRIPSI", sayur.deskripsi)
            intent.putExtra("GAMBAR", sayur.gambar)

            context.startActivity(intent)
        }
    }

    override fun getItemCount() = listSayur.size
}