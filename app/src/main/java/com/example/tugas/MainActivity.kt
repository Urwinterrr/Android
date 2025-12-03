// Lokasi file: app/src/main/java/com/example/tugas/MainActivity.kt

package com.example.tugas

import android.content.Intent
import android.os.Bundle
// HAPUS BARIS-BARIS YANG SALAH DI SINI
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas.adapter.TransactionAdapter // Adapter baru
import com.example.tugas.model.TransactionModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var rvTransactions: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private var transactionList = mutableListOf<TransactionModel>()

    private lateinit var txtTotalBalance: TextView
    private lateinit var txtTotalIncome: TextView
    private lateinit var txtTotalExpense: TextView
    private lateinit var fabAddTransaction: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)

        // Inisialisasi Views
        txtTotalBalance = findViewById(R.id.txtTotalBalance)
        txtTotalIncome = findViewById(R.id.txtTotalIncome)
        txtTotalExpense = findViewById(R.id.txtTotalExpense)
        fabAddTransaction = findViewById(R.id.fabAddTransaction)
        rvTransactions = findViewById(R.id.rvTransactions)

        // Setup RecyclerView
        rvTransactions.layoutManager = LinearLayoutManager(this)
        transactionAdapter = TransactionAdapter(this, transactionList)
        rvTransactions.adapter = transactionAdapter

        // Fungsi FAB untuk pindah ke halaman tambah transaksi
        fabAddTransaction.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Setiap kali kembali ke halaman ini, muat ulang data dari database
        loadData()
    }

    private fun loadData() {
        // Ambil semua transaksi dari database
        transactionList = db.getAllTransactions().toMutableList()

        // Hitung total pemasukan, pengeluaran, dan saldo
        val totalIncome = transactionList.filter { it.type == "INCOME" }.sumOf { it.amount }
        val totalExpense = transactionList.filter { it.type == "EXPENSE" }.sumOf { it.amount }
        val totalBalance = totalIncome - totalExpense

        // Format angka menjadi format mata uang (Rp)
        val localeID = Locale("in", "ID")
        val currencyFormat = NumberFormat.getCurrencyInstance(localeID)
        currencyFormat.maximumFractionDigits = 0 // Hilangkan desimal

        // Tampilkan di UI
        txtTotalBalance.text = currencyFormat.format(totalBalance.toDouble())
        txtTotalIncome.text = currencyFormat.format(totalIncome.toDouble())
        txtTotalExpense.text = currencyFormat.format(totalExpense.toDouble())

        // Update data di adapter agar RecyclerView menampilkan data terbaru
        transactionAdapter.updateData(transactionList)
    }
}
