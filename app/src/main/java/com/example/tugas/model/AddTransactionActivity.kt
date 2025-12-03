package com.example.tugas

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas.model.TransactionModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    private lateinit var rgTransactionType: RadioGroup
    private lateinit var rbIncome: RadioButton
    private lateinit var rbExpense: RadioButton
    private lateinit var etAmount: EditText
    private lateinit var tvCategoryLabel: TextView
    private lateinit var spinnerCategory: Spinner
    private lateinit var etNote: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        db = DatabaseHelper(this)

        // Inisialisasi semua view
        rgTransactionType = findViewById(R.id.rgTransactionType)
        rbIncome = findViewById(R.id.rbIncome)
        rbExpense = findViewById(R.id.rbExpense)
        etAmount = findViewById(R.id.etAmount)
        tvCategoryLabel = findViewById(R.id.tvCategoryLabel)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        etNote = findViewById(R.id.etNote)
        btnSave = findViewById(R.id.btnSaveTransaction)

        setupCategorySpinner()
        setupTransactionTypeListener()

        btnSave.setOnClickListener {
            saveTransaction()
        }
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("Makan", "Belanja", "Transportasi", "Lainnya")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    private fun setupTransactionTypeListener() {
        // Logika untuk menampilkan/menyembunyikan dropdown kategori
        rgTransactionType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbExpense) {
                tvCategoryLabel.visibility = View.VISIBLE
                spinnerCategory.visibility = View.VISIBLE
            } else { // Jika Pemasukan dipilih
                tvCategoryLabel.visibility = View.GONE
                spinnerCategory.visibility = View.GONE
            }
        }
    }

    private fun saveTransaction() {
        val amountText = etAmount.text.toString()
        if (amountText.isEmpty()) {
            Toast.makeText(this, "Nominal tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toInt()
        val type = if (rbIncome.isChecked) "INCOME" else "EXPENSE"
        val note = etNote.text.toString()

        // Jika pemasukan, kategori bisa diisi default. Jika pengeluaran, ambil dari spinner.
        val category = if (type == "INCOME") {
            "Pemasukan"
        } else {
            spinnerCategory.selectedItem.toString()
        }

        // Dapatkan tanggal hari ini dalam format yang bagus
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = sdf.format(Date())

        // Buat objek TransactionModel
        val transaction = TransactionModel(
            id = 0, // ID akan di-generate otomatis oleh database
            amount = amount,
            type = type,
            category = category,
            note = note,
            date = date
        )

        // Simpan ke database
        db.addTransaction(transaction)

        Toast.makeText(this, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show()
        finish() // Tutup activity dan kembali ke MainActivity
    }
}
