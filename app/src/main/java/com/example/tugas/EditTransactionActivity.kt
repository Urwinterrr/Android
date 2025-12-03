// Lokasi file: app/src/main/java/com/example/tugas/EditTransactionActivity.kt

package com.example.tugas

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas.model.TransactionModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// HANYA ADA SATU DEKLARASI KELAS DI SINI
class EditTransactionActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var transactionId: Int = -1

    // Deklarasikan semua komponen UI yang akan digunakan
    private lateinit var rgTransactionType: RadioGroup
    private lateinit var etAmount: EditText
    private lateinit var tvCategoryLabel: TextView
    private lateinit var spinnerCategory: Spinner
    private lateinit var etNote: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button // Bonus: Tombol Hapus

    // HANYA ADA SATU FUNGSI onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction) // Pastikan layout XML sudah dibuat

        db = DatabaseHelper(this)

        // 1. Inisialisasi semua komponen UI (findViewById)
        rgTransactionType = findViewById(R.id.rgTransactionType)
        etAmount = findViewById(R.id.etAmount)
        tvCategoryLabel = findViewById(R.id.tvCategoryLabel)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        etNote = findViewById(R.id.etNote)
        btnUpdate = findViewById(R.id.btnUpdateTransaction) // Ganti ID jika perlu
        btnDelete = findViewById(R.id.btnDeleteTransaction) // Ganti ID jika perlu

        setupCategorySpinner()
        setupTransactionTypeListener()

        // 2. Ambil ID transaksi yang dikirim dari adapter
        transactionId = intent.getIntExtra("TRANSACTION_ID", -1)

        // Validasi ID
        if (transactionId == -1) {
            Toast.makeText(this, "Error: ID Transaksi tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 3. Muat data lama dari database dan tampilkan di UI
        loadTransactionData()

        // 4. Atur OnClickListener untuk tombol update
        btnUpdate.setOnClickListener {
            updateTransactionData()
        }

        // 5. Atur OnClickListener untuk tombol hapus
        btnDelete.setOnClickListener {
            deleteTransactionData()
        }
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("Makan", "Belanja", "Transportasi", "Lainnya", "Pemasukan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    private fun setupTransactionTypeListener() {
        rgTransactionType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbExpenseEdit) { // Ganti ID jika perlu
                tvCategoryLabel.visibility = View.VISIBLE
                spinnerCategory.visibility = View.VISIBLE
            } else {
                tvCategoryLabel.visibility = View.GONE
                spinnerCategory.visibility = View.GONE
            }
        }
    }

    private fun loadTransactionData() {
        val transaction = db.getTransactionById(transactionId)

        if (transaction != null) {
            // Tampilkan data lama ke komponen UI
            etAmount.setText(transaction.amount.toString())
            etNote.setText(transaction.note)

            // Atur RadioButton
            if (transaction.type == "INCOME") {
                rgTransactionType.check(R.id.rbIncomeEdit) // Ganti ID jika perlu
            } else {
                rgTransactionType.check(R.id.rbExpenseEdit) // Ganti ID jika perlu
            }

            // Atur pilihan di Spinner
            val categories = arrayOf("Makan", "Belanja", "Transportasi", "Lainnya", "Pemasukan")
            val categoryPosition = categories.indexOf(transaction.category)
            if (categoryPosition >= 0) {
                spinnerCategory.setSelection(categoryPosition)
            }
        } else {
            Toast.makeText(this, "Gagal memuat data transaksi", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateTransactionData() {
        val amountText = etAmount.text.toString()
        if (amountText.isEmpty()) {
            Toast.makeText(this, "Nominal tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        // 1. Ambil semua nilai baru dari komponen UI
        val newAmount = amountText.toInt()
        val newType = if (rgTransactionType.checkedRadioButtonId == R.id.rbIncomeEdit) "INCOME" else "EXPENSE"
        val newCategory = if (newType == "INCOME") "Pemasukan" else spinnerCategory.selectedItem.toString()
        val newNote = etNote.text.toString()

        // Kita gunakan tanggal yang lama agar tidak berubah
        val oldTransaction = db.getTransactionById(transactionId)
        val date = oldTransaction?.date ?: SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())


        // 2. Buat objek TransactionModel yang baru dengan data yang sudah di-update
        val updatedTransaction = TransactionModel(
            id = transactionId,
            amount = newAmount,
            type = newType,
            category = newCategory,
            note = newNote,
            date = date
        )

        // 3. Panggil fungsi updateTransaction dari DatabaseHelper
        db.updateTransaction(updatedTransaction)

        // 4. Beri feedback dan tutup activity
        Toast.makeText(this, "Berhasil diperbarui!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun deleteTransactionData() {
        // Panggil fungsi delete dari DatabaseHelper
        db.deleteTransaction(transactionId)
        Toast.makeText(this, "Transaksi dihapus!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
