package com.example.tugas.adapter

import android.content.Context
import android.content.Intent // Import Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas.EditTransactionActivity // Import EditTransactionActivity
import com.example.tugas.R
import com.example.tugas.model.TransactionModel
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter(
    private val context: Context,
    private var transactionList: List<TransactionModel>
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    // 1. BUAT INTERFACE UNTUK ONCLICK LISTENER
    interface OnItemClickListener {
        fun onItemClick(transaction: TransactionModel)
    }

    private var listener: OnItemClickListener? = null

    // 2. BUAT FUNGSI UNTUK MENGATUR LISTENER DARI ACTIVITY
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCategoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactionList[position]

        // ... (kode untuk mengatur format mata uang dan teks lainnya tetap sama) ...
        val localeID = Locale("in", "ID")
        val currencyFormat = NumberFormat.getCurrencyInstance(localeID)
        currencyFormat.maximumFractionDigits = 0

        if (transaction.type == "INCOME") {
            holder.tvAmount.text = "+ ${currencyFormat.format(transaction.amount)}"
            holder.tvAmount.setTextColor(Color.parseColor("#4CAF50"))
            holder.ivCategoryIcon.setImageResource(R.drawable.ic_income)
        } else {
            holder.tvAmount.text = "- ${currencyFormat.format(transaction.amount)}"
            holder.tvAmount.setTextColor(Color.parseColor("#F44336"))
            when (transaction.category.lowercase()) {
                "makan" -> holder.ivCategoryIcon.setImageResource(R.drawable.ic_makan)
                "belanja" -> holder.ivCategoryIcon.setImageResource(R.drawable.ic_belanja)
                "transportasi" -> holder.ivCategoryIcon.setImageResource(R.drawable.ic_transportasi) // Tambahkan jika ada
                else -> holder.ivCategoryIcon.setImageResource(R.drawable.ic_lainnya)
            }
        }

        holder.tvCategory.text = transaction.category
        holder.tvNote.text = transaction.note
        holder.tvDate.text = transaction.date

        // 3. ATUR ONCLICK LISTENER PADA ITEMVIEW DI SINI
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditTransactionActivity::class.java).apply {
                // Kirim ID transaksi ke EditTransactionActivity
                putExtra("TRANSACTION_ID", transaction.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = transactionList.size

    fun updateData(newList: List<TransactionModel>) {
        transactionList = newList
        notifyDataSetChanged()
    }
}
