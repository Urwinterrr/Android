// Lokasi file: app/src/main/java/com/example/tugas/LoginActivity.kt

package com.example.tugas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// HANYA ADA SATU DEKLARASI KELAS DI FILE INI
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Pastikan Anda punya layout activity_login.xml

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
            } else {
                // Saat ini kita anggap login selalu berhasil untuk tujuan demo
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

                // =============================================================
                // BAGIAN PENTING YANG DITAMBAHKAN: Pindah ke MainActivity
                // =============================================================
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Menutup LoginActivity agar pengguna tidak bisa kembali dengan tombol back
                // =============================================================
            }
        } // <- tutup setOnClickListener
    } // <- tutup onCreate
} // <- tutup class

