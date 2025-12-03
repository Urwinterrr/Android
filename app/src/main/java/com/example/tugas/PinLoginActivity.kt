package com.example.tugas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PinLoginActivity : AppCompatActivity() {

    private lateinit var txtDisplay: TextView
    private var pinInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_login)

        txtDisplay = findViewById(R.id.txtPinDisplay)

        val btnIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        // tombol angka 0–9
        btnIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                if (pinInput.length < 4) {
                    pinInput += (it as Button).text.toString()
                    updateDisplay()
                }

                if (pinInput.length == 4) {
                    checkPin()
                }
            }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (pinInput.isNotEmpty()) {
                pinInput = pinInput.dropLast(1)
                updateDisplay()
            }
        }
    }

    private fun updateDisplay() {
        txtDisplay.text = pinInput.padEnd(4, '-')
    }

    private fun checkPin() {
        val savedPin = DatabaseHelper(this).getPin()

        if (savedPin == pinInput) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            txtDisplay.text = "SALAH"
            pinInput = ""
        }
    }
}
