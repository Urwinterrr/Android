// Lokasi file: app/src/main/java/com/example/tugas/HistoryActivity.kt

package com.example.tugas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas.adapter.TransactionAdapter // DIUBAH: Menggunakan TransactionAdapter
import com.example.tugas.model.TransactionModel     // DIUBAH: Menggunakan TransactionModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter // DIUBAH: Nama variabel adapter
    private lateinit var db: DatabaseHelper
    private var transactionList = mutableListOf<TransactionModel>() // DIUBAH: List of TransactionModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history) // Pastikan Anda punya layout 'activity_history.xml'

        db = DatabaseHelper(this)

        recyclerView = findViewById(R.id.rvHistory) // Ganti rvHistory dengan ID RecyclerView Anda
        recyclerView.layoutManager = LinearLayoutManager(this)

        // DIUBAH: Gunakan TransactionAdapter di sini
        transactionAdapter = TransactionAdapter(this, transactionList)
        recyclerView.adapter = transactionAdapter

        // Panggil fungsi untuk memuat data
        loadTransactionData()
    }

    override fun onResume() {
        super.onResume()
        // Muat ulang data setiap kali kembali ke activity ini
        loadTransactionData()
    }

    private fun loadTransactionData() {
        // DIUBAH: Panggil fungsi getAllTransactions() yang sudah ada di DatabaseHelper
        val data = db.getAllTransactions()

        transactionList.clear()
        transactionList.addAll(data)
        transactionAdapter.updateData(transactionList) // Beri tahu adapter bahwa data telah diperbarui
    }
}
