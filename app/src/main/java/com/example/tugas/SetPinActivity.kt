package com.example.tugas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SetPinActivity : AppCompatActivity() {

    private var newPin = ""
    private var confirmPin = ""
    private var enteringConfirm = false

    private lateinit var txtDisplay: TextView
    private lateinit var txtTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_pin)

        txtDisplay = findViewById(R.id.txtSetPinDisplay)
        txtTitle = findViewById(R.id.txtSetTitle)

        val btnIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9
        )

        btnIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                val digit = (it as Button).text.toString()

                if (!enteringConfirm) {
                    if (newPin.length < 4) {
                        newPin += digit
                        updateDisplay(newPin)
                        if (newPin.length == 4) {
                            enteringConfirm = true
                            txtTitle.text = "Konfirmasi PIN"
                            txtDisplay.text = "----"
                        }
                    }
                } else {
                    if (confirmPin.length < 4) {
                        confirmPin += digit
                        updateDisplay(confirmPin)
                        if (confirmPin.length == 4) {
                            checkPin()
                        }
                    }
                }
            }
        }

        // DELETE
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (!enteringConfirm) {
                if (newPin.isNotEmpty()) {
                    newPin = newPin.dropLast(1)
                    updateDisplay(newPin)
                }
            } else {
                if (confirmPin.isNotEmpty()) {
                    confirmPin = confirmPin.dropLast(1)
                    updateDisplay(confirmPin)
                }
            }
        }
    }

    private fun updateDisplay(pin: String) {
        txtDisplay.text = pin.padEnd(4, '-')
    }

    private fun checkPin() {
        if (newPin == confirmPin) {
            DatabaseHelper(this).savePin(newPin)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            txtTitle.text = "PIN TIDAK SAMA, ULANGI!"
            newPin = ""
            confirmPin = ""
            enteringConfirm = false
            txtDisplay.text = "----"
        }
    }
}
