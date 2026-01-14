package com.example.tugasakhirpamyourtis

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tugasakhirpamyourtis.model.Sayur

class SayurAdapter(private val listSayur: List<Sayur>) : RecyclerView.Adapter<SayurAdapter.ViewHolder>() {

    // Menghubungkan variabel dengan ID di layout item_sayur.xml
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgSayur: ImageView = view.findViewById(R.id.ivSayur)
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvHarga: TextView = view.findViewById(R.id.tvHarga)
        val tvStok: TextView = view.findViewById(R.id.tvStok)
    }

    // Memasang layout item_sayur.xml ke dalam RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sayur, parent, false)
        return ViewHolder(view)
    }

    // Mengisi data ke setiap kotak
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sayur = listSayur[position]

        // 1. Set Teks
        holder.tvNama.text = sayur.nama_sayur
        holder.tvHarga.text = "Rp ${sayur.harga} /kg"
        holder.tvStok.text = "Stok: ${sayur.stok} kg"

        // 2. Set Gambar dengan Glide
        // Ganti URL ini sesuai backend (10.0.2.2 untuk emulator)
        val baseUrl = "http://10.0.2.2:3000/uploads/"
        val fullUrl = baseUrl + sayur.gambar

        Glide.with(holder.itemView.context)
            .load(fullUrl)
            .placeholder(android.R.drawable.ic_menu_gallery) // Gambar sementara jika loading
            .error(android.R.drawable.stat_notify_error)     // Gambar jika error/gagal load
            .into(holder.imgSayur)

        // 3. LOGIKA KLIK ITEM (Pindah ke DetailSayurActivity)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailSayurActivity::class.java)

            // Kirim data sayur ke halaman sebelah (Detail)
            intent.putExtra("NAMA", sayur.nama_sayur)
            intent.putExtra("HARGA", sayur.harga)
            intent.putExtra("STOK", sayur.stok)
            intent.putExtra("DESKRIPSI", sayur.deskripsi)
            intent.putExtra("GAMBAR", sayur.gambar)

            context.startActivity(intent)
        }
    }

    // Menghitung jumlah data
    override fun getItemCount() = listSayur.size
}