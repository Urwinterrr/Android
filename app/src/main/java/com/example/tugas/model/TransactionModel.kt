package com.example.tugas.model

data class TransactionModel(val id: Int,
                            val amount: Int,
                            val type: String,     // "INCOME" atau "EXPENSE"
                            val category: String, // "Pemasukan", "Makan", "Belanja", dll.
                            val note: String?,    // Bisa null jika tidak ada catatan
                            val date: String
)
