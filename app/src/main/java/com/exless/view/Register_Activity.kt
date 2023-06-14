package com.exless.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowInsetsControllerCompat
import com.exless.R
import com.google.firebase.auth.FirebaseAuth

class Register_Activity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var toLoginTextView: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        fullNameEditText = findViewById(R.id.editTextText)
        emailEditText = findViewById(R.id.editTextTextEmailAddress)
        passwordEditText = findViewById(R.id.pass)
        confirmPasswordEditText = findViewById(R.id.pass2)
        registerButton = findViewById(R.id.button)
        toLoginTextView = findViewById(R.id.tv_tologin)
        firebaseAuth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Registrasi berhasil, lakukan tindakan yang diinginkan
                                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                                // Contoh: Pindah ke halaman utama aplikasi
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                // Registrasi gagal, tampilkan pesan error
                                Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Konfirmasi password tidak sesuai", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
            }
        }

        toLoginTextView.setOnClickListener {
            // Pindah ke halaman login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        //         testing to tambah
        findViewById<Button>(R.id.test_totambah).setOnClickListener {
            val intent = Intent(this, Tambahbahan_Activity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.test_see_item).setOnClickListener {
            val intent = Intent(this, seeitems_Activity::class.java)
            startActivity(intent)
        }
    }
}